package org.limmen.mystart.server.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;

public class UserServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  private final Path avatarDirectory;

  public UserServlet(LinkStorage linkStorage,
                     UserStorage userStorage,
                     MultipartConfigElement multipartConfigElement,
                     Path temporaryDirectory,
                     Path avatarDirectory) {
    super(linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
    this.avatarDirectory = avatarDirectory;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);

    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }

    User user = getUserStorage().get(userId);
    req.setAttribute("user", user);
    req.getRequestDispatcher("/account.jsp").include(req, res);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);

    String email = req.getParameter("email");
    String name = req.getParameter("name");
    String password = req.getParameter("password");

    if (exists(req, "saveButton")) {

      Long id = getLong(req, "id");
      User user = getUserStorage().get(id);
      String password2 = req.getParameter("password2");
      String fullName = req.getParameter("fullname");
      Part filePart = req.getPart("avatar");

      if (filePart != null) {
        String fileName = id + "-" + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        Path tempFile = Paths.get(avatarDirectory.toString(), fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile())) {
          fileOutputStream.write(IOUtils.toByteArray(filePart.getInputStream()));
        }
        user.setAvatarFileName("/avatar/" + fileName);
      }
      user.setOpenInNewTab(exists(req, "openinnewtab"));
      user.setEmail(email);
      user.setFullName(fullName);
      user.setName(name);
      if (password != null && password.length() > 0 && password2 != null && password.equals(password2)) {
        user.updatePassword(password);
      }
      getUserStorage().store(user);

      res.sendRedirect("/home");

    } else if (exists(req, "cancelButton")) {

      res.sendRedirect("/home");
    }
  }
}
