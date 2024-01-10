package com.example.cmpt276_2021_7_manganese;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.cmpt276_2021_7_manganese.model.Child;
import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * This class is for add child
 * user can use this class to add child, by clicking floating button
 *
 * @author Shuai Li & Yam
 * @author Yam for Iteration2
 */
public class AddChildActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "Child";
    private static int indexForSwitchActivity = -1;
    private boolean isSaved = false;
    private EditText inputName;
    private String name;
    private ChildManager childManager;
    private static final String DefaultPhoto = "photo.jpg";
    private String PhotoUrl;
    private ImageView Photo = null;
    private Button TakePhoto;
    private Button SkipPhoto;
    private int idForChild;

    //whether user change their photo done.
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(AddChildActivity.this, "Change Successfully", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(AddChildActivity.this, "Generate one", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddChildActivity.this, "Change Fail", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Child child;
    private int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        Toolbar toolbar = findViewById(R.id.add_child_toolbar);
        setSupportActionBar(toolbar);
        childManager = ChildManager.getInstance();
        indexForSwitchActivity = getIntent().getIntExtra(EXTRA_MESSAGE, -1);

        idForChild = loadID();
        inputName = findViewById(R.id.et_name);
        inputName.addTextChangedListener(tw);
        Photo = findViewById(R.id.iv_photo);
        TakePhoto = findViewById(R.id.bt_take_photo);
        TakePhotoAccess();
        SkipPhoto = findViewById(R.id.bt_skip_photo);
        SkipAccess();
        child = (Child) getIntent().getSerializableExtra("child");
        if (null != child) {
            inputName.setText(child.getName());
            Glide.with(this).load(child.getPhotoUrl()).placeholder(R.mipmap.default_head)
                    .error(R.mipmap.default_head).into(Photo);
            pos = getIntent().getIntExtra("Child", -1);
        }
    }

    private void TakePhotoAccess() {
        TakePhoto.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                AndPermission.with(AddChildActivity.this)
                        .runtime()
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .onGranted(permissions -> {
                            PictureSelector.create(AddChildActivity.this)
                                    .openGallery(PictureMimeType.ofImage())
                                    .isCamera(true)
                                    .isZoomAnim(true)// Image list click zoom effect defaults to true
                                    .isPreviewImage(true)// Whether images can be previewed
                                    .isCompress(true)// Whether the compression
                                    .isEnableCrop(true)
                                    .withAspectRatio(1, 1)
                                    .setLanguage(2)
                                    .compressQuality(50)// The output quality of the compressed picture is 0~ 100
                                    .synOrAsy(false)//Synchronous true or asynchronous false Compression default synchronization
                                    .maxSelectNum(1)
                                    .showCropFrame(false)// Whether to display clipping rectangle border Circle clipping You are advised to set this parameter to false
                                    .showCropGrid(false)// Whether to display clipping rectangular mesh circle clipping Set to false is recommended
                                    .imageEngine(GlideEngine.createGlideEngine()) // See Demo glideengine.java
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        })
                        .onDenied(permissions -> {
                            // Storage permission are not allowed.
                        })
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // result callback
                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
                    if (null != result && result.size() > 0) {
                        Glide.with(AddChildActivity.this).load(result.get(0).getCompressPath()).into(Photo);
                        PhotoUrl = result.get(0).getCompressPath();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void SkipAccess() {
        SkipPhoto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                PhotoUrl = DefaultPhoto;
            }
        });
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String strName = inputName.getText().toString().trim();
            if (strName.length() <= 0) {
                isSaved = false;
                return;
            }
            isSaved = true;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_backup:
                if (isSaved) {
                    name = inputName.getText().toString();
                    if (indexForSwitchActivity < 0) {
                        addChildToManager();
                    } else {
                        editChildInManager();
                    }
                    finish();
                } else {
                    Toast.makeText(this, "Cannot save with invalid inputs!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                childManager.removeChild(indexForSwitchActivity);
                finish();
            case R.id.action_back:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent makeLaunchIntent(Context c) {
        Intent intent = new Intent(c, AddChildActivity.class);
        intent.putExtra(EXTRA_MESSAGE, -1);
        return intent;
    }

    public static Intent makeLaunchIntentWithPosition(Context c, int position) {
        Intent intent = new Intent(c, AddChildActivity.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        return intent;
    }

    // add a child to the exist manager object, when indexForSwitchActivity < 0
    private void addChildToManager() {
        ChildManager manager = ChildManager.getInstance();
        manager.add(new Child(name, PhotoUrl, idForChild));
        idForChild++;
    }

    // edit a child, when indexForSwitchActivity >= 0
    private void editChildInManager() {
        ChildManager manager = ChildManager.getInstance();
        manager.getByIndex(indexForSwitchActivity).setName(name);
        if (!TextUtils.isEmpty(PhotoUrl)) {
            manager.getByIndex(indexForSwitchActivity).setPhotoUrl(PhotoUrl);
        }
    }

    // a static function to set indexForSwitchActivity in onDestroy()
    private static void setIndex(int position) {
        indexForSwitchActivity = position;
    }

    @Override
    protected void onDestroy() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        super.onDestroy();
        jsonSave();
        saveID();
        setIndex(-1);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (indexForSwitchActivity >= 0) {
            getMenuInflater().inflate(R.menu.backup_and_delete_on_action_bar, menu);
        } else {
            getMenuInflater().inflate(R.menu.backup_on_action_bar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (indexForSwitchActivity >= 0) {
            this.setTitle("Edit your child");
        } else {
            this.setTitle("Add your child");
        }
    }

    private void jsonSave() {
        String jsonString = childManager.getGsonString();
        SharedPreferences prefs = this.getSharedPreferences("tag", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("save", jsonString);
        editor.apply();
    }

    private void saveID() {
//        String jsonString = childManager.getGsonString();
        SharedPreferences prefs = this.getSharedPreferences("child tag", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("save", jsonString);
        editor.putInt("childID", idForChild);
        editor.apply();
    }

    private int loadID() {
        SharedPreferences prefs = this.getSharedPreferences("child tag", MODE_PRIVATE);
        int id = prefs.getInt("childID", 1);
        return id;
    }

}