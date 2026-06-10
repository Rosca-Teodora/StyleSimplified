package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.ClothingItem;
import com.example.stylesimplified.backend.services.WardrobeService;

public class UpdateClothingCommand implements Command{
    private WardrobeService service;
    private ClothingItem item;

    public UpdateClothingCommand(WardrobeService service, ClothingItem item){
        this.item = item;
        this.service = service;
    }

    @Override
    public void execute() {
        service.updateClothingItem(item);
    }

    @Override
    public String getCommandText() {
        return "updated_clothing_item_" + item.getName();
    }
}
