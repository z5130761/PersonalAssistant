package learn.zhu.com.personalassistant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhu on 2017/4/19.
 */

public class Data {
    public Data() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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
    @SerializedName("_category_id")
    private int categoryId;
    @SerializedName("_type_id")
    private int typeId;
    @SerializedName("_money")
    private float money;
    @SerializedName("_date")
    private String date;
    @SerializedName("_remark")
    private String remark;
}
