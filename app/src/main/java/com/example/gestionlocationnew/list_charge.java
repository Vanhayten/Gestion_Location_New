package com.example.gestionlocationnew;

public class list_charge {
    private String color;
    private String matr;
    private  String marque;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMatr() {
        return matr;
    }

    public void setMatr(String matr) {
        this.matr = matr;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }


   /* public list_vihcule(String matr, String marque) {
        this.matr = matr;
        this.marque = marque;
    }*/

    public list_charge( String matr, String marque,String color) {
        this.color = color;
        this.matr = matr;
        this.marque = marque;
    }
}
