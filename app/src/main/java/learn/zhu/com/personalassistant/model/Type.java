package learn.zhu.com.personalassistant.model;

/**
 * Created by zhu on 2017/4/19.
 */

public class Type {
    public final static int SR = 1;
    public final static int ZC = 2;

    public Type() {}
    public Type(int id, String name) {
        this.id = id;
        this.name = name;
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

    private int id;
    private String name;
}
