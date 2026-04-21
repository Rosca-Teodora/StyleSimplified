package com.example.stylesimplified.backend.models;

public class Top extends ClothingItem {
    private String sleeveLength;
    private String neckline;
    private boolean isOuterwear; // if it's a jacket type or a shirt type (can it be worn over other clothes?)

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
    public int compareTo(Object obj){
        if (obj.equals(this)){
            return 0;
        }
        if (!(obj instanceof Top)){
            return 1; // orice bluza e mai "mare" decat celelalte haine
        }

        Top comp = (Top) obj;
        if (comp.dateUploaded.isAfter(this.dateUploaded)){
            return -1; // mentine descrescator prin a considera ca toate chestiile adaugate mai tarziu sunt mai mici
        }
        return 1;

    }
}
