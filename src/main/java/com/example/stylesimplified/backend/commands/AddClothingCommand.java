package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.models.Wardrobe;
import com.example.stylesimplified.backend.services.WardrobeService;

public class AddClothingCommand implements Command{
    private WardrobeService service;
    private ClothingItem item;

    public AddClothingCommand(WardrobeService service, ClothingItem item){
        this.item = item;
        this.service = service;
    }

    @Override
    public void execute() {
        service.addClothingItem(item);
    }

    @Override
    public String getCommandText() {
        return "added_clothing_item_" + item.getName();
    }
}
