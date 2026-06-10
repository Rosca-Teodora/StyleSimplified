package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.*;

@DatabaseTable(tableName = "outfits")
public class Outfit {
    @DatabaseField(generatedId = true)
    private int outfitId;

    @DatabaseField
    private String dbDateString;

    @DatabaseField
    protected String name;

    @DatabaseField
    protected String imagePath;

    protected List<ClothingItem> items = new ArrayList<>();

    protected Set<Tag> outfitTags = new HashSet<>();

    private LocalDate dateUploaded;

    public LocalDate getDateUploaded(){
        return this.dateUploaded;
    }
    public String getName() {
        return name;
    }
    public List<ClothingItem> getClothes() {
        return this.items;
    }
    public int getId(){ return this.outfitId; }
    public String getImagePath() { return this.imagePath; }

    public Outfit() {}

    public Outfit(String name) {
        this.name = name;
    }

    public Outfit(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Outfit outfit = (Outfit) o;
        return outfitId == outfit.outfitId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(outfitId);
    }

    public void setName(String text) {
        this.name = text;
    }

    public void setCustomThumbnailPath(String string) {
        this.imagePath = string;
    }
}
