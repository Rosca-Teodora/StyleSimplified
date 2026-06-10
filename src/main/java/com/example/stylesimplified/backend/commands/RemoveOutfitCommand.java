package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.Outfit;
import com.example.stylesimplified.backend.services.WardrobeService;

public class RemoveOutfitCommand implements Command{
    private WardrobeService service;
    private Outfit outfit;

    public RemoveOutfitCommand(WardrobeService instance, Outfit currentOutfit) {
        this.service = instance;
        this.outfit = currentOutfit;
    }

    @Override
    public void execute() {
        service.removeOutfit(outfit);
    }

    @Override
    public String getCommandText() {
        return "removed_outfit_" + outfit.getName();
    }
}
