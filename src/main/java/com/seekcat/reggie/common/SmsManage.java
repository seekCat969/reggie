package com.seekcat.reggie.common;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SmsManage {

    public void sendMessage(String phoneNumber) throws Exception {
        Config config = new Config()
                // 配置 AccessKey ID，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(System.getenv("OSS_ACCESS_KEY_ID"))
                // 配置 AccessKey Secret，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(System.getenv("OSS_ACCESS_KEY_SECRET"));
                // System.getenv()方法表示获取系统环境变量，不要直接在getenv()中填入AccessKey信息。

        // 配置 Endpoint。中国站请使用dysmsapi.aliyuncs.com
        config.endpoint = "dysmsapi.aliyuncs.com";

        Client client = new Client(config);

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName("阿里云通信")
                .setTemplateCode("SMS_317740309")
                // TemplateParam为序列化后的JSON字符串。其中\"表示转义后的双引号。
                .setTemplateParam("{\"code\":\"1234\"}");


        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

        // 响应包含服务端响应的 body 和 headers
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(sendSmsResponse));

    }

    public static void main(String[] args) throws Exception {
        SmsManage smsManage = new SmsManage();
        smsManage.sendMessage("18740756386");
    }
}
