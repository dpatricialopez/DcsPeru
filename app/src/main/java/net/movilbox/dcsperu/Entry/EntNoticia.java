package net.movilbox.dcsperu.Entry;

/**
 * Created by dianalopez on 10/10/16.
 */

public class EntNoticia {
    private int id, estado;
    private String name, status, image, profilePic, timeStamp, url;


    public EntNoticia(int id, String name, String image, String status,
                      String profilePic, String timeStamp, String url, int estado){
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;
        this.estado=estado;

    }

    public EntNoticia() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;}

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;

    }
}





