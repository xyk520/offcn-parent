package com.offcn.user.utils;

import com.offcn.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsTemplate {
    @Value("${sms.path}")
    private String path; // "/dx/sendSms";
    @Value("${sms.method}")
    private String method; // "POST";
    @Value("${sms.appCode}")
    private String appCode; // "APPCODE f44b9437185349ad89f5504b8e01f393";
    @Value("${sms.host}")
    private String host; // "http://dingxin.market.alicloudapi.com";

    public String sendCode(String phoneNum,String code){

        Map<String,String> header = new HashMap<>();
        header.put("Authorization",appCode);
        header.put("Content-Type","application/x-www-form-urlencoded");

        Map<String,String> querys = new HashMap<>();
        querys.put("mobile",phoneNum);
        querys.put("param","code:" + code);
        querys.put("tpl_id","TP1711063");

        try {
            HttpResponse httpResponse = HttpUtils.doPost(host, path, method, header, querys, "");
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail"; // 失败
        }

    }
}
