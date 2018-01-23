package me.gujun.android.taggroup.demo;

import android.os.Parcel;
import android.os.Parcelable;

public class TagData implements Parcelable {


    public TagData(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
    }

    public TagData() {
    }

    protected TagData(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<TagData> CREATOR = new Parcelable.Creator<TagData>() {
        @Override
        public TagData createFromParcel(Parcel source) {
            return new TagData(source);
        }

        @Override
        public TagData[] newArray(int size) {
            return new TagData[size];
        }
    };
}
