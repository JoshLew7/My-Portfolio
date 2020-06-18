package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.sps.data.Authentication;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthenticationStatus extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();

        UserService userService = UserServiceFactory.getUserService();
        if (userService.isUserLoggedIn()) {
            String redirectAfterLogout = "/";
            String logoutURL = userService.createLogoutURL(redirectAfterLogout);

            Authentication userLoginStatus = Authentication.createLoggedInInfo(logoutURL);
            response.getWriter().println(gson.toJson(userLoginStatus));
        } else {
            String redirectAfterLogin = "/";
            String loginURL = userService.createLoginURL(redirectAfterLogin);

            Authentication userLoginStatus = Authentication.createLoggedOutInfo(loginURL);
            response.getWriter().println(gson.toJson(userLoginStatus));
        }
    }
}
