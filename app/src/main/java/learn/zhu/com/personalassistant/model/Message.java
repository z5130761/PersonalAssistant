package learn.zhu.com.personalassistant.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhu on 2017/5/2.
 */

public class Message {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    @SerializedName("Code")
    private int code;
    @SerializedName("MessageName")
    private String messageName;
}
