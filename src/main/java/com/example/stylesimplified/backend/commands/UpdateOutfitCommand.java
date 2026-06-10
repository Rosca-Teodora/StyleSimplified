package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Outfit;
import com.example.stylesimplified.backend.services.WardrobeService;

import java.util.List;

public class UpdateOutfitCommand implements Command{
    private final WardrobeService service;
    private final Outfit outfit;
    private final List<ClothingItem> updatedClothes;

    public UpdateOutfitCommand(WardrobeService service, Outfit outfit, List<ClothingItem> clothes) {
        this.service = service;
        this.outfit = outfit;
        this.updatedClothes = clothes;
    }

    @Override
    public void execute() {
        service.updateOutfitClothes(outfit, updatedClothes);
        service.removeOutfit(outfit);
    }

    @Override
    public String getCommandText() {
        return "removed_outfit_" + outfit.getName();
    }
}
