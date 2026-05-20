package com.example.stylesimplified.backend.models;

import java.util.*;

public class Wardrobe {

    // vreau sa afisez hainele intr-o ordine custom (bluze, pantaloni, accesorii si pt fiecare subclasa alfabetic dupa denumire deci implementez TreeSet)
    private Set<ClothingItem> ownedClothes = new TreeSet<>(new Comparator<ClothingItem>() {
        @Override
        public int compare(ClothingItem o1, ClothingItem o2) {
            return o1.compareTo(o2); // clasa abstracta ClothingItem implementeaza Comparable
        };
    });

    private Set<Outfit> allOutfits = new TreeSet<>(new Comparator<Outfit>() {
        @Override
        public int compare(Outfit o1, Outfit o2) {
            if (o1.equals(o2)){
                return 0;
            }

            // momentan cred sortate crescatoare in fct de data
            if (o1.getDateUploaded().compareTo(o2.getDateUploaded()) == 0){
                // sunt adaugate in aceeasi zi
                return o1.getName().compareTo(o2.getName()); // sortare alfabetica
            }
            else {
                // nu sunt adaugate in aceeasi zi deci metoda compareTo predefinita pt LocalDate merge
                return o1.getDateUploaded().compareTo(o2.getDateUploaded());
            }
        }
    });

    private Set<Group> groups = new TreeSet<>(new Comparator<Group>() {
        @Override
        public int compare(Group g1, Group g2) { // fix acelasi cod ca la grup for now
            if (g1.equals(g2)){
                return 0;
            }

            // momentan cred sortate crescatoare in fct de data
            if (g1.getDateCreated().compareTo(g2.getDateCreated()) == 0){
                // sunt adaugate in aceeasi zi
                return g1.getName().compareTo(g2.getName()); // sortare alfabetica
            }
            else {
                // nu sunt adaugate in aceeasi zi deci metoda compareTo predefinita pt LocalDate merge
                return g1.getDateCreated().compareTo(g2.getDateCreated());
            }
        }
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
