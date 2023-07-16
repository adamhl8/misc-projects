package assessment;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private BookRepo bookRepo;

  private Book book1;
  private Book book2;

  private ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void init() {
    book1 = new Book("Warbreaker", "Brandon Sanderson");
    book2 = new Book("The Hero of Ages", "Brandon Sanderson");
  }

  @Test
  @Transactional
  @Rollback
  public void canGetAllBooks() throws Exception {
    MockHttpServletRequestBuilder request = get("/books");

    mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isEmpty());
    
    bookRepo.save(book1);
    bookRepo.save(book2);

    mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(book1.getId()))
      .andExpect(jsonPath("$[0].title").value("Warbreaker"))
      .andExpect(jsonPath("$[0].author").value("Brandon Sanderson"))
      .andExpect(jsonPath("$[0].favorite").value(false))
      .andExpect(jsonPath("$[1].id").value(book2.getId()))
      .andExpect(jsonPath("$[1].title").value("The Hero of Ages"))
      .andExpect(jsonPath("$[1].author").value("Brandon Sanderson"))
      .andExpect(jsonPath("$[1].favorite").value(false));
  }

  @Test
  @Transactional
  @Rollback
  public void canGetBook() throws Exception {
    mvc.perform(get("/books/-1"))
      .andExpect(status().isNotFound())
      .andExpect(content().string("Book at given id does not exist."));

    bookRepo.save(book1);

    mvc.perform(get(String.format("/books/%d", book1.getId())))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(book1.getId()))
      .andExpect(jsonPath("$.title").value("Warbreaker"))
      .andExpect(jsonPath("$.author").value("Brandon Sanderson"))
      .andExpect(jsonPath("$.favorite").value(false));
  }

  @Test
  @Transactional
  @Rollback
  public void canCreateBook() throws Exception {
    MockHttpServletRequestBuilder request = post("/books")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/json/book.json"));

    String response = mvc.perform(request)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNumber())
      .andExpect(jsonPath("$.title").value("The Way of Kings"))
      .andExpect(jsonPath("$.author").value("Brandon Sanderson"))
      .andExpect(jsonPath("$.favorite").value(false))
      .andReturn().getResponse().getContentAsString();
    
    int bookId = mapper.readTree(response).findValue("id").asInt();

    mvc.perform(get(String.format("/books/%d", bookId)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(bookId))
      .andExpect(jsonPath("$.title").value("The Way of Kings"))
      .andExpect(jsonPath("$.author").value("Brandon Sanderson"))
      .andExpect(jsonPath("$.favorite").value(false));
  }

  
  @Test
  @Transactional
  @Rollback
  public void canUpdateBook() throws Exception {
    MockHttpServletRequestBuilder requestWithInvalidId = patch("/books/-1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/json/book.json"));

    // case where book ID is invalid
    mvc.perform(requestWithInvalidId)
      .andExpect(status().isNotFound())
      .andExpect(content().string("Book at given id does not exist."));

    bookRepo.save(book1);
    String requestPath = String.format("/books/%d", book1.getId());

    MockHttpServletRequestBuilder request = patch(requestPath)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(getJSON("/json/bookUpdate.json"));

    mvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(book1.getId()))
      .andExpect(jsonPath("$.title").value("Words of Radiance"))
      .andExpect(jsonPath("$.author").value("Brando Sando"))
      .andExpect(jsonPath("$.favorite").value(true));
    
    mvc.perform(get(requestPath))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(book1.getId()))
      .andExpect(jsonPath("$.title").value("Words of Radiance"))
      .andExpect(jsonPath("$.author").value("Brando Sando"))
      .andExpect(jsonPath("$.favorite").value(true));
  }

  @Test
  @Transactional
  @Rollback
  public void canDeleteBook() throws Exception {
    bookRepo.save(book1);
    String requestPath = String.format("/books/%d", book1.getId());

    mvc.perform(delete(requestPath)).andExpect(status().isNoContent());

    mvc.perform(get(requestPath))
      .andExpect(status().isNotFound())
      .andExpect(content().string("Book at given id does not exist."));
  }

  private String getJSON(String path) throws Exception {
    URL url = getClass().getResource(path);
    return new String(Files.readAllBytes(Paths.get(url.getFile())));
  }
}
