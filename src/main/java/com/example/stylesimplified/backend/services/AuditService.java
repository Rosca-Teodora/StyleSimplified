package com.example.stylesimplified.backend.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance = null;
    private static final String CSV_FILE_PATH = "audit_log.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {
        // Private constructor for Singleton
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);

        // Use try-with-resources to ensure the file writer is closed automatically
        try (FileWriter fw = new FileWriter(CSV_FILE_PATH, true); // 'true' means append mode
             PrintWriter pw = new PrintWriter(fw)) {

            // Format requested: nume_actiune, timestamp
            pw.println(actionName + "," + timestamp);

        } catch (IOException e) {
            System.err.println("Error writing to audit file: " + e.getMessage());
        }
    }
}