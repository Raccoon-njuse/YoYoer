package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Trainer_edit extends AppCompatActivity {
    /** 编辑页面组件 */
    private EditText editText;
    /** 顶栏组件 */
    private Toolbar trainer_edit_toolBar;

    /** 当启动模式为3时，用于保存旧内容 */
    private String old_content = "";
    /** 当启动模式为3时，用于保存旧时间 */
    private String old_time = "";
    /** 当启动模式为3时，用于保存旧标签 */
    private int old_tag = 1;
    /** 用于保存旧id或者创建新id */
    private long id = 0;
    /** 启动模式，3表示编辑现有，4表示新建 */
    private int openMode = 0;
    /** 新标签 */
    private int tag = 1;
    /** 是否改变标签 */
    private boolean tagChange = false;
    /** 用于返回的intent */
    private Intent intent = new Intent();

    /**
     * 渲染顶栏
     * 可能由Trainer的两个地方启动本页面，1：点击加号 2：点击列表
     * 读取Trainer传过来的intent中的数据到全局变量
     * 若为编辑模式，则将所有数据注入editText，显示到编辑界面，光标移到文末
     * 否则为新建模式，默认打开新的edit页面
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainer_add_item_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        trainer_edit_toolBar = findViewById(R.id.trainer_edit_toolBar);
        trainer_edit_toolBar.setTitle("招式内容");
        setSupportActionBar(trainer_edit_toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trainer_edit_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            /**
             * 设置导航栏返回按钮事件
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                autoSetResultIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        editText = findViewById(R.id.trainer_add_item_editText);

        Intent getIntent = getIntent();
        openMode = getIntent.getIntExtra("mode", 0);
        if (openMode == 3) {
            id = getIntent.getLongExtra("id", 0);
            old_content = getIntent.getStringExtra("content");
            old_time = getIntent.getStringExtra("time");
            old_tag = getIntent.getIntExtra("tag", 1);
            editText.setText(old_content);
            editText.setSelection(old_content.length());
        }
    }

    /**
     * 按下按钮的监听方法，当按下返回键时，调用getResultIntent方法，返回带mode的intent；按下home键时，直接return
     * @param keyCode 监听手机按钮的键值
     * @param event 将keyCode包装为事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("KeyCode" , new Date().toString() + " " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            autoSetResultIntent();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 根据启动模式和用户交互判断应当给返回intent注入什么mode
     * 1. 编辑模式，用户无输入，并且tag未改变，则返回mode=-1
     * 2. 编辑模式，用户有输入或者tag已经改变，将新的内容注入intent，返回mode=1
     * 3. 新建模式，用户无输入，返回mode=-1
     * 4. 新建模式，用户有输入，返回mode=0，将用户输入的信息注入intent返回上级活动
     */
    public void autoSetResultIntent() {
        if (openMode == 4) {
            if (editText.getText().toString().length() == 0) {
                intent.putExtra("mode", -1);
            } else {
                intent.putExtra("mode", 0);
                intent.putExtra("content", editText.getText().toString());
                intent.putExtra("time", dateToStr());
                intent.putExtra("tag", tag);
            }
        } else {
            if (editText.getText().toString().equals(old_content) && !tagChange) {
                intent.putExtra("mode", -1);
            } else {
                intent.putExtra("mode", 1);
                intent.putExtra("content", editText.getText().toString());
                intent.putExtra("id", id);
                intent.putExtra("tag", tag);
                intent.putExtra("time", dateToStr());
            }
        }
    }

    /**
     * Util方法，日期转str
     * @return 代表日期的字符串
     */
    public String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 渲染菜单的方法
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainer_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}