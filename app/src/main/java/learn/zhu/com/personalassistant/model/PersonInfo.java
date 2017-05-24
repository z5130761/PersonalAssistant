package learn.zhu.com.personalassistant.model;

/**
 * Created by zhu on 2017/4/20.
 */

public class PersonInfo {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public float getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(float surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String name;
    private String sex;
    private int age;
    private float totalMoney;
    private float surplusMoney;
    private String bornDate;
    private String password;
    private String email;
    private String location;
}
