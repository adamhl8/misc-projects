package assessment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String title;
  private String author;
  private Boolean favorite = false;

  public Book() {}

  public Book(String title, String author) {
    this.title = title;
    this.author = author;
  }

  public Integer getId() { return id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getAuthor() { return author; }
  public void setAuthor(String author) { this.author = author; }

  public Boolean getFavorite() { return favorite; }
  public void setFavorite(Boolean favorite) { this.favorite = favorite; }
}
