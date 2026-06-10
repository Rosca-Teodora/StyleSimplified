package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.models.Tag;
import com.example.stylesimplified.backend.services.WardrobeService;

public class UpdateTagCommand implements Command {
    WardrobeService service;
    Tag tag;

    public UpdateTagCommand(WardrobeService service, Tag tag){
        this.tag = tag;
        this.service = service;
    }

    @Override
    public void execute() {
        service.updateTag(tag);
    }

    @Override
    public String getCommandText() {
        return "updated_tag_" + tag.getNume();
    }
}
