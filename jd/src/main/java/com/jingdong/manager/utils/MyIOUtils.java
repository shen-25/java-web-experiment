package com.jingdong.manager.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyIOUtils {
    public static JSONObject getJson(HttpServletRequest request) throws IOException {
        ServletInputStream requestInputStream = request.getInputStream();
        InputStreamReader ir = new InputStreamReader(requestInputStream,"utf-8");
        BufferedReader br = new BufferedReader(ir);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null) {
            sb.append(line);
        }

        JSONObject json = JSONObject.parseObject(sb.toString());

        return json;
    }

}
