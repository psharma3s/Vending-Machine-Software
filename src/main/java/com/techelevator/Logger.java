package com.techelevator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String FILE_NAME = "Log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    public static void log(String message, double firstAmount, double secondAmount) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {

            String timeStamp = LocalDateTime.now().format(FORMATTER);

            writer.printf("%s %s $%.2f $%.2f\n", timeStamp, message, firstAmount, secondAmount);

        } catch (IOException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

}

