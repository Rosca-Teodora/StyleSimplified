package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tags")
public class Tag {
    @DatabaseField(generatedId = true)
    private int tagId;

    @DatabaseField
    protected String denumire;

    @DatabaseField
    protected String purpose;
}
