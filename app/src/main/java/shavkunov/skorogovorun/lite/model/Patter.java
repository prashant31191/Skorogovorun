package shavkunov.skorogovorun.lite.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Patter implements Parcelable {

    private String image;
    private String title;
    private String sounds;
    private boolean isFavorite;

    public Patter() {}

    public Patter(Parcel in) {
        image = in.readString();
        title = in.readString();
        sounds = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Patter> CREATOR = new Creator<Patter>() {
        @Override
        public Patter createFromParcel(Parcel in) {
            return new Patter(in);
        }

        @Override
        public Patter[] newArray(int size) {
            return new Patter[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(sounds);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
