package com.example.android.model;

/**
 * Created by Ahmed Wafik Mohamed on 11/29/2017.
 */

public class Reviews {

    private int id;
    private String author;
    private String content;

    public static String sID = "id";
    public final static String sAUTHOR = "author";
    public final static String sCONTENT = "content";

    public Reviews(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public Reviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
