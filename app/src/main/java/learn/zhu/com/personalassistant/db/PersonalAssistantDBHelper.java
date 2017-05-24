package learn.zhu.com.personalassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhu on 2017/4/19.
 */

public class PersonalAssistantDBHelper extends SQLiteOpenHelper {

    /**
     * 类型，支出或者收入
     * id       类型id
     * name     类型名字
     */
    private final String CREATE_TYPE = "create table type(" +
            "id integer primary key autoincrement," +
            "name text);";

    /**
     * 用于存储收入和支出的数据
     * id           收入或支出的id
     * category_id  类别的id
     * money        金钱
     * time         日期
     * remark       备注
     */
    private final String CREATE_DATA = "create table data(" +
            "id integer primary key autoincrement," +
            "category_id int," +
            "type_id int," +
            "money real," +
            "date date," +
            "remark text);";

    /**
     * 收入或支出的类别
     * id               类别id
     * name             类别名称
     * introductions    说明
     * type_id          类型id
     */
    private final String CREATE_CATEGORY = "create table category(" +
            "id integer primary key autoincrement," +
            "name text," +
            "introductions text," +
            "type_id integer);";

    public PersonalAssistantDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_TYPE);
        db.execSQL(CREATE_DATA);
        db.execSQL(CREATE_CATEGORY);

        //添加两条数据到类型中
        ContentValues sr = new ContentValues();
        sr.put("name", "收入");
        ContentValues zc = new ContentValues();
        zc.put("name", "支出");
        db.insert("type", null, sr);
        db.insert("type", null, zc);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
