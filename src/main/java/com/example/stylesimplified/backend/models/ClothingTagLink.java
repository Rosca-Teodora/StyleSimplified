package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "clothing_items_tags")
public class ClothingTagLink {
    @DatabaseField(generatedId = true)
    private int clothingItemsTagsId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "tag_id")
    private Tag tag;

    @DatabaseField(columnName = "item_id")
    private int itemId;

    @DatabaseField(columnName = "clothing_type")
    private String clothingType;

    public ClothingTagLink() {}

    public ClothingTagLink(Tag tag, int itemId, String clothingType){
        this.itemId = itemId;
        this. tag = tag;
        this.clothingType = clothingType;
    }

    public int getClothingItemsTagsId() {
        return clothingItemsTagsId;
    }
    public int getItemId() {
        return itemId;
    }
    public Tag getTag() {
        return tag;
    }
    public String getClothingType() {
        return clothingType;
    }
}
