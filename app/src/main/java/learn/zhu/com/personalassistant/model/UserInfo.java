package learn.zhu.com.personalassistant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhu on 2017/5/2.
 */

public class UserInfo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPickname() {
        return pickname;
    }

    public void setPickname(String pickname) {
        this.pickname = pickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("_id")
    private int id;
    @SerializedName("_username")
    private String username;
    @SerializedName("_password")
    private String password;
    @SerializedName("_pickname")
    private String pickname;
    @SerializedName("_age")
    private int age;
    @SerializedName("_email")
    private String email;
    @SerializedName("_sex")
    private String sex;
    @SerializedName("_born")
    private String born;
    @SerializedName("_location")
    private String location;
}
