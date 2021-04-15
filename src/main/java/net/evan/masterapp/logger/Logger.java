package net.evan.masterapp.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private void log(LogLevel level, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
        System.out.println(
                LogColor.RESET.getColor() + String.format("[%1$s] - %2$s - %3$s",
                        dateFormat.format(new Date()),
                        level.getColor().getColor() + level.getName() + LogColor.RESET.getColor(),
                        message
                )
        );
    }

    public void warn(String message) {
        this.log(LogLevel.WARN, message);
    }

    public void success(String message) {
        this.log(LogLevel.SUCCESS, message);
    }

    public void fatal(String message) {
        this.log(LogLevel.FATAL, message);
    }

    public void error(String message) {
        this.log(LogLevel.ERROR, message);
    }

    public void info(String message) {
        this.log(LogLevel.INFO, message);
    }

    public void debug(String message) {
        this.log(LogLevel.DEBUG, message);
    }

    public void space() {
        this.log(LogLevel.INFO, " ");
    }

}
