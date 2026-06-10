package com.example.stylesimplified.backend.utils;

import com.example.stylesimplified.backend.models.ClothingItem;

// clasa facuta ca sa pot trimite fals sau true in perspectiva edit mode ului (pt ca poti apasa butonul de edit sau pe clothing item si te duce in acelasi controller dar cu context diferit)
public class ClothingNavigationContext {
    private final ClothingItem item;
    private final boolean startInEditMode;

    public ClothingNavigationContext(ClothingItem item, boolean startInEditMode) {
        this.item = item;
        this.startInEditMode = startInEditMode;
    }

    public ClothingItem getItem() { return item; }
    public boolean isStartInEditMode() { return startInEditMode; }
}