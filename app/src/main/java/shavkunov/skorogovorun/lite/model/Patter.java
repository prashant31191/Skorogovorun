package shavkunov.skorogovorun.lite.model;

public class Patter {

    private String image;
    private String title;
    private String sounds;
    private boolean isFavorite;

    public Patter() {}

    public Patter(String image, String title, String sounds, boolean isFavorite) {
        this.image = image;
        this.title = title;
        this.sounds = sounds;
        this.isFavorite = isFavorite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSounds() {
        return sounds;
    }

    public void setSounds(String sounds) {
        this.sounds = sounds;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
