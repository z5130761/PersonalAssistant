package learn.zhu.com.personalassistant.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import learn.zhu.com.personalassistant.R;

public class BackupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
    }

    public void backup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View loginView = View.inflate(this, R.layout.view_user_dialog, null);
    }
}
