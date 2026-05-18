package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tags")
public class Tag {
    @DatabaseField(generatedId = true)
    private int tagId;

    @DatabaseField
    protected String nume;

    @DatabaseField
    protected String descriere;

    public Tag() {}

    public Tag(String nume) {
        this.nume = nume;
        this.descriere = "";
    }

    public Tag(String nume, String descriere) {
        this.nume = nume;
        this.descriere = descriere;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
