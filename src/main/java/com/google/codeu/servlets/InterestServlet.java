package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Interest;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving user intersts. */
@WebServlet("/interest")
public class InterestServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /** Responds with the "about me" section for a particular user. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("text/html");
    String user = request.getParameter("user");
    if (user == null || user.equals("")) { // Request is invalid, return empty array
      response.getWriter().println("User is invalid");
      return;
    }
    List<Interest> likes = datastore.getInterests(user);
    for (Interest myInterest : likes) {
      response.getWriter().println(myInterest.getInfo());
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }
    String userEmail = userService.getCurrentUser().getEmail();
    String interest = Jsoup.clean(request.getParameter("my-interest"), Whitelist.none());
    Interest newInterest = new Interest(userEmail, interest);
    datastore.storeInterest(newInterest);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}
