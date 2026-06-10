package com.example.stylesimplified.backend.utils;

import com.example.stylesimplified.backend.models.Outfit;

// aceeasi chestie ca la clothing items
public class OutfitNavigationContext {
    private final Outfit outfit;
    private final boolean startInEditMode;

    public OutfitNavigationContext(Outfit outfit, boolean startInEditMode) {
        this.outfit = outfit;
        this.startInEditMode = startInEditMode;
    }

    public Outfit getOutfit() { return outfit; }
    public boolean isStartInEditMode() { return startInEditMode; }
}