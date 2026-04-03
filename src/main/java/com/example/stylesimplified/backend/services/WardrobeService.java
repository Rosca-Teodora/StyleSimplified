package com.example.stylesimplified.backend.services;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Wardrobe;

// singleton "menu" class
// service that manipulates all the CRUD operations
public class WardrobeService {
    WardrobeService instance = null;
    Wardrobe wardrobe;

    private WardrobeService(){
        this.wardrobe = new Wardrobe();
    }

    public WardrobeService getInstance() {
        if (this.instance == null){
            this.instance = new WardrobeService();
        }

        return instance;
    }

    public void addClothingItem(ClothingItem ci){
        wardrobe.getOwnedClothes().add(ci);
        System.out.println("Added clothing item");
    }

    public void removeClothingItem(ClothingItem ci){
        wardrobe.getOwnedClothes().remove(ci);
    }
}
