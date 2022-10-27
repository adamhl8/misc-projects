package checkpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class AuthenticatedUser {
  private boolean authenticated = false;
  private User user;

  public boolean getAuthenticated() { return authenticated; }
  public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }

  public User getUser() { return user; }
  public void setUser(User user) {
    this.user = user;
    this.authenticated = true;
  }
}
