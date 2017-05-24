package learn.zhu.com.personalassistant.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.model.PersonalAssistantDB;
import learn.zhu.com.personalassistant.model.UserInfo;
import learn.zhu.com.personalassistant.util.BackupUtil;
import learn.zhu.com.personalassistant.util.DialogUtil;
import learn.zhu.com.personalassistant.util.HttpUtil;
import learn.zhu.com.personalassistant.util.JsonUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhu on 2017/5/2.
 */

public class LoginDialogFragment  extends DialogFragment {
    private EditText mUsername;
    private EditText mPassword;
    private DialogInterface mDialogInterface;
    private PersonalAssistantDB mPersonalAssistantDB;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_user_dialog, null);
        mPersonalAssistantDB = PersonalAssistantDB.getInstance(getContext());
        mUsername = (EditText) view.findViewById(R.id.username);
        mPassword = (EditText) view.findViewById(R.id.password);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        String positiveButtonName =
                getArguments().getInt("TYPE") == BackupUtil.TYPE_BACKUP ? "Backup" : "Restore";
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(positiveButtonName,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                login();
                                mDialogInterface = dialog;
                                DialogUtil.setButtonExit(mDialogInterface, false);
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogUtil.setButtonExit(dialog, true);
                        dismiss();
                    }
                });

        if(getArguments().getInt("TYPE") == BackupUtil.TYPE_BACKUP) {
            builder.setNeutralButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment();
                    registerDialogFragment.show(getFragmentManager(), "registerDialog");
                }
            });
        }
        return builder.create();
    }

    private void login() {
        String userName = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        String url = HttpUtil.getUrl("userinfo/login");
        String param = JsonUtil.parseToJson(new String[]{"_username", "_password"},
                new String[]{userName, password});
        RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), param);
        //"{_username:'" + userName  + "',_password:'" + password + "'}");
        HttpUtil.sendOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String messageString = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(messageString.equals("null")) {
                            mUsername.requestFocus();
                            mUsername.setError("用户名或密码错误");
                            return;
                        }
                        try {
                            Gson gson = new Gson();
                            UserInfo userInfo = gson.fromJson(messageString, UserInfo.class);
                            BackupUtil.onLoginSuccess(getContext(), userInfo, mPersonalAssistantDB, getArguments().getInt("TYPE"));
                            DialogUtil.setButtonExit(mDialogInterface, true);
                        }catch (Exception e) {
                            Toast.makeText(getContext(), "出错", Toast.LENGTH_SHORT).show();
                        }

                        dismiss();
                    }
                });
            }
        });
    }

}
