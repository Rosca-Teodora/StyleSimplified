package com.example.stylesimplified.backend.models;

import java.util.HashSet;
import java.util.Set;

// any clothing item like shirts, pants, boots will inherit from this class
public abstract class ClothingItem {
    static private Integer counter = 0;
    protected String ItemId;
    protected String imgPath;
    protected String name;
    protected Set<Tag> tags = new HashSet<>();
}
