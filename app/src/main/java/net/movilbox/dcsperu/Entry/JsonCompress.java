package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by germangarcia on 17/05/16.
 */


public class JsonCompress {

    @SerializedName("Content")
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}