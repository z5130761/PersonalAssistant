package learn.zhu.com.personalassistant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhu on 2017/4/19.
 */

public class Category {
    public Category(){}
    public Category(String name, String introductions, int typeId) {
        this.name = name;
        this.introductions = introductions;
        this.typeId = typeId;
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

    public String getIntroductions() {
        return introductions;
    }

    public void setIntroductions(String introductions) {
        this.introductions = introductions;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }


    @Override
    public String toString() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    @SerializedName("_id")
    private int id;
    @SerializedName("_user_id")
    private int userId;
    @SerializedName("_name")
    private String name;
    @SerializedName("_introductions")
    private String introductions;
    @SerializedName("_type_id")
    private int typeId;
}
