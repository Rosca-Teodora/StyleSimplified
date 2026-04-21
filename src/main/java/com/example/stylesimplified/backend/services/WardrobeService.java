package com.example.stylesimplified.backend.services;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Wardrobe;

// singleton "menu" class
// service that manipulates all the CRUD operations
public class WardrobeService {
    private static WardrobeService instance = null;
    private final Wardrobe wardrobe;

    private WardrobeService(){
        this.wardrobe = new Wardrobe();
    }

    public static WardrobeService getInstance() {
        if (instance == null){
            instance = new WardrobeService();
        }

        return instance;
    }

    public void addClothingItem(ClothingItem ci){
        wardrobe.getOwnedClothes().add(ci);
        System.out.println("Added clothing item");
    }

    public void removeClothingItem(ClothingItem ci){
        wardrobe.getOwnedClothes().remove(ci);
        System.out.println("Removed clothing item");
    }

    public Wardrobe getWardrobe() {
        return wardrobe;
    }
}
