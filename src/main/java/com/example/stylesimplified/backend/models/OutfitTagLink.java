package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "outfits_tags")
public class OutfitTagLink {
    @DatabaseField(generatedId = true)
    private int outfitsTagsId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "outfit_id")
    private Outfit outfit;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "tag_id")
    private Tag tag;

    public OutfitTagLink() {}

    public OutfitTagLink(Outfit outfit, Tag tag){
        this.outfit = outfit;
        this.tag = tag;
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public Tag getTag() {
        return tag;
    }

    public int getOutfitsTagsId() {
        return outfitsTagsId;
    }
}
