package net.evan.masterapp.logger;

public enum LogLevel {

    INFO("INFO", LogColor.CYAN),
    SUCCESS("SUCCESS", LogColor.GREEN),
    DEBUG("DEBUG", LogColor.PURPLE),
    WARN("WARN", LogColor.YELLOW),
    ERROR("ERROR", LogColor.RED),
    FATAL("FATAL", LogColor.RED_BG);

    private final String name;
    private final LogColor color;

    LogLevel(String name, LogColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public LogColor getColor() {
        return color;
    }
}
