package learn.zhu.com.personalassistant.preference;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.DialogPreference;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import learn.zhu.com.personalassistant.R;
import learn.zhu.com.personalassistant.activity.PersonInfoActivity;

/**
 * Created by zhu on 2017/5/2.
 */

public class AvatarPreference extends DialogPreference {
    private Context mContext;
    private Button mChoose;
    private ImageView mImage;
    private String picturePath;

    public AvatarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.avatar_choose_dialog);
        setPositiveButtonText("确定");
        setNegativeButtonText("取消");
        mContext = context;
        ((PersonInfoActivity)mContext).setAvatarPreference(this);

        setDialogIcon(null);
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        ((Activity)mContext).startActivityForResult(intent, 1);
    }

    public void setPicture(Intent data) {
        Uri uri = data.getData();
        if(Build.VERSION.SDK_INT >= 19 ) {
            if(DocumentsContract.isDocumentUri(mContext, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    picturePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(docId));
                    picturePath = getImagePath(contentUri, null);
                } else if("file".equalsIgnoreCase(uri.getScheme())) {
                    picturePath = uri.getPath();
                }
            }
        } else {
            picturePath = uri.getPath();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        mImage.setImageBitmap(bitmap);
        ((Activity)mContext).setResult(Activity.RESULT_OK);
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mChoose = (Button) view.findViewById(R.id.choose);
        mImage = (ImageView)view.findViewById(R.id.picture);
        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }



    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        persistString(picturePath);
    }
}
