package com.example.stylesimplified.backend.services;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.example.stylesimplified.backend.models.*;

public class DatabaseManager {
    // baza de date intr-un container docker pe portul 5432 cu environment-ul setat asa pt development
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/wardrobe_db?user=admin&password=password123";
    // singleton pattern pt database pool
    private static JdbcPooledConnectionSource connectionSource;
    private DatabaseManager(){}

    public static JdbcPooledConnectionSource getDatabase() throws Exception {
        if (connectionSource == null) {
            connectionSource = new JdbcPooledConnectionSource(DATABASE_URL);
            connectionSource.setTestBeforeGet(true); // verifica sa nu fi murit conexiunea cu baza de date inainte sa fie trimisa
        }
        return connectionSource;
    }

    public static void setupDatabase() {
        try {
            JdbcPooledConnectionSource sursa = getDatabase();

            TableUtils.createTable(sursa, Top.class);
            TableUtils.createTable(sursa, Bottom.class);
            TableUtils.createTable(sursa, Accessory.class);
            TableUtils.createTable(sursa, Tag.class);

            System.out.println("PostgreSQL with docker connected + tables made");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // e nevoie de metode closeConnection fiindca altfel pot aparea memory leak-uri
    public static void closeConnection() {
        if (connectionSource != null){
            try {
                connectionSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
