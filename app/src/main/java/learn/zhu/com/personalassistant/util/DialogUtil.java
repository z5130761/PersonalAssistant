package learn.zhu.com.personalassistant.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by zhu on 2017/4/21.
 */

public class DialogUtil {
    public static void showNormalDialog(final Context context, String title, View view,
                            DialogInterface.OnClickListener onNegativeClickListener,
                            DialogInterface.OnClickListener onNeutralClickListener,
                                        boolean canclelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setView(view)
                .setNegativeButton("确定", onNegativeClickListener);
        if(!canclelable) {
            builder.setNeutralButton("退出", onNeutralClickListener);
        }
        builder.setCancelable(canclelable).show();
    }

    public static void showDatepicker(Context context, final EditText editText) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1; //月份是从0开始
                editText.setText(year + "-" + month + "-" +String.format(Locale.CHINA, "%02d", dayOfMonth));
            }
        }, year, month, day);
        dialog.show();
    }

    public static void setButtonExit(DialogInterface dialog, boolean isExit) {
        //利用反射控制对话框是否退出
        //Dialog类有个字段mShowing控制是否显示
        Field field;
        try {
            field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            // 设置mShowing值，欺骗android系统
            field.set(dialog, isExit);//如果为true则会推出
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
