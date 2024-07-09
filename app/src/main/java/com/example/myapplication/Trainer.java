package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Entity.CRUD;
import com.example.myapplication.Entity.Trick;
import com.example.myapplication.Entity.TrickAdapter;
import com.example.myapplication.Entity.TrickDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Trainer extends AppCompatActivity implements AdapterView.OnItemClickListener{

    /** 上下文 */
    private Context context = this;
    /** 浮动添加按钮 */
    private FloatingActionButton trainer_add_item;
    /** 核心列表视图 */
    private ListView trainer_trick_list;
    /** 数据适配器 */
    private TrickAdapter trickAdapter;
    /** 招式列表 */
    private List<Trick> trickList = new ArrayList<>();
    /** 顶栏 */
    private Toolbar trainer_toolBar;

    /**
     * 获取布局文件中的各元素 将适配器注入招式列表数据 将适配器和列表视图绑定并添加点击监听 顶栏添加支持 add按钮添加监听
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        trainer_add_item = findViewById(R.id.trainer_add_item);
        trainer_trick_list = findViewById(R.id.trainer_trick_list);
        trickAdapter = new TrickAdapter(getApplicationContext(), trickList);
        refreshListView();
        trainer_trick_list.setAdapter(trickAdapter);
        trainer_trick_list.setOnItemClickListener(this);
        trainer_toolBar = findViewById(R.id.trainer_toolBar);
        setSupportActionBar(trainer_toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trainer_add_item.setOnClickListener(new View.OnClickListener() {
            /**
             * 针对add按钮实现OnClickListener方法，新建intent，注入mode=4为新建模式，执行跳转到edit活动
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trainer.this, Trainer_add_item_edit.class);
                intent.putExtra("mode", 4);
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * 实现FragmentActivity的获取返回值方法，用于处理上一个活动返回的值
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 意图数据，即返回值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String content = data.getStringExtra("content");
        String time = data.getStringExtra("time");
        Trick trick = new Trick(content, time, 1);
        CRUD op = new CRUD(context);
        op.open();
        op.addItem(trick);
        op.close();
        refreshListView();
    }

    /**
     * 刷新函数，用于在进入Trainer页面或者从edit页面返回时调用
     * 用当前上下文打开CRUD数据库，清空trickList并重新add所有元素，通知适配器更改
     */
    public void refreshListView() {
        CRUD op = new CRUD(context);
        op.open();
        //set adapter
        if (trickList.size() > 0) trickList.clear();
        trickList.addAll(op.getItemList());
        op.close();
        trickAdapter.notifyDataSetChanged();
    }

    /**
     * 实现AdapterView.OnItemClickListener类的方法，用于适配器对于点击列表元素做出反应
     * 注入mode=3为编辑模式，指定intent跳转到edit页面
     * @param parent 父元素
     * @param view 视图
     * @param position 位置
     * @param id 识别子元素的id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.trainer_trick_list:
                Trick curr = (Trick) parent.getItemAtPosition(position);
                Intent intent = new Intent(Trainer.this, Trainer_add_item_edit.class);
                intent.putExtra("content", curr.getContent());
                intent.putExtra("id", curr.getId());
                intent.putExtra("time", curr.getTime());
                intent.putExtra("mode", 3); //信号量，click to edit模式
                intent.putExtra("tag", curr.getTag());
                startActivityForResult(intent, 1);
                break;
        }
    }
}