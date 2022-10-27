package checkpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/items")
public class ItemController {

  private final ItemRepo itemRepo;

  public ItemController(ItemRepo itemRepo) {
    this.itemRepo = itemRepo;
  }

  @CrossOrigin(origins = "http://localhost:5173")
  @GetMapping("")
  public List<Item> getAllItems() {
    return itemRepo.findAll();
  }

  @CrossOrigin(origins = "http://localhost:5173")
  @PostMapping("")
  @ResponseStatus(code = HttpStatus.CREATED)
  public Item createUser(@RequestBody Item item) {
    return itemRepo.save(item);
  }

  @CrossOrigin(origins = "http://localhost:5173")
  @PatchMapping("/{id}")
  public Item updateItem(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) throws Exception {
    Item item = itemRepo.findById(id).orElseThrow();

    if (body.get("completed") != null) item.setCompleted(body.get("completed"));
    else throw new InvalidRequestException();

    return itemRepo.save(item);
  }

  @CrossOrigin(origins = "http://localhost:5173")
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Integer id) {
    itemRepo.deleteById(id);
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
