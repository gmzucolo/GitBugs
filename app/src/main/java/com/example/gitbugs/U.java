package com.example.gitbugs;

import java.util.HashMap;
import java.util.Map;

public class U {

    public static final String BASE_URL = "https://api.github.com";
    public static final String LOGIN_URL = "https://github.com/login/oauth";
    public static final String CLIENT_ID = "40912848bfc009cb085d";
    public static final String CLIENT_SECRET = "949615b65de266096f7ca23ad5cc0b3e62b16570";
    public static final String SCOPE = "admin:repo_hook%20user";
    public static final String STATE = "gitbugs";
    public static String TOKEN = "";
    public static Map<String, String> HEADERS = new HashMap<>();

    public static String checkNull(String value) {
        if (value.equals("null")) {
            return "";
        } else return value;
    }
}
