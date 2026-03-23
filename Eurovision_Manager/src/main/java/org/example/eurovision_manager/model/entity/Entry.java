package org.example.eurovision_manager.model.entity;

public class Entry {
    private int id;
    private String country;
    private String artist;
    private String title;
    private int releaseYear;

    public Entry() {}

    public Entry(String country, String artist, String title, int releaseYear) {
        this.country = country;
        this.artist = artist;
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public Entry(int id, String country, String artist, String title, int releaseYear) {
        this.id = id;
        this.country = country;
        this.artist = artist;
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
}