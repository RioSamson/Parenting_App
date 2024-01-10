package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.cmpt276_2021_7_manganese.model.Child;
import com.example.cmpt276_2021_7_manganese.model.ChildManager;
import com.example.cmpt276_2021_7_manganese.model.CoinResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * This class is for Flip coin activity
 * After children data transfer, user can choose child to choose head or tail and flip coin
 * When choose one child which is not the first child the order of children flip will change,
 * the chose children will turn  to the top and be the first one to flip coin, otherwise, the order will be
 * the order user add child.
 *
 * @author Lingjie Li(Larry)
 */

public class FlipCoinActivity extends AppCompatActivity {

    private Button start;
    private CoinImageView mCoinImageView;
    private int currentItem = -1;
    private int result;
    private int currentUserPosition = 0;

    private MediaPlayer player;
    private ChildManager manager;
    private TextView show_name;
    private Spinner show_icon;

    private Toolbar toolbar;
    private AppDatabase db;

    private static final String[] coinChooseList = {"——", "Head", "Tail"};

    private ArrayAdapter<String> coin_adapter;

    private ArrayList<Child> childrenList = new ArrayList<>();
    private ImageView childPhoto;

    private Child childCurrent = null;
    private PopupWindow mPopupWindow;
    private MyListAdapter myListAdapter;
    private boolean isItemClick;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        //Initial Spinner
        initView();
        //Initial data
        initData();
        //Initial listener events
        initListener();
        showIcon();
        setPop();
    }

    private void setPop() {
        childCurrent = childrenList.get(0);
        show_name.setText(childCurrent.getName());
        Glide.with(this).load(childCurrent.getPhotoUrl()).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head).into(childPhoto);
        View popupView = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);

        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);

        ListView mListView = popupView.findViewById(R.id.rv_content);
        myListAdapter = new MyListAdapter(this, childrenList);
        mListView.setAdapter(myListAdapter);
        mListView.setOnItemClickListener((adapterView, view, index, l) -> {
            isItemClick = true;
            if (childrenList.get(index).getName().equals("nobody")) {
                Glide.with(FlipCoinActivity.this).load(R.mipmap.default_head).into(childPhoto);
            } else {
                if (TextUtils.isEmpty(childrenList.get(index).getPhotoUrl())) {
                    Glide.with(FlipCoinActivity.this).load(R.mipmap.default_head).into(childPhoto);
                } else {
                    if (childrenList.get(index).getPhotoUrl().equals("photo.jpg")) {
                        Glide.with(FlipCoinActivity.this).load(R.mipmap.default_head).into(childPhoto);
                    } else {
                        Glide.with(FlipCoinActivity.this).load(childrenList.get(index).getPhotoUrl()).into(childPhoto);
                    }

                }

            }
            childCurrent = childrenList.get(index);
            show_name.setText(childCurrent.getName());

            chooseChildInOrder(index);
        });


    }

    private void showIcon() {
        coin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coinChooseList);
        coin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        show_icon.setAdapter(coin_adapter);
        show_icon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentItem = i - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initListener() {
        start.setOnClickListener(view -> showAnimotion());
        findViewById(R.id.ll_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mPopupWindow) {
                    mPopupWindow.showAsDropDown(findViewById(R.id.show_name));
                }
            }
        });
    }

    private void initData() {
        db = Room.databaseBuilder(this, AppDatabase.class, "database-name").build();
        manager = ChildManager.getInstance();
        ArrayList<Child> manager = this.manager.getManager();
        childrenList.addAll(manager);
        Child child = new Child("nobody", "", -1);
        childrenList.add(child);
    }

    private void initView() {
        start = findViewById(R.id.start);
        mCoinImageView = findViewById(R.id.tiv);
        show_name = findViewById(R.id.show_name);
        show_icon = findViewById(R.id.show_coin);
        toolbar = findViewById(R.id.tb_flip_coin);
        childPhoto = findViewById(R.id.child_photo);
        setUpToolBar(toolbar);
    }

    private void chooseChildInOrder(int position) {
        if (childrenList.get(position).getName().equals("nobody")) {
            return;
        }
        List<Child> strNew = new ArrayList<>();

        for (int i = 0; i < childrenList.size(); i++) {
            if (i != position && !childrenList.get(i).getName().equals("nobody")) {
                strNew.add(childrenList.get(i));
            }
        }
        strNew.add(childrenList.get(position));

        Child child = new Child("nobody", "", -1);
        strNew.add(child);

        childrenList.clear();
        childrenList.addAll(strNew);
        myListAdapter.setListItems(childrenList);
        myListAdapter.notifyDataSetChanged();
    }

    private void showAnimotion() {
        result = new Random().nextInt(2);
        mCoinImageView.clearOtherAnimation();
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.coin_flip);
        }
        player.start();
        mCoinImageView.setInterpolator(new DecelerateInterpolator())
                .setDuration(5200)
                .setCircleCount(35)
                .setXAxisDirection(CoinAnimation.DIRECTION_CLOCKWISE)
                .setYAxisDirection(CoinAnimation.DIRECTION_NONE)
                .setResult(result == 0 ? CoinImageView.RESULT_HEAD : CoinImageView.RESULT_TAIL);
        mCoinImageView.setCoinAnimationListener(new CoinAnimation.CoinAnimationListener() {
            @Override
            public void onDrawableChange(int result, CoinAnimation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String msg;
                if (result == 0) {
                    msg = "Heads!";
                } else {
                    msg = "Tails!";
                }

                showNormalDialog(msg);
                if (null != childCurrent && !childCurrent.getName().equals("nobody")) {
                    String coinResultChoose = "--";
                    if (currentItem == 0) {
                        coinResultChoose = "Head";
                    } else if (currentItem == 1) {
                        coinResultChoose = "Tail";
                    }
                    show_icon.setSelection(0);
                    if (isItemClick) {
                        childCurrent = childrenList.get(0);
                        chooseChildInOrder(0);
                    } else {
                        if (childrenList.size() > 1) {
                            if (childrenList.get(1).getName().equals("nobody")) {
                                childCurrent = childrenList.get(1);
                            } else {
                                childCurrent = childrenList.get(1);
                                chooseChildInOrder(0);
                            }
                        } else {
                            if (childrenList.size() == 1) {
                                chooseChildInOrder(0);
                            }
                        }
                    }

                    show_name.setText(childCurrent.getName());
                    Glide.with(FlipCoinActivity.this).load(childCurrent.getPhotoUrl()).placeholder(R.mipmap.default_head)
                            .error(R.mipmap.default_head).into(childPhoto);

                    CoinResult coinresult = new CoinResult(getUUID(), getTime(), coinResultChoose, result == 0 ? "Head" : "Tail", childCurrent.getName(), childCurrent.getPhotoUrl());

                    new Thread(() -> db.coinDao().insert(coinresult)).start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mCoinImageView.startFlipCoin();
    }

    public String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }

    public String getTime() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return sdf1.format(date);
    }

    private void showNormalDialog(String msg) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(FlipCoinActivity.this);
        normalDialog.setIcon(R.mipmap.msg);
        normalDialog.setTitle("");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("confirm",
                (dialog, which) -> dialog.dismiss());
        normalDialog.setNegativeButton("cancel",
                (dialog, which) -> dialog.dismiss());
        normalDialog.show();
    }

    public static Intent makeLaunchIntent(Context c) {
        return new Intent(c, FlipCoinActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent intent = RecordListActivity.makeLaunchIntent(FlipCoinActivity.this);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_on_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
