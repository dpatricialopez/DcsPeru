package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dianalopez on 10/10/16.
 */

public class EntNoticia {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String  title;

    @SerializedName("contain")
    private String contain;

    @SerializedName("url")
    private String url;

    @SerializedName("date")
    private String date;

    @SerializedName("image")
    private String image;

    @SerializedName("file_name")
    private String file_name;

    @SerializedName("file_url")
    private String file_url;

    @SerializedName("status")
    private int status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;}

    public String getContain() {
        return contain ;
    }

    public void setContain(String contain) {
        this.contain = contain;

    }

    public String getFileName() {
        return file_name ;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;

    }

    public String getFile_url() {
        return file_url ;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;

    }
}





