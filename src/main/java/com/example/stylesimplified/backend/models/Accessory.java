package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "clothing-accesories")
public class Accessory extends ClothingItem{
    @DatabaseField
    private String placement;

    @DatabaseField
    private String material;

    @DatabaseField
    private String type;

    public Accessory() {
        super(null, null);
    }

    public Accessory(String name, String imgPath, String placement, String material, String type){
        super(name, imgPath);
        this.placement = placement;
        this.material = material;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){ // same mem address
            return true;
        }
        if (!(obj instanceof Accessory)){
            return false;
        }

        Accessory comp = (Accessory) obj;
        if (comp.material.equals(this.material) &&
            comp.type.equals(this.type) &&
            comp.name.equals(this.name)) { // ac material, tip de bijuterie si nume
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, placement, material, type);
    }
}
