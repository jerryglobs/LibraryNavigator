package com.example.librarynavigator;

import java.util.ArrayList;

public class Dot {
    private int num;
    private ArrayList<Dot> side[] = new ArrayList[4];
    private ArrayList<Dot> t_dots = new ArrayList<>();
    private ArrayList<Dot> b_dots = new ArrayList<>();
    private ArrayList<Dot> l_dots = new ArrayList<>();
    private ArrayList<Dot> r_dots = new ArrayList<>();

    public Dot(int num) {
        this.num = num;
        side[0] = t_dots;
        side[1] = b_dots;
        side[2] = l_dots;
        side[3] = r_dots;
    }

    public ArrayList<Dot> getSideDots(int num) {
        return side[num];
    }

    public int getNum() {
        return num;
    }


}
