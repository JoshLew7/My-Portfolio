package com.google.sps.data;

public class Authentication {
    private final boolean isLoggedIn;
    private final String loginUrl;
    private final String logoutUrl;

    public Authentication(boolean isLoggedIn, String loginUrl, String logoutUrl) {
        this.isLoggedIn = isLoggedIn;
        this.loginUrl = loginUrl;
        this.logoutUrl = logoutUrl;
    }

    public static Authentication createLoggedInInfo(String url) {
        return new Authentication(true, "", url);
    }

    public static Authentication createLoggedOutInfo(String url) {
        return new Authentication(false, url, "");
    }
}
