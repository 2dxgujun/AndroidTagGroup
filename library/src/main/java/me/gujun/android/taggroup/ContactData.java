package me.gujun.android.taggroup;

/**
 * Created by rkgarg on 27/07/15.
 */
public class ContactData {

    private String name;

    private String emailID;

    private String imageUri;

    private boolean IsChecked = false;

    public ContactData(String name, String emailID, String imageUri) {
        this.name = name;
        this.emailID = emailID;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setIsChecked(boolean isChecked) {
        IsChecked = isChecked;
    }
}
