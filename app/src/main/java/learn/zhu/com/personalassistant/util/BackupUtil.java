package learn.zhu.com.personalassistant.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.Category;
import learn.zhu.com.personalassistant.model.Data;
import learn.zhu.com.personalassistant.model.Message;
import learn.zhu.com.personalassistant.model.PersonalAssistantDB;
import learn.zhu.com.personalassistant.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhu on 2017/5/2.
 */

public class BackupUtil {
    public interface OnBackup {
        void OnSuccess();
        void OnFail();
    }
    public static final int TYPE_BACKUP = 0;
    public static final int TYPE_RESTORE = 1;
    //备份
    private static void backupData(UserInfo userInfo, List<Data> list, final OnBackup onBackup) {
        String url = HttpUtil.getUrl("data/adddata");
        for (Data item:
                list) {
            item.setUserId(userInfo.getId());
        }
        String param = new Gson().toJson(list);
        RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), param);
        HttpUtil.sendOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onBackup.OnFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String messageString = response.body().string();
                Message msg = new Gson().fromJson(messageString, Message.class);
                if(msg.getCode() > 0) {
                    onBackup.OnSuccess();
                }
                else
                    onBackup.OnFail();
            }
        });
    }
    private static void backupUserInfo(final UserInfo userInfo, Context context, final OnBackup onBackup) {
        String url = HttpUtil.getUrl("userinfo/update");
        userInfo.setAge(Integer.valueOf(Utility.getString(context, context.getString(R.string.age))));
        userInfo.setBorn(Utility.getString(context, context.getString(R.string.born_date)));
        userInfo.setEmail(Utility.getString(context, context.getString(R.string.email)));
        userInfo.setLocation(Utility.getString(context, context.getString(R.string.location)));
        userInfo.setSex(Utility.getString(context, context.getString(R.string.sex)));

        String param = new Gson().toJson(userInfo);
        RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), param);
        HttpUtil.sendOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onBackup.OnFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String messageString = response.body().string();
                Message msg = new Gson().fromJson(messageString, Message.class);
                if(msg.getCode() > 0) {
                    onBackup.OnSuccess();
                }
                else
                    onBackup.OnFail();
            }
        });
    }
    private static void backupCategory(final UserInfo userInfo, List<Category> list, final OnBackup onBackup) {
        String url = HttpUtil.getUrl("category/addcategory");
        for (Category item:
                list) {
            item.setUserId(userInfo.getId());
        }
        String param = new Gson().toJson(list);
        RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), param);
        HttpUtil.sendOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onBackup.OnFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String messageString = response.body().string();
                Message msg = new Gson().fromJson(messageString, Message.class);
                if(msg.getCode() > 0) {
                    onBackup.OnSuccess();
                }
                else
                    onBackup.OnFail();
            }
        });
    }

    //还原
    private static void restoreUserInfo(Context context, UserInfo userInfo, PersonalAssistantDB mPersonalAssistantD) {
        //还原用户信息
        Utility.setString(context, context.getString(R.string.sex), userInfo.getSex());
        Utility.setString(context, context.getString(R.string.age), String.valueOf(userInfo.getAge()));
        Utility.setString(context, context.getString(R.string.location), userInfo.getLocation());
        Utility.setString(context, context.getString(R.string.email), userInfo.getEmail());
        Utility.setString(context, context.getString(R.string.born_date), userInfo.getBorn());
        Utility.setString(context, context.getString(R.string.user_name), userInfo.getUsername());
        //还原分类
        restoreCategory(userInfo, context, mPersonalAssistantD);
        //还原数据
    }
    private static void restoreCategory(final UserInfo userInfo, final Context context, final PersonalAssistantDB mPersonalAssistantDB) {
        String url = HttpUtil.getUrl("category/getcategory?id=" + userInfo.getId());
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FailToast(context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String messageString = response.body().string();

                List<Category> list = new Gson().fromJson(messageString, new TypeToken<List<Category>>() {
                }.getType());
                mPersonalAssistantDB.deleteAllCategory();
                for (Category category:
                        list) {
                    mPersonalAssistantDB.addCategory(category);
                }
                restoreData(userInfo, context, mPersonalAssistantDB);

            }
        });
    }
    private static void restoreData(UserInfo userInfo, final Context context, final PersonalAssistantDB mPersonalAssistantDB) {
        String url = HttpUtil.getUrl("data/getdata?id=" + userInfo.getId());
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FailToast(context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String messageString = response.body().string();

                List<Data> list = new Gson().fromJson(messageString, new TypeToken<List<Data>>() {
                }.getType());
                mPersonalAssistantDB.deleteAllData();
                for (Data data:
                        list) {
                    String date = data.getDate().split(" ")[0].replaceAll("/", "-");
                    data.setDate(date);
                    mPersonalAssistantDB.addData(data);
                }
                SuccessToast(context);
            }
        });
    }

    //登录成功
    public static void onLoginSuccess(final Context context, final UserInfo userInfo, final PersonalAssistantDB mPersonalAssistantDB, int type) {

        if(type == TYPE_BACKUP)
            backupUserInfo(userInfo, context, new BackupUtil.OnBackup() {
            @Override
            public void OnSuccess() {
                //备份用户信息成功，备份分类
                List<Category> list = mPersonalAssistantDB.getAllCategories();
                BackupUtil.backupCategory(userInfo, list, new BackupUtil.OnBackup() {
                    @Override
                    public void OnSuccess() {
                        //备份分类成功，备份数据
                        List<Data> list = mPersonalAssistantDB.getDatas(0, 0, null, null, -1, -1, null);
                        BackupUtil.backupData(userInfo, list, new BackupUtil.OnBackup() {
                            @Override
                            public void OnSuccess() {
                                //备份数据成功
                                SuccessToast(context);
                            }

                            @Override
                            public void OnFail() {
                                FailToast(context);
                            }
                        });
                    }

                    @Override
                    public void OnFail() {
                        FailToast(context);
                    }
                });
            }

            @Override
            public void OnFail() {
                FailToast(context);
            }
        });
        else if(type == TYPE_RESTORE)
            restoreUserInfo(context, userInfo, mPersonalAssistantDB);
    }

    private static void SuccessToast(final Context context) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void FailToast(final Context context) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
