package com.example.stylesimplified.backend.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// any clothing item like shirts, pants, boots will inherit from this class
public abstract class ClothingItem implements Comparable {
    static private Integer counter = 0;
    protected String itemId;
    protected String imgPath;
    protected String name;
    protected LocalDate dateUploaded;
    protected Set<Tag> tags = new HashSet<>();

    public String getName(){
        return name;
    }

    public ClothingItem(String name, String imgPath){
        this.name = name;
        this.imgPath = imgPath;
    }

    public String getImgPath(){
        return this.imgPath;
    }
}
