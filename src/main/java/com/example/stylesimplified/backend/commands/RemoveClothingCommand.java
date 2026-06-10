package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.services.WardrobeService;

public class RemoveClothingCommand implements Command {
    private WardrobeService service;
    private ClothingItem item;

    public RemoveClothingCommand(WardrobeService service, ClothingItem item){
        this.item = item;
        this.service = service;
    }

    @Override
    public void execute() {
        service.removeClothingItem(item);
    }

    @Override
    public String getCommandText() {
        return "removed_clothing_item_" + item.getName();
    }
}
