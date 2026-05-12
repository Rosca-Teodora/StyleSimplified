package com.example.stylesimplified.backend.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "clothing-bottoms")
public class Bottom extends ClothingItem {

    @DatabaseField(generatedId = true)
    private int bottomId;

    @DatabaseField
    private String fitType; // skinny, baggy etc

    @DatabaseField
    private String waistRise; // low-rise, high-rise etc

    @DatabaseField
    private String length; // shorts, long pants, mid-lenghth

    public Bottom(){
        super(null, null);
    }

    public Bottom(String name, String imgPath, String fitType, String waistRise, String length){
        super(name, imgPath);
        this.fitType = fitType;
        this.waistRise = waistRise;
        this.length = length;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (!(obj instanceof Bottom)){
            return false;
        }

        Bottom comp = (Bottom) obj;
        if (comp.fitType.equals(this.fitType) &&
                comp.length.equals(this.length) &&
                comp.name.equals(this.name)) { // ac mod de a fi purtat, lungime de bijuterie si nume
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Object obj){
        if (obj.equals(this)){
            return 0;
        }
        if (obj instanceof Accessory){
            return 1; // mai mare decat accesoriile
        }
        if (obj instanceof Top){
            return -1; // mai mic decat bluzele
        }

        Bottom comp = (Bottom) obj;
        if (comp.dateUploaded.isAfter(this.dateUploaded)){
            return -1; // mentine descrescator prin a considera ca toate chestiile adaugate mai tarziu sunt mai mici
        }
        return 1;

    }
}
