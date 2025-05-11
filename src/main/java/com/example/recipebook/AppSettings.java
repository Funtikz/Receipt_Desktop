package com.example.recipebook;

public class AppSettings {
    private static AppSettings instance;
    private String fontName = "System"; // значение по умолчанию

    private AppSettings() {}

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }
}
