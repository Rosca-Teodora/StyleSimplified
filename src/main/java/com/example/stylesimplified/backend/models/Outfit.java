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

    protected List<ClothingItem> items = new ArrayList<>();

    protected Set<Tag> outfitTags = new HashSet<>();

    private LocalDate dateUploaded;

    public LocalDate getDateUploaded(){
        return this.dateUploaded;
    }
    public String getName() {
        return name;
    }

    public Outfit() {}

    public Outfit(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Outfit outfit = (Outfit) o;
        return Objects.equals(items, outfit.items) && Objects.equals(outfitTags, outfit.outfitTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, outfitTags);
    }
}
