package com.example.stylesimplified.backend.services;

import com.example.stylesimplified.backend.exceptions.CharacterLimitExceededException;
import com.example.stylesimplified.backend.models.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.util.HashSet;
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
    private Dao<Outfit, Integer> outfitDao;

    // relationship links
    private Dao<ClothingTagLink, Integer> clothingTagLinkDao;
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
            String type = "";
            if (ci.getName().length() > 22){
                throw new CharacterLimitExceededException("Clothing name too long, expecting a shorter one");
            }
            if (ci instanceof Top) {
                topDao.create((Top) ci);
                type = "top";
            }
            if (ci instanceof Bottom) {
                bottomDao.create((Bottom) ci);
                type = "bottom";
            }
            if (ci instanceof Accessory) {
                accessoryDao.create((Accessory) ci);
                type = "accessory";
            }

            for (Tag tag : ci.getTags()){
                ClothingTagLink link = new ClothingTagLink(tag, ci.getItemId(), type);
                clothingTagLinkDao.create(link);
            }

            wardrobe.getOwnedClothes().add(ci);
            System.out.println("Added clothing item");
        }
        catch (CharacterLimitExceededException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println("Nu se poate salva item-ul");
            System.out.println(e.getMessage());
        }
    }

    public void updateClothingItem(ClothingItem ci){
        try {
            if (ci instanceof Top){
                topDao.update((Top) ci);
            }
            if (ci instanceof  Bottom){
                bottomDao.update((Bottom) ci);
            }
            if (ci instanceof Accessory){
                accessoryDao.update((Accessory) ci);
            }
        }
        catch (Exception e){
            System.out.println("Nu poate fi updatat item-ul");
            System.out.println(e.getMessage());
        }
    }

    public void removeClothingItem(ClothingItem ci){
        try {
            // exista relatia many to many intre clothing + tag -> tb sterse si tabelele asociative legate de o haina
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

            QueryBuilder<OutfitClothingLink, Integer> query = outfitItemLinkDao.queryBuilder();
            query.where()
                    .eq("item_id", ci.getItemId())
                    .and()
                    .eq("clothing_type", type);
            List<OutfitClothingLink> affectedLinks = query.query();
            Set<Outfit> affectedFits = new HashSet<>();
            for (OutfitClothingLink link : affectedLinks) { // taking every table entry
                affectedFits.add(link.getOutfit());
            }

            DeleteBuilder<OutfitClothingLink, Integer> outftiClothingDelete = outfitItemLinkDao.deleteBuilder();
            outftiClothingDelete.where()
                    .eq("item_id", ci.getItemId())
                    .and()
                    .eq("clothing_type", type);
            outftiClothingDelete.delete();

            // problema pe care tb sa o repar = unele fit-uri pot deveni complet goale in momentul in care se sterge un clothing item
            // deci tb sterse
            for (Outfit fit : affectedFits){
                long nrOfClothes = outfitItemLinkDao.queryBuilder().where().eq("outfit_id", fit.getId()).countOf();
                if (nrOfClothes == 0){
                    outfitDao.delete(fit);
                    wardrobe.getAllOutfits().remove(fit);
                    System.out.println("Empty outfit generated by deleteing clothing item. Deleted outfit as well");
                }
                else {
                    // update for cache still needed even if the fit isnt empty
                    for (Outfit memoryOutfit : wardrobe.getAllOutfits()) {
                        if (memoryOutfit.getId() == fit.getId()) {
                            memoryOutfit.getClothes().remove(ci);
                            break;
                        }
                    }
                }
            }

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
            System.out.println("Tag salvat in DB");
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
            System.out.println("Tag object scos din DB");
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

                if (!outfit.getClothes().contains(ci)) {
                    outfit.getClothes().add(ci);
                }
            }

            wardrobe.getAllOutfits().add(outfit);
            System.out.println("Outfit saved with a nr of" + selectedClothes.size() + "clothes");
        }
        catch (SQLException ex) {
            System.out.println("Nu s-a putut crea outfitu-ul");
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
            System.out.println("Nu s-a putut sterge outfit-ul");
            e.printStackTrace();
        }
    }

    // load clothes from DB de preferat on startup
    // 3 tabele diferite desi vrem hainele in acelasi arraylist "wardrobe"
    public void loadItemsFromDb() {
        try {
            wardrobe.getOwnedClothes().clear();
            wardrobe.getTags().clear();

            wardrobe.getOwnedClothes().addAll(topDao.queryForAll());
            wardrobe.getOwnedClothes().addAll(bottomDao.queryForAll());
            wardrobe.getOwnedClothes().addAll(accessoryDao.queryForAll());
            wardrobe.getTags().addAll(tagDao.queryForAll());
            wardrobe.getAllOutfits().addAll(outfitDao.queryForAll());

            // pune tag-urile pt fiecare clothing item
            for (ClothingItem ci : wardrobe.getOwnedClothes()) {
                String type = "";
                if (ci instanceof Top) { type = "top"; }
                else if (ci instanceof Bottom) { type = "bottom"; }
                else if (ci instanceof Accessory) { type = "accessory"; }

                List<ClothingTagLink> links = clothingTagLinkDao.queryBuilder()
                        .where()
                        .eq("item_id", ci.getItemId())
                        .and()
                        .eq("clothing_type", type)
                        .query();

                for (ClothingTagLink link : links) {
                    Tag linkedTag = link.getTag();
                    if (linkedTag != null) {
                        if (!ci.getTags().contains(linkedTag)) {
                            ci.getTags().add(linkedTag);
                        }
                    }
                }
            }

            // aceeasi chestie doar ca tb puse hainele pt fiecare outfit
            for (Outfit fit : wardrobe.getAllOutfits()) {
                List<OutfitClothingLink> links = outfitItemLinkDao.queryBuilder()
                        .where()
                        .eq("outfit_id", fit.getId())
                        .query();

                for (OutfitClothingLink link : links) {
                    int linkedId = link.getItemId();
                    String linkedType = link.getClothingType();

                    for (ClothingItem ci : wardrobe.getOwnedClothes()) {
                        String ciType = "";
                        if (ci instanceof Top) ciType = "top";
                        else if (ci instanceof Bottom) ciType = "bottom";
                        else if (ci instanceof Accessory) ciType = "accessory";

                        // tb sa match uiasca si id-ul si tipul clothing itemului
                        if (linkedId == ci.getItemId() && ciType.equals(linkedType)) {
                            if (!fit.getClothes().contains(ci)) { // nu mai vreau duplicate...
                                fit.getClothes().add(ci);
                            }
                            break;
                        }
                    }
                }
            }

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
            System.out.println("S-a salvat relatia dintre clothing item si tag");
        }
        catch (Exception e) {
            System.out.println("Nu s-a putut crea relatia dintre clothing item si tag");
            e.printStackTrace();
        }
    }

    public Wardrobe getWardrobe() {
        return wardrobe;
    }

    public void removeClothingTagLink(ClothingItem currentItem, Tag tag) {
        try {
            DeleteBuilder<ClothingTagLink, Integer> deleteBuilder = clothingTagLinkDao.deleteBuilder();
            deleteBuilder.where()
                    .eq("item_id", currentItem.getItemId())
                    .and()
                    .eq("tag_id", tag.getTagId());
            deleteBuilder.delete();
            System.out.println("S-a sters tag-ul de pe item");
        }
        catch (Exception e){
            System.out.println("Nu s-a putut sterge tag-ul de pe item");
            System.out.println(e.getMessage());
        }
    }


    public void updateOutfitClothes(Outfit outfit, List<ClothingItem> newClothesSelection) {
        try {
            // 1. Wipe the old database links entirely
            DeleteBuilder<OutfitClothingLink, Integer> deleteBuilder = outfitItemLinkDao.deleteBuilder();
            deleteBuilder.where().eq("outfit_id", outfit.getId());
            deleteBuilder.delete();

            // 2. Clear the memory list
            outfit.getClothes().clear();

            // 3. Create the new links and add them back to memory
            for (ClothingItem ci : newClothesSelection) {
                String type = "";
                if (ci instanceof Top) type = "top";
                else if (ci instanceof Bottom) type = "bottom";
                else if (ci instanceof Accessory) type = "accessory";

                OutfitClothingLink link = new OutfitClothingLink(outfit, ci.getItemId(), type);
                outfitItemLinkDao.create(link);

                outfit.getClothes().add(ci);
            }
            System.out.println("Successfully updated outfit clothes.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateOutfit(Outfit outfit) {
        try {
            outfitDao.update(outfit);
            System.out.println("Successfully updated outfit details in DB.");
        } catch (SQLException e) {
            System.err.println("Failed to update outfit in DB.");
            System.out.println(e.getMessage());
        }
    }
}
