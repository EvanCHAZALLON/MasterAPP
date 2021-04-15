package net.evan.masterapp.logger;

public enum LogColor {

    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    RED_BG("\u001B[41m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m");

    private final String color;

    LogColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
