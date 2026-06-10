package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.print.attribute.standard.OutputBin;

// tabela asociativa pt a face relatie many to many dintre
// outfit >----<  clothing item (+ type needed to be known ORMLite doesnt know how to convert)
@DatabaseTable(tableName = "outfits_clothing_items")
public class OutfitClothingLink {
    @DatabaseField(generatedId = true)
    private int outfitClothingLinkId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "outfit_id")
    private Outfit outfit;

    // nu pot sa pastrez un obiect de tip top, bottom sau accesory pt ca am nevoie de polimorfism
    // e posibil totusi sa tin minte item id-ul si clothing type-ul si dupa sa le manipulez eu cand se introduce o relatie noua
    @DatabaseField(columnName = "item_id")
    private int itemId;

    @DatabaseField(columnName = "clothing_type")
    private String clothingtype;

    public OutfitClothingLink() {}

    public OutfitClothingLink(Outfit outfit, int itemId, String clothingtype) {
        this.outfit = outfit;
        this.itemId = itemId;
        this.clothingtype = clothingtype;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOutfitClothingLinkId() {
        return outfitClothingLinkId;
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public String getClothingType() {
        return clothingtype;
    }
}
