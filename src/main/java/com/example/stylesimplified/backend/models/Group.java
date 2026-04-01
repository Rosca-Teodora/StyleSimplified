package com.example.stylesimplified.backend.models;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TreeSet;

public class Group{
    static private Integer counter = 0;
    protected String groupId;
    protected String groupName;
    protected LocalDate dateCreated;
    protected Set<Outfit> outfits= new TreeSet<>(new Comparator<Outfit>() {
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
    public LocalDate getDateCreated(){
        return dateCreated;
    }
    public String getGroupId() {
        return groupId;
    }
    public String getName() {
        return groupName;
    }

    // override la equals pentru ca doua grupuri ar trebui sa poata avea aceleasi outfit-uri dar nu acelasi nume si group id
    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (!(obj instanceof Group)){
            return false;
        }

        Group comp = (Group) obj;
        if (comp.getGroupId() == this.groupId){
            return true;
        }
        return false;
    }
}
