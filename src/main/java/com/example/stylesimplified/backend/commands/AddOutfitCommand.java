package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Outfit;
import com.example.stylesimplified.backend.services.WardrobeService;
import java.util.List;

public class AddOutfitCommand implements Command {
    private final WardrobeService service;
    private final Outfit outfit;
    private final List<ClothingItem> clothes;

    public AddOutfitCommand(WardrobeService service, Outfit outfit, List<ClothingItem> clothes) {
        this.service = service;
        this.outfit = outfit;
        this.clothes = clothes;
    }

    @Override
    public void execute() {
        service.createOutfit(outfit, clothes);
    }

    @Override
    public void undo() {
        service.removeOutfit(outfit);
    }

    @Override
    public String getCommandText() {
        return "Added Outfit " + outfit.getName();
    }
}