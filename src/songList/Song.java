// Junfeng Wang NetID: jw139, Joey Zheng NetID: jz813
package songList;

import javafx.beans.property.SimpleStringProperty;

public class Song {
    private SimpleStringProperty SongTitle, Artist, Album;
    private int Year = -1;

    public Song(String SongTitle, String Artist, String Album, int Year) {
        this.SongTitle = new SimpleStringProperty(SongTitle);
        this.Artist = new SimpleStringProperty(Artist);
        this.Album = new SimpleStringProperty(Album);
        this.Year = Year;
    }

    public Song(String SongTitle, String Artist) {
        this.SongTitle = new SimpleStringProperty(SongTitle);
        this.Artist = new SimpleStringProperty(Artist);

    }

    public Song(String SongTitle, String Artist, String Album) {
        this.SongTitle = new SimpleStringProperty(SongTitle);
        this.Artist = new SimpleStringProperty(Artist);
        this.Album = new SimpleStringProperty(Album);
    }

    public Song(String SongTitle, String Artist, int Year) {
        this.SongTitle = new SimpleStringProperty(SongTitle);
        this.Artist = new SimpleStringProperty(Artist);
        this.Year = Year;
    }

    public String getSongTitle() {
        return SongTitle.get();
    }

    public void setSongTitle(SimpleStringProperty songTitle) {
        SongTitle = songTitle;
    }

    public String getArtist() {
        return Artist.get();
    }

    public void setArtist(SimpleStringProperty artist) {
        Artist = artist;
    }

    public String getAlbum() {
        return Album.get();
    }

    public void setAlbum(SimpleStringProperty album) {
        Album = album;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String toString() {
        return this.SongTitle.get() + "    " + this.Artist.get();
    }
}