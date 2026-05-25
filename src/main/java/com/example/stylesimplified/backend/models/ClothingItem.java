package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// any clothing item like shirts, pants, boots will inherit from this class
@DatabaseTable(tableName = "clothing_items")
public abstract class ClothingItem implements Comparable<ClothingItem> {
    @DatabaseField(generatedId = true)
    protected int itemId;

    @DatabaseField
    protected String imgPath;

    @DatabaseField
    protected String name;

    @DatabaseField
    protected String dbDateString;

    protected LocalDateTime dateUploaded;

    protected Set<Tag> tags = new HashSet<>();

    public String getName(){
        return name;
    }

    public ClothingItem() {
        // when loading from db
        if (this.dbDateString != null) {
            this.dateUploaded = LocalDateTime.parse(this.dbDateString); // converst String into LocalDateTime
        }
    }

    public ClothingItem(String name, String imgPath){
        this.name = name;
        this.imgPath = imgPath;
        this.dateUploaded = LocalDateTime.now();
        this.dbDateString = this.dateUploaded.toString();
    }

    public String getImgPath(){
        return this.imgPath;
    }

    public LocalDateTime getDateUploaded() {
        if (dateUploaded == null && dbDateString != null) { // if trying to gte date right after a load from db
            dateUploaded = LocalDateTime.parse(dbDateString);
        }
        return dateUploaded;
    }

    public int getRank(ClothingItem o) {
        if (o instanceof Top) return 1;
        if (o instanceof Bottom) return 2;
        if (o instanceof Accessory) return 3;
        return 4;
    }

    public int getItemId() {
        return itemId;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    @Override
    public int compareTo(ClothingItem o) {
        int thisRank = getRank(this);
        int otherRank = getRank(o);

        if (thisRank != otherRank) {
            return Integer.compare(thisRank, otherRank);
        }

        int dateCompare = o.dateUploaded.compareTo(this.dateUploaded);
        if (dateCompare != 0){
            return dateCompare;
        }

        int nameCompare = o.name.compareTo((this.name));
        if (nameCompare != 0) {
            return nameCompare;
        }

        // last chance compare to catch all the issues
        return Integer.compare(this.itemId, o.itemId);
    }
}
