package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "clothing_tops")
public class Top extends ClothingItem {
    @DatabaseField
    private String sleeveLength;

    @DatabaseField
    private String neckline;

    @DatabaseField
    private boolean isOuterwear; // if it's a jacket type or a shirt type (can it be worn over other clothes?)

    // needed default constructor for database handling
    public Top(){
        super(null, null);
    }

    public Top(String name, String imgPath, String sleeveLength, String neckline, boolean isOuterwear){
        super(name, imgPath);
        this.sleeveLength = sleeveLength;
        this.neckline = neckline;
        this.isOuterwear = isOuterwear;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){ // same mem address
            return true;
        }
        if (!(obj instanceof Top)){
            return false;
        }

        Top comp = (Top) obj;
        if (comp.neckline.equals(this.neckline) &&
                comp.isOuterwear == this.isOuterwear &&
                comp.name.equals(this.name)) { // ac neckline, tip de maneca si nume (nu conteaza atat de tare sleeve length pt ca ma gandesc ca cineva poate sa isi suflece manecile si transforma un tricou in short sleeve deci na)
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isOuterwear);
    }
}
