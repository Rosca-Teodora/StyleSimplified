package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.Tag;
import com.example.stylesimplified.backend.services.WardrobeService;

public class RemoveTagCommand implements Command{
    WardrobeService service;
    Tag tag;

    public RemoveTagCommand(WardrobeService service, Tag tag){
        this.tag = tag;
        this.service = service;
    }

    @Override
    public void execute() {
        service.removeTag(tag);
    }

    @Override
    public String getCommandText() {
        return "removed_tag_" + tag.getNume();
    }
}
