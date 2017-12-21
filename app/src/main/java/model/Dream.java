package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vincent on 19/02/2016.
 */
public class Dream implements Parcelable {

    private static int lastId = 0;

    private long id;
    private String title;
    private String content;
    private long creationDate;

    public Dream(long id, String title, String content, long creationDate){
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
    }
    public Dream(String title, String content){
        this.id = -1;

        if(title.isEmpty()){
            if(content.length() > 25){
                this.title = content.substring(0, 25) + "...";
            }
            else {
                this.title = content.substring(0, content.length());
            }
        }
        else {
            this.title = title;
        }

        this.content = content;
        Calendar cal = Calendar.getInstance();
        this.creationDate = cal.getTime().getTime();
    }

    public Dream(String content){
        this("", content);
    }

    //GETTTERS AND SETTERS
    public long getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public Date getCreationDateFormatted(){
        return new Date(this.creationDate);
    }
    public long getCreationDate(){
        return this.creationDate;
    }

    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return this.id + " " + this.title + "\n" + this.content + " - " + sdf.format(new Date(this.creationDate));
    }

    /* PARCELABLE PART */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(creationDate);
    }

    protected Dream(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        creationDate = in.readLong();
    }

    public static final Creator<Dream> CREATOR = new Creator<Dream>() {
        @Override
        public Dream createFromParcel(Parcel in) {
            return new Dream(in);
        }

        @Override
        public Dream[] newArray(int size) {
            return new Dream[size];
        }
    };
}
