package com.example.recipebook.bd;

public class Session {
    private static int userId = -1;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static boolean isAuthenticated() {
        return userId != -1;
    }

    public static void logout() {
        userId = -1;
    }
}
