package com.example.stylesimplified.backend.services;

import com.example.stylesimplified.backend.exceptions.CharacterLimitExceededException;
import com.example.stylesimplified.backend.models.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

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

    // relationship links
    private Dao<ClothingTagLink, Integer> clothingTagLinkDao;
    private Dao<Outfit, Integer> outfitDao;
    private Dao<OutfitClothingLink, Integer> outfitItemLinkDao;

    private WardrobeService(){
        this.wardrobe = new Wardrobe();

        // database setup needed
        try {
            JdbcPooledConnectionSource connection = DatabaseManager.getDatabase();
            this.topDao = DaoManager.createDao(connection, Top.class);
            this.bottomDao = DaoManager.createDao(connection, Bottom.class);
            this.accessoryDao = DaoManager.createDao(connection, Accessory.class);
            this.tagDao = DaoManager.createDao(connection, Tag.class);

            // many to many relationship associative tables
            this.clothingTagLinkDao = DaoManager.createDao(connection, ClothingTagLink.class);
            this.outfitDao = DaoManager.createDao(connection, Outfit.class);
            this.outfitItemLinkDao = DaoManager.createDao(connection, OutfitClothingLink.class);

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
            if (ci.getName().length() > 22){
                throw new CharacterLimitExceededException("Clothing name too long, expecting a shorter one");
            }
            if (ci instanceof Top) {
                topDao.create((Top) ci);
            }
            if (ci instanceof Bottom) {
                bottomDao.create((Bottom) ci);
            }
            if (ci instanceof Accessory) {
                accessoryDao.create((Accessory) ci);
            }

            wardrobe.getOwnedClothes().add(ci);
            System.out.println("Added clothing item");
        }
        catch (CharacterLimitExceededException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeClothingItem(ClothingItem ci){
        try {
            // exista relatia many to many intre clothing + tag -> tb sterse si tabelele asociative legate de o haina
            // TODO: in viitor aceeasi problema o sa apara si pt outfit-uri (un outfit are mai multe haine, daca se sterge un outfit tb sters si link-ul dintre ele si vice versa cand se sterge o haina tb eliminata din outfit automat)
            String type = "";
            if (ci instanceof Top) { type = "top";}
            if (ci instanceof Bottom) { type = "bottom";}
            if (ci instanceof Accessory) {type = "accessory"; }

            DeleteBuilder<ClothingTagLink, Integer> deleteBuilder = clothingTagLinkDao.deleteBuilder();
            // DELETE FROM clothing_items WHERE item_id = x AND clothing_type = Y
            deleteBuilder.where()
                    .eq("item_id", ci.getItemId())
                    .and()
                    .eq("clothing_type", type);
            deleteBuilder.delete();

            if (ci instanceof Top) {
                topDao.delete((Top) ci);
            }
            if (ci instanceof Bottom) {
                bottomDao.delete((Bottom) ci);
            }
            if (ci instanceof Accessory) {
                accessoryDao.delete((Accessory) ci);
            }

            wardrobe.getOwnedClothes().remove(ci);
            System.out.println("Removed clothing item");
        }
        catch (Exception e){
            System.out.println("Nu se poate sterge clothing item-ul");
            e.printStackTrace();
        }
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
        catch (CharacterLimitExceededException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeTag(Tag tag){
        try {
            DeleteBuilder<ClothingTagLink, Integer> deleteBuilder = clothingTagLinkDao.deleteBuilder();
            deleteBuilder.where()
                            .eq("tag_id", tag.getTagId());
            deleteBuilder.delete();

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

    public void createOutfit(Outfit outfit, List<ClothingItem> selectedClothes) {
        try {
            outfitDao.create(outfit);

            for (ClothingItem ci : selectedClothes) {
                String type = "";
                if (ci instanceof Top) { type = "top"; }
                if (ci instanceof Bottom) { type = "bottom"; }
                if (ci instanceof Accessory) { type = "accessory"; }

                OutfitClothingLink outfitClothingLink = new OutfitClothingLink(outfit, ci.getItemId(), type);
                outfitItemLinkDao.create(outfitClothingLink);
            }

            wardrobe.getAllOutfits().add(outfit);
            System.out.println("Outfit saved with a nr of" + selectedClothes.size() + "clothes");
        }
        catch (SQLException ex) {
            System.out.println("Couldn't create outfit");
            ex.printStackTrace();
        }
    }

    public void removeOutfit(Outfit outfit) {
        try {
            for (ClothingItem ci : outfit.getClothes()) { // since outfits have multiple clothes in a list each clothing link to that outfit has to be deleted
                DeleteBuilder<OutfitClothingLink, Integer> deleteBuilder = outfitItemLinkDao.deleteBuilder();
                deleteBuilder.where()
                        .eq("outfit_id", outfit.getId())
                        .and()
                        .eq("item_id", ci.getItemId());
                deleteBuilder.delete();
            }
        }
        catch (SQLException e){
            System.out.println("Couldn't remove outfit");
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

    // many-to-many relationships separately
    // clothing to tags:
    public void addClothingTagLink(ClothingTagLink link) {
        try {
            clothingTagLinkDao.create((ClothingTagLink) link);
            System.out.println("Saved tag to clothing");
        }
        catch (Exception e) {
            System.out.println("Nu s-a putut crea relatia dintre clothing item si tag");
            e.printStackTrace();
        }
    }

    public Wardrobe getWardrobe() {
        return wardrobe;
    }
}
