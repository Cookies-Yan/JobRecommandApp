package com.recommand.job.entity;

public class ExampleBook {
    public String title;
    public String author;
    public String date;
    public double price;
    public String currency;
    public int page;
    public String series;
    public String language;
    public String isbn;

    public ExampleBook(String title, String author, String date, double price, String currency, int page, String series, String language, String isbn) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.price = price;
        this.currency = currency;
        this.page = page;
        this.series = series;
        this.language = language;
        this.isbn = isbn;
    }
}
