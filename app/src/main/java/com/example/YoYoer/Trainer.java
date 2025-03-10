package com.example.YoYoer;

import static com.example.YoYoer.Global.Global.DIFFICULTY_TAG_TABLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.YoYoer.Entity.CRUD;
import com.example.YoYoer.Entity.Trick;
import com.example.YoYoer.Entity.TrickAdapter;
import com.example.YoYoer.Global.Global;
import com.example.YoYoer.Utils.ExcelUtils;
import com.example.YoYoer.Utils.UriUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Trainer extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static final int ADD_ITEM_REQUEST_CODE = 0;

    public static final int EDIT_ITEM_REQUEST_CODE = 1;

    public  static final int ADD_FROM_EXCEL_REQUEST_CODE = 2;

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
        updateListAndNotify();
        trainer_trick_list.setAdapter(trickAdapter);
        trainer_trick_list.setOnItemClickListener(this);
        trainer_toolBar = findViewById(R.id.trainer_toolBar);
        trainer_toolBar.setTitle("招式列表");
        setSupportActionBar(trainer_toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trainer_toolBar.setNavigationOnClickListener(v -> finish());
//        trainer_toolBar.setNavigationIcon(R.drawable.baseline_menu_24);
        trainer_add_item.setOnClickListener(new View.OnClickListener() {
            /**
             * 针对add按钮实现OnClickListener方法，新建intent，注入mode=4为新建模式，执行跳转到edit活动
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trainer.this, Trainer_edit.class);
                intent.putExtra("mode", 4);
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            }
        });
    }

    /**
     * 从上一个活动返回列表活动
     * 1. 从新建活动返回 根据返回模式决定数据库操作（不变，更新，新建）并更新视图
     * 2. 从编辑活动返回 同上
     * 3. 从文件选择器活动返回 返回data可以直接转uri File， 读取并更新数据库，添加并更新视图
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 意图数据，即返回值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case ADD_ITEM_REQUEST_CODE:
            case EDIT_ITEM_REQUEST_CODE:
                int returnMode;
                long id;
                returnMode = data.getExtras().getInt("mode", -1);
                id = data.getExtras().getLong("id", 0);
                if (returnMode == 1) {
                    String content = data.getExtras().getString("content");
                    String time = data.getExtras().getString("time");
                    int tag = data.getExtras().getInt("tag", -1);
                    Trick trick_to_update = new Trick(content, time, tag);
                    trick_to_update.setId(id);
                    CRUD op = new CRUD(context);
                    op.open();
                    op.updateItem(trick_to_update);
                    op.close();
                } else if (returnMode == 0) {
                    String content = data.getExtras().getString("content");
                    String time = data.getExtras().getString("time");
                    int tag = data.getExtras().getInt("tag", 1);
                    Trick trick_to_new = new Trick(content, time, tag);
                    CRUD op = new CRUD(context);
                    op.open();
                    op.addItem(trick_to_new);
                    op.close();
                } else if (returnMode == 2) {
                    Trick trick_to_delete = new Trick();
                    trick_to_delete.setId(id);
                    CRUD op = new CRUD(context);
                    op.open();
                    op.removeItem(trick_to_delete);
                    op.close();
                } else {
                    //Do nothing
                }
                updateListAndNotify();
                break;
            case ADD_FROM_EXCEL_REQUEST_CODE:
                Uri uri = data.getData();
                File excelFile = UriUtils.uriToFileApiQ(uri, this);
                if (ExcelUtils.isExcelFile(excelFile)) {
                    try {
                        String[][] table = ExcelUtils.readExcel(excelFile);
                        ArrayList<Trick> list_to_add = tableToTrickList(table);
                        CRUD op = new CRUD(context);
                        op.open();
                        for(Trick t : list_to_add) op.addItem(t);
                        op.close();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                updateListAndNotify();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 刷新适配器函数
     * 用当前上下文打开CRUD数据库，清空适配器的trickList变量并重新add所有元素，通知适配器更改
     * 1. 进入Trainer页面时，notify语句本质上不起作用，因为此时还没有setAdapter
     * 2. 从编辑页面返回时，为避免反复setAdapter造成性能开销，采用了notify方法
     */
    public void updateListAndNotify() {
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
                Intent intent = new Intent(Trainer.this, Trainer_edit.class);
                intent.putExtra("content", curr.getContent());
                intent.putExtra("id", curr.getId());
                intent.putExtra("time", curr.getTime());
                intent.putExtra("mode", 3); //信号量，click to edit模式
                intent.putExtra("tag", curr.getTag());
                startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                break;
        }
    }

    /**
     * 渲染菜单的方法
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 监听顶栏删除键
     * 当按下删除全部按钮，弹出对话框询问
     * 是：删除全部元素，将id下标改成从0开始
     * 否：取消对话框
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trainer_menu_delete_all:
                new AlertDialog.Builder(Trainer.this)
                        .setMessage("确认删除全部吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CRUD op = new CRUD(context);
                                op.open();
                                op.clear();
                                op.close();
                                updateListAndNotify();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            case R.id.trainer_menu_add_from_excel:
                new AlertDialog.Builder(Trainer.this)
                        .setMessage("从excel导入")
                        .setPositiveButton("选择文件", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("*/*");
                                startActivityForResult(intent, ADD_FROM_EXCEL_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 将二维字符表转换为招式列表，添加默认元素
     * TODO 表格硬编码
     * @param table
     * @return
     */
    private ArrayList<Trick> tableToTrickList(String[][] table) {
        ArrayList<Trick> res = new ArrayList<>();
        for (String[] col : table) {
            if (col[0].equals("content")) continue;
            String content = (col[0].equals("")) ? "dName" : col[0];
            String time = (col[1].equals("")) ? Global.dateToStr() : col[1];
            String tagContent = (col[2].equals("")) ? "default" : col[2];
            int tag = DIFFICULTY_TAG_TABLE.indexOf(tagContent);
            Trick trick = new Trick(content, time, tag);
            res.add(trick);
        }
        return res;
    }
}