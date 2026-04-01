package com.example.stylesimplified.backend.models;

public class Accessory extends ClothingItem{
    private String placement;
    private String material;
    private String type;

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
    public int compareTo(Object obj){
        if (obj.equals(this)){
            return 0;
        }
        if (!(obj instanceof Accessory)){
            return -1; // daca nu e accesoriu este mai "mare" obiectul obj
        }

        Accessory comp = (Accessory) obj;
        return this.name.compareTo(comp.getName()); // ordine lexicografica fiindca name e String
    }
}
