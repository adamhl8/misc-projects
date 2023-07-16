package checkpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  UserRepo userRepo;

  User user1;
  User user2;

  @BeforeEach
  void init() {
    user1 = new User("user1@email.com", "1234");
    user2 = new User("user2@email.com", "4321");
  }

  @Test
  @Transactional
  @Rollback
  public void getAllUsersReturnsAnArrayOfUsers() throws Exception {
    MockHttpServletRequestBuilder request = get("/users");

    this.mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isEmpty());
    
    this.userRepo.save(user1);
    this.userRepo.save(user2);

    this.mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(user1.getId()))
      .andExpect(jsonPath("$[0].email").value("user1@email.com"))
      .andExpect(jsonPath("$[0].password").doesNotExist())
      .andExpect(jsonPath("$[1].id").value(user2.getId()))
      .andExpect(jsonPath("$[1].password").doesNotExist())
      .andExpect(jsonPath("$[1].email").value("user2@email.com"));
  }

  @Test
  @Transactional
  @Rollback
  public void canGetUser() throws Exception {
    this.mvc.perform(get("/users/-1"))
      .andExpect(status().isNotFound())
      .andExpect(content().string("User does not exist."));

    this.userRepo.save(user1);

    this.mvc.perform(get(String.format("/users/%d", user1.getId())))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(user1.getId()))
      .andExpect(jsonPath("$.email").value("user1@email.com"))
      .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  @Transactional
  @Rollback
  public void canCreateUser() throws Exception {
    MockHttpServletRequestBuilder request = post("/users")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/user/user.json"));

    String response = this.mvc.perform(request)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.email").value("user1@email.com"))
      .andExpect(jsonPath("$.password").doesNotExist())
      .andReturn().getResponse().getContentAsString();
    
    ObjectMapper mapper = new ObjectMapper();
    int userId = mapper.readTree(response).findValue("id").asInt();

    this.mvc.perform(get(String.format("/users/%d", userId)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(userId))
      .andExpect(jsonPath("$.email").value("user1@email.com"))
      .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  @Transactional
  @Rollback
  public void canUpdateUser() throws Exception {
    MockHttpServletRequestBuilder invalidId = patch("/users/-1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/user/badUpdate.json"));

    // case where user ID is invalid
    this.mvc.perform(invalidId)
      .andExpect(status().isNotFound())
      .andExpect(content().string("User does not exist."));

    this.userRepo.save(user1);
    String requestPath = String.format("/users/%d", user1.getId());

    MockHttpServletRequestBuilder badRequest = patch(requestPath)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/user/badUpdate.json"));

    // case where user ID is valid but request data is not
    this.mvc.perform(badRequest)
      .andExpect(status().isBadRequest())
      .andExpect(content().string("Invalid request."));

    MockHttpServletRequestBuilder request = patch(requestPath)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/user/update.json"));

    this.mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(user1.getId()))
      .andExpect(jsonPath("$.email").value("user1@gmail.com"))
      .andExpect(jsonPath("$.password").doesNotExist());
    
    this.mvc.perform(get(requestPath))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(user1.getId()))
      .andExpect(jsonPath("$.email").value("user1@gmail.com"))
      .andExpect(jsonPath("$.password").doesNotExist());
    
    assertEquals("12345", userRepo.findById(user1.getId()).orElseThrow().getPassword());
  }

  @Test
  @Transactional
  @Rollback
  public void canDeleteUser() throws Exception {
    this.userRepo.save(user1);
    String requestPath = String.format("/users/%d", user1.getId());

    this.mvc.perform(delete(requestPath))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.count").value(userRepo.count()));

    this.mvc.perform(get(requestPath))
      .andExpect(status().isNotFound())
      .andExpect(content().string("User does not exist."));
  }

  @Test
  @Transactional
  @Rollback
  public void canAuthenticate() throws Exception {
    this.userRepo.save(user1);

    MockHttpServletRequestBuilder invalidPassword = post("/users/authenticate")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/user/badAuth.json"));
  
    this.mvc.perform(invalidPassword)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.authenticated").value(false))
      .andExpect(jsonPath("$.user").doesNotExist());

    MockHttpServletRequestBuilder request = post("/users/authenticate")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/user/user.json"));

    this.mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.authenticated").value(true))
      .andExpect(jsonPath("$.user.id").value(user1.getId()))
      .andExpect(jsonPath("$.user.email").value("user1@email.com"))
      .andExpect(jsonPath("$.password").doesNotExist());
  }

  private String getJSON(String path) throws Exception {
    URL url = this.getClass().getResource(path);
    return new String(Files.readAllBytes(Paths.get(url.getFile())));
  }
}
