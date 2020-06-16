package com.example.librarynavigator;

import android.graphics.Bitmap;

public class Book {
    private String bookName;
    private boolean isBorrowed;
    private String author;
    private String cid;
    private Bitmap imgurl;
    private String booksign;
    public String getBooksign() {
        return booksign;
    }

    public void setBooksign(String booksign) {
        this.booksign = booksign;
    }

    public Bitmap getImgurl() {
        return imgurl;
    }

    public void setImgurl(Bitmap imgurl) {
        this.imgurl = imgurl;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
