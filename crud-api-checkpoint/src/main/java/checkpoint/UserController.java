package checkpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepo userRepo;

  public UserController(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @GetMapping("")
  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  @GetMapping("/{id}")
  public User getUser(@PathVariable Integer id) {
    return userRepo.findById(id).orElseThrow();
  }

  @PostMapping("")
  @ResponseStatus(code = HttpStatus.CREATED)
  public User createUser(@RequestBody User user) {
    return userRepo.save(user);
  }

  @PatchMapping("/{id}")
  public User updateUser(@PathVariable Integer id, @RequestBody User userRequeest) throws Exception {
    User user = userRepo.findById(id).orElseThrow();

    if (userRequeest.getEmail() != null) user.setEmail(userRequeest.getEmail());
    else throw new InvalidRequestException();
    if (userRequeest.getPassword() != null) user.setPassword(userRequeest.getPassword());

    return userRepo.save(user);
  }

  @DeleteMapping("/{id}")
  public Map<String, Long> deleteUser(@PathVariable Integer id) {
    userRepo.deleteById(id);
    Map<String, Long> countMap = new HashMap<String, Long>();
    countMap.put("count", userRepo.count());
    return countMap;
  }

  @PostMapping("/authenticate")
  public AuthenticatedUser authenticate(@RequestBody User userRequest) {
    AuthenticatedUser authenticatedUser = new AuthenticatedUser();

    User user = userRepo.findByEmail(userRequest.getEmail());
    if (userRequest.getPassword().equals(user.getPassword())) authenticatedUser.setUser(user);
    
    return authenticatedUser;
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String userNotfound() {
    return "User does not exist.";
  }

  @ExceptionHandler(InvalidRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String invalidRequest() {
    return "Invalid request.";
  }
}
