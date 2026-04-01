package com.example.stylesimplified.backend.services;

public class WardrobeService {
    WardrobeService instance;

    private WardrobeService(){
        this.instance = null;
    }

    public WardrobeService getInstance() {
        if (this.instance == null){
            WardrobeService service = new WardrobeService();
            this.instance = service;
        }
        return instance;
    }
}
