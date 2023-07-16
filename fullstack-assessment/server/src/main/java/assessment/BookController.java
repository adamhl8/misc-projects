package assessment;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
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

@CrossOrigin
@RestController
@RequestMapping("/books")
public class BookController {

  private final BookRepo bookRepo;
  public BookController(BookRepo bookRepo) { this.bookRepo = bookRepo; }

  @GetMapping("")
  public List<Book> getAllBooks() {
    return bookRepo.findAll();
  }

  @GetMapping("/{id}")
  public Book getBook(@PathVariable Integer id) {
    return bookRepo.findById(id).orElseThrow();
  }

  @PostMapping("")
  @ResponseStatus(code = HttpStatus.CREATED)
  public Book createBook(@RequestBody Book book) {
    return bookRepo.save(book);
  }

  @PatchMapping("/{id}")
  public Book updateBook(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
    Book book = bookRepo.findById(id).orElseThrow();

    BeanWrapper accessor = PropertyAccessorFactory.forBeanPropertyAccess(book);
    Field[] bookFields = book.getClass().getDeclaredFields();
    for (Field field : bookFields) {
      String fieldName = field.getName();
      if (body.containsKey(fieldName)) accessor.setPropertyValue(fieldName, body.get(fieldName));
    }
  
    return bookRepo.save(book);
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void deleteBook(@PathVariable Integer id) {
    bookRepo.deleteById(id);
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String bookNotFound() {
    return "Book at given id does not exist.";
  }
}
