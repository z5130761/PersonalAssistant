package learn.zhu.com.personalassistant.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
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
import learn.zhu.com.personalassistant.model.Message;
import learn.zhu.com.personalassistant.util.DialogUtil;
import learn.zhu.com.personalassistant.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhu on 2017/5/2.
 */

public class RegisterDialogFragment extends DialogFragment {
    private EditText mUsername;
    private EditText mPassword;
    private DialogInterface mDialogInterface;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_user_dialog, null);
        mUsername = (EditText) view.findViewById(R.id.username);
        mPassword = (EditText) view.findViewById(R.id.password);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Sign up",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                register();
                                mDialogInterface = dialog;
                                DialogUtil.setButtonExit(mDialogInterface, false);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DialogUtil.setButtonExit(dialog, true);
                                dismiss();
                            }
                        });
        return builder.create();
    }

    private void register() {
        String userName = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String url = HttpUtil.getUrl("userinfo/register");
        RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), "{_username:'" + userName  +
                "',_password:'" + password + "'}");
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
                String messageString = response.body().string();
                Gson gson = new Gson();
                final Message message = gson.fromJson(messageString, Message.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(message.getCode() < 0) {
                            mUsername.requestFocus();
                            mUsername.setError(message.getMessageName());
                        }
                        else {
                            Toast.makeText(getActivity(), message.getMessageName(), Toast.LENGTH_SHORT).show();
                            DialogUtil.setButtonExit(mDialogInterface, true);
                            dismiss();
                        }
                    }
                });
            }
        });
    }
}
