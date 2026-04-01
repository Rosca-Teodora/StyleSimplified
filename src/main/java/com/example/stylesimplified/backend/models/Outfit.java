package com.example.stylesimplified.backend.models;

import java.time.LocalDate;
import java.util.*;

public class Outfit {
    static private Integer counter = 0;
    private LocalDate dateUploaded;
    protected String name;
    protected List<ClothingItem> items = new ArrayList<>();
    protected Set<Tag> outfitTags = new HashSet<>();

    public LocalDate getDateUploaded(){
        return this.dateUploaded;
    }
    public String getName() {
        return name;
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
