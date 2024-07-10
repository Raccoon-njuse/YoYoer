package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Trainer_add_item_edit extends AppCompatActivity {

    private String old_content = "";
    private String old_time = "";
    private int old_tag = 1;
    private long id = 0;
    /** 3表示编辑现有，4表示新建 */
    private int openMode = 0;
    private int tag = 1;
    /** message to send */
    private boolean tagChange = false;

    private EditText editText;
    private Intent intent = new Intent();

    /**
     * 由Trainer的两个地方启动本页面，1：点击加号 2：点击列表
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
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("KeyCode" , new Date().toString() + " " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            getResultIntent();
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
    public void getResultIntent() {
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
}