package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class test1 {
    public static void main(String[] args){
//        String s="[\"a\",\"b\"]";
//        JSONArray jsonArray=JSONArray.parseArray(s);
//        List<String> list=jsonArray.toJavaList(String.class);
//        System.out.println(list.size());

        String s="2018/1/2 13:11";
        Format format=new SimpleDateFormat("yyyy/m/d HH:mm");
        try {
            ((SimpleDateFormat) format).parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
