package com.example.YoYoer.Global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Global {
    /**
     * 0-default; 1-easy; 2-hard; 3-very hard
     */
    public static final ArrayList<String> DIFFICULTY_TAG_TABLE = new ArrayList<String>(){{
        add("default");
        add("easy");
        add("hard");
        add("very hard");
    }};

    /**
     * Util方法，日期转str
     * @return 代表日期的字符串
     */
    public static String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
