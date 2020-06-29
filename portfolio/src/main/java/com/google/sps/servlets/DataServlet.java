// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.sps.data.Comment;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String COMMENT_NAME = "name";
  private static final String COMMENT_EMAIL = "email";
  private static final String COMMENT_DESCRIPTION = "description";
  private static final String COMMENT_TIMESTAMP = "timestamp";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String requestParam = request.getParameter("limit");
    Query query = new Query("comment").addSort(COMMENT_TIMESTAMP, Query.SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    Iterable<Entity> entityIterable = results.asIterable();
    if (requestParam != null) {
      int numCommentsToReturn = Integer.parseInt(requestParam);
      entityIterable = Iterables.limit(entityIterable, numCommentsToReturn);
    }

    for (Entity entity : entityIterable) {
      String name = (String) entity.getProperty(COMMENT_NAME);
      String email = (String) entity.getProperty(COMMENT_EMAIL);
      String description = (String) entity.getProperty(COMMENT_DESCRIPTION);
      long timestamp = (long) entity.getProperty(COMMENT_TIMESTAMP);

      Comment comment = new Comment(name, email, description, timestamp);
      comments.add(comment);
    }

    response.setContentType("application/json;");
    Gson gson = new Gson();
    String serializedJSON = gson.toJson(comments);
    response.getWriter().println(serializedJSON);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String username = request.getParameter("username");
    String email = userService.getCurrentUser().getEmail();
    String commentDescription = request.getParameter("description");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("comment");
    commentEntity.setProperty(COMMENT_NAME, username);
    commentEntity.setProperty(COMMENT_EMAIL, email);
    commentEntity.setProperty(COMMENT_DESCRIPTION, commentDescription);
    commentEntity.setProperty(COMMENT_TIMESTAMP, timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    response.setStatus(HttpServletResponse.SC_OK);

    response.sendRedirect("/");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

}
