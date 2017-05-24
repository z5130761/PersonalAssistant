package learn.zhu.com.personalassistant.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import learn.zhu.com.personalassistant.db.PersonalAssistantDBHelper;

/**
 * Created by zhu on 2017/4/19.
 */

public class PersonalAssistantDB {


    private final String DB_NAME = "personal_assistant";
    private final int VERSION = 1;

    private static PersonalAssistantDB sPersonalAssistantDB;
    private SQLiteDatabase db;

    private PersonalAssistantDB(Context context) {
        PersonalAssistantDBHelper dbHelper = new PersonalAssistantDBHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static PersonalAssistantDB getInstance(Context context) {
        if(sPersonalAssistantDB == null)
            sPersonalAssistantDB = new PersonalAssistantDB(context);
        return sPersonalAssistantDB;
    }

    ////////////////////////////
    ////////////////////////////
    ////////////////////////////

    /**
     * 添加Data
     * @param data
     * @return
     */
    public long addData(Data data) {
        if(data != null) {
            ContentValues values = new ContentValues();
            values.put("category_id", data.getCategoryId());
            values.put("type_id", data.getTypeId());
            values.put("money", data.getMoney());
            values.put("date", data.getDate());
            values.put("remark", data.getRemark());
            return db.insert("data", null, values);
        }
        return -1;
    }

    /**
     * 详细查询Data
     * @param categoryId 分类id
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @param minMoney 最小金额
     * @param maxMoney 最大金额
     * @return
     */
    public List<Data> getDatas(int categoryId, int typeId, String beginDate, String endDate,
                              float minMoney, float maxMoney, String orderBy) {
        List<Data> datas = new ArrayList<>();
        //拼接参数
        StringBuilder stringBuilder = new StringBuilder();
        List<String> selectionList = new ArrayList<>();
        if(categoryId != 0) {
            stringBuilder.append("category_id=?");
            selectionList.add(String.valueOf(categoryId));
        } else {
            stringBuilder.append("1=1");
        }

        if(typeId != 0) {
            stringBuilder.append(" and type_id=?");
            selectionList.add(String.valueOf(typeId));
        }


        if(beginDate != null) {
            stringBuilder.append(" and date >= ?");
            selectionList.add(beginDate);
        }

        if(endDate != null) {
            stringBuilder.append(" and date <= ?");
            selectionList.add(endDate);
        }

        if(minMoney >= 0) {
            stringBuilder.append(" and money >= ?");
            selectionList.add(String.valueOf(minMoney));
        }

        if(maxMoney >= 0) {
            stringBuilder.append(" and money <= ?");
            selectionList.add(String.valueOf(maxMoney));
        }

        String[] selectionArgs = null;
        if(selectionList.size() != 0) {
            selectionArgs = new String[selectionList.size()];
            selectionList.toArray(selectionArgs);
        }

        //查询
        Cursor cursor = db.query("data", null, stringBuilder.toString(), selectionArgs, null, null, orderBy);


        if(cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
                data.setTypeId(cursor.getInt(cursor.getColumnIndex("type_id")));
                data.setDate(cursor.getString(cursor.getColumnIndex("date")));
                data.setMoney(cursor.getFloat(cursor.getColumnIndex("money")));
                data.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                datas.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return datas;
    }

    /**
     * 通过id删除Data
     * @param id
     */
    public void deleteData(int id) {
        db.delete("data", "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteAllData() {
        db.delete("data", null, null);
    }

    ////////////////////////////
    ////////////////////////////
    ////////////////////////////

    /**
     * 添加Category
     * @param category
     * @return
     */
    public long addCategory(Category category) {
        if(category != null) {
            ContentValues values = new ContentValues();
            if(category.getId() != 0) {
                values.put("id", category.getId());
            }
            values.put("name", category.getName());
            values.put("introductions", category.getIntroductions());
            values.put("type_id", category.getTypeId());
            return db.insert("category", null, values);
        }
        return -1;
    }

    /**
     * 获取所有Category
     * @return
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = db.query("category", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex("id")));
                category.setName(cursor.getString(cursor.getColumnIndex("name")));
                category.setIntroductions(cursor.getString(cursor.getColumnIndex("introductions")));
                category.setTypeId(cursor.getInt(cursor.getColumnIndex("type_id")));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public void deleteAllCategory() {
        db.delete("category", null, null);
    }


    /**
     * 通过id获取Category
     * @param id
     * @return
     */
    public Category getCategory(int id) {
        Category category = null;
        String selection = "id=?";
        String[] selectionArgs = new String[] { String.valueOf(id) };

        Cursor cursor = db.query("category", null, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()) {
            category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setName(cursor.getString(cursor.getColumnIndex("name")));
            category.setIntroductions(cursor.getString(cursor.getColumnIndex("introductions")));
            category.setTypeId(cursor.getInt(cursor.getColumnIndex("type_id")));
        }
        cursor.close();
        return category;
    }

    /**
     * 通过类型获取Category集合
     * @param typeId
     * @return
     */
    public List<Category> getCategories(int typeId) {
        List<Category> categories = new ArrayList<>();
        String selection = "type_id=?";
        String[] selectionArgs = new String[] { String.valueOf(typeId) };

        Cursor cursor = db.query("category", null, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex("id")));
                category.setName(cursor.getString(cursor.getColumnIndex("name")));
                category.setIntroductions(cursor.getString(cursor.getColumnIndex("introductions")));
                category.setTypeId(cursor.getInt(cursor.getColumnIndex("type_id")));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    /**
     * 通过id删除Category
     * @param id
     */
    public int deleteCategory(int id) {
        return db.delete("category", "id=?", new String[]{String.valueOf(id)});
    }

    /**
     * 获取类型
     * @param name
     * @return
     */
    public Type getType(String name) {
        Type type = null;
        String selection = "name=?";
        String[] selectionArgs = new String[] { name };
        Cursor cursor = db.query("type", null, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()) {
            type = new Type();
            type.setId(cursor.getInt(cursor.getColumnIndex("id")));
            type.setName(cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        return type;
    }

    /**
     * 关闭
     */
    public void close() {
        db.close();
        sPersonalAssistantDB = null;
    }
}
