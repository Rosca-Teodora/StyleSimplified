package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.Tag;
import com.example.stylesimplified.backend.services.WardrobeService;

public class AddTagCommand implements Command{
    WardrobeService service;
    Tag tag;

    public AddTagCommand(WardrobeService service, Tag tag){
        this.tag = tag;
        this.service = service;
    }

    @Override
    public void execute() {
        service.addTag(tag);
    }

    @Override
    public void undo() {
        service.removeTag(tag);
    }

    @Override
    public String getCommandText() {
        return "Added Tag " + tag.getNume();
    }
}
