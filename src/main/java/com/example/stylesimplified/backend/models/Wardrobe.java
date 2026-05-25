package com.example.stylesimplified.backend.models;

import java.time.LocalDate;
import java.util.*;

public class Wardrobe {

    // vreau sa afisez hainele intr-o ordine custom (bluze, pantaloni, accesorii si pt fiecare subclasa alfabetic dupa denumire deci implementez TreeSet)
    private Set<ClothingItem> ownedClothes = new TreeSet<>();

    private Set<Outfit> allOutfits = new TreeSet<>((o1, o2) -> {
        if (o1.getId() == o2.getId()) return 0;

        LocalDate d1 = o1.getDateUploaded();
        LocalDate d2 = o2.getDateUploaded();
        if (d1 != null && d2 != null && !d1.equals(d2)) { // newer dates go first
            return d1.compareTo(d2);
        }
        int nameCompare = o1.getName().compareToIgnoreCase(o2.getName()); // check name after dates
        if (nameCompare != 0) {
            return nameCompare;
        }
        return Integer.compare(o1.getId(), o2.getId()); // have to compare ids bc otherwise treeset deletes them (...nu mai zic nimic...)
    });

    private Set<Group> groups = new TreeSet<>((g1, g2) -> {
        if (g1.getGroupId() == g2.getGroupId()) return 0;

        LocalDate d1 = g1.getDateCreated();
        LocalDate d2 = g2.getDateCreated();

        if (d1 != null && d2 != null && !d1.equals(d2)) {
            return d1.compareTo(d2);
        }

        int nameCompare = g1.getName().compareToIgnoreCase(g2.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }

        return Integer.compare(g1.getGroupId(), g2.getGroupId());
    });

    private ArrayList<Tag> tags = new ArrayList<>();

    public Set<ClothingItem> getOwnedClothes() {
        return ownedClothes;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public Set<Outfit> getAllOutfits() {
        return allOutfits;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }
}
