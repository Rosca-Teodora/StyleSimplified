package com.example.stylesimplified.backend.services;

import com.example.stylesimplified.backend.exceptions.CharacterLimitExceededException;
import com.example.stylesimplified.backend.models.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.sql.SQLException;

// singleton "menu" class
// service that manipulates all the CRUD operations
public class WardrobeService {
    private static WardrobeService instance = null;
    private final Wardrobe wardrobe;

    // dao = data access objects
    // pt ca se poate alege ce tip de clothing item se face dar in database se pastreaza doar unul (si fara mostenire) tb sa se verifice instanta obiectului ce vrea sa fie creat
    private Dao<Top, Integer> topDao;
    private Dao<Bottom, Integer> bottomDao;
    private Dao<Accessory, Integer> accessoryDao;
    private Dao<Tag, Integer> tagDao;

    private WardrobeService(){
        this.wardrobe = new Wardrobe();

        // database setup needed
        try {
            JdbcPooledConnectionSource connection = DatabaseManager.getDatabase();
            this.topDao = DaoManager.createDao(connection, Top.class);
            this.bottomDao = DaoManager.createDao(connection, Bottom.class);
            this.accessoryDao = DaoManager.createDao(connection, Accessory.class);
            this.tagDao = DaoManager.createDao(connection, Tag.class);
        } catch (SQLException e) {
            System.out.println("Eroare in gestionarea bazei de date");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WardrobeService getInstance() {
        if (instance == null){
            instance = new WardrobeService();
        }
        return instance;
    }

    public void addClothingItem(ClothingItem ci){
        try {
            if (ci instanceof Top) {
                topDao.create((Top) ci);
            }
            if (ci instanceof Bottom) {
                bottomDao.create((Bottom) ci);
            }
            if (ci instanceof Accessory) {
                accessoryDao.create((Accessory) ci);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wardrobe.getOwnedClothes().add(ci);
        System.out.println("Added clothing item");
    }

    public void removeClothingItem(ClothingItem ci){
        try {
            if (ci instanceof Top) {
                topDao.delete((Top) ci);
            }
            if (ci instanceof Bottom) {
                bottomDao.delete((Bottom) ci);
            }
            if (ci instanceof Accessory) {
                accessoryDao.delete((Accessory) ci);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        wardrobe.getOwnedClothes().remove(ci);
        System.out.println("Removed clothing item");
    }

    public void addTag(Tag t) throws CharacterLimitExceededException {
        if (t.getNume().length() > 50){
            throw new CharacterLimitExceededException("Tag name too long, it must be shorter than 50 chars");
        }
        try {
            tagDao.create((Tag) t);
            wardrobe.getTags().add(t);
            System.out.println("Added tag to memory");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTag(Tag tag){
        try {
            tagDao.delete((Tag) tag);
            wardrobe.getTags().remove(tag);
            System.out.println("Tag object removed from DB");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTag(Tag tag) {
        try {
            tagDao.update(tag);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // load clothes from DB de preferat on startup
    // 3 tabele diferite desi vrem hainele in acelasi arraylist "wardrobe"
    public void loadItemsFromDb() {
        try {
            // clothes
            wardrobe.getOwnedClothes().clear();

            wardrobe.getOwnedClothes().addAll(topDao.queryForAll());
            wardrobe.getOwnedClothes().addAll(bottomDao.queryForAll());
            wardrobe.getOwnedClothes().addAll(accessoryDao.queryForAll());

            // tags
            wardrobe.getTags().clear();
            wardrobe.getTags().addAll(tagDao.queryForAll());

            System.out.println("Loaded items from DB");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Wardrobe getWardrobe() {
        return wardrobe;
    }
}
