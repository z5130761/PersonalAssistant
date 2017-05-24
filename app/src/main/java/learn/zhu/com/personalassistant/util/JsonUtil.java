package learn.zhu.com.personalassistant.util;

import com.google.gson.Gson;

import java.util.List;

import learn.zhu.com.personalassistant.model.Message;

/**
 * Created by zhu on 2017/5/2.
 */

public class JsonUtil {
    public static String parseToJson(String[] field, String[] value) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for(int i = 0; i < field.length; i++) {
            builder.append(field[i]);
            builder.append(":'");
            builder.append(value[i]);
            builder.append("',");
        }

        String json = builder.toString().substring(0, builder.length()-1);
        return json + "}";
    }
}
