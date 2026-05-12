package com.example.stylesimplified.backend.services;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.example.stylesimplified.backend.models.*;

import java.sql.SQLException;

public class DatabaseManager {
    // baza de date intr-un container docker pe portul 5432 cu environment-ul setat asa pt development
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/wardrobe_db?user=admin&password=password123";

    public static void setupDatabase() {
        try (JdbcConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL)) {
            // table creation
            TableUtils.createTable(connectionSource, Top.class);
            TableUtils.createTable(connectionSource, Bottom.class);
            TableUtils.createTable(connectionSource, Accessory.class);
            System.out.println("PostgreSQL docker container started and tables created");
        } catch (SQLException e) {
            System.out.println("SQL exception occured");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
