package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "tags")
public class Tag {
    @DatabaseField(generatedId = true)
    private int tagId;

    @DatabaseField
    protected String nume;

    @DatabaseField
    protected String descriere;

    @DatabaseField
    private Boolean isFavourite;

    public Tag() {}

    public Tag(String nume) {
        this.nume = nume;
        this.descriere = "";
        this.isFavourite = false;
    }

    public Tag(String nume, String descriere) {
        this.nume = nume;
        this.descriere = descriere;
        this.isFavourite = false;
    }

    public String getNume() { return nume; }

    public void setNume(String nume) { this.nume = nume; }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getTagId() {
        return tagId;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite){
        this.isFavourite = favourite;
    }

    public String getName() {
        return this.nume;
    }

    @Override // override pt ca puteai sa adaugi aceleasi tag uri de mai multe orii :((((((
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag otherTag = (Tag) o;
        return this.tagId == otherTag.getTagId();
    }

    @Override
    public int hashCode() { // override hasCode pt ca am dat override la equals
        return Objects.hash(this.tagId);
    }
}
