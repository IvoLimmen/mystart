package org.limmen.mystart.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.limmen.mystart.Storage;
import org.limmen.mystart.User;
import spark.Request;
import spark.Response;

public class UserHandler extends BaseHandler {

  public UserHandler(Storage storage, ObjectMapper objectMapper) {
    super(storage, objectMapper);
  }

  public String login(Request req, Response res) {
    String email = req.params("email");
    String password = req.params("password");
    User user = getUserStorage().getByEmail(email);
    if (user != null && user.check(password)) {
      req.session().attribute("currentUser", user);
      res.cookie("/", "mystart", email + "|" + user.getPassword(), 60 * 60 * 24 * 7, true, true);
      res.status(200);
      return "";
    }
    
    return "";
  };

  public String logout(Request req, Response res) {
    req.session().removeAttribute("currentUser");
    return "";
  };
}
