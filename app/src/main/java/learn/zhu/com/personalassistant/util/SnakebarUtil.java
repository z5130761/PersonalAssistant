package learn.zhu.com.personalassistant.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import learn.zhu.com.personalassistant.R;

/**
 * Created by zhu on 2017/4/21.
 */

public class SnakebarUtil {
    public static void show(Activity activity, String msg, String actionMsg, View.OnClickListener onClickListener) {
        Snackbar.make(activity.findViewById(R.id.root), msg, Snackbar.LENGTH_LONG).setAction(actionMsg, onClickListener).show();
    }
}
