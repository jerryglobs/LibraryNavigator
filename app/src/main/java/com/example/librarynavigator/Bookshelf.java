package com.example.librarynavigator;

import java.util.ArrayList;

public class Bookshelf {
    private int bookshelf_num;
    private int dot_num;

    private ArrayList<String> Side0 = new ArrayList<>();
    private ArrayList<String> Side1 = new ArrayList<>();

    public int getDot_num() {
        return dot_num;
    }

    public void setDot_num(int dot_num) {
        this.dot_num = dot_num;
    }

    public ArrayList<String> getSide0() {
        return Side0;
    }

    public void setSide0(ArrayList<String> side0) {
        Side0 = side0;
    }

    public ArrayList<String> getSide1() {
        return Side1;
    }

    public void setSide1(ArrayList<String> side1) {
        Side1 = side1;
    }

    public int getBookshelf_num() {
        return bookshelf_num;
    }

    public void setBookshelf_num(int bookshelf_num) {
        this.bookshelf_num = bookshelf_num;
    }

}
