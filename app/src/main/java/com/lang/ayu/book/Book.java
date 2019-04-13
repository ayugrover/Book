package com.lang.ayu.book;

public class Book {
   private String mName;
   private String mAuthor;
   private String mUrl;
   private String infoUrl;

    public Book(String bname ,String bauthor,String burl,String info) {
       mAuthor = bauthor;
        mName = bname;
        mUrl = burl;
        infoUrl = info;
    }

    public String getName(){return mName;}
    public String getmAuthor(){return mAuthor;}
    public String geturl(){return mUrl;}
    public String getInfo(){return infoUrl;}
}
