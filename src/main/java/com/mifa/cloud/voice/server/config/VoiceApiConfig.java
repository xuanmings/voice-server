package com.mifa.cloud.voice.server.config;

import com.google.common.collect.ImmutableMap;
import com.mifa.cloud.voice.server.api.aliyun.AliyunVoiceApi;
import com.mifa.cloud.voice.server.api.jx.JxVoiceApi;
import com.mifa.cloud.voice.server.api.montnets.MontnetsVoiceApi;
import com.mifa.cloud.voice.server.component.properties.AppProperties;
import com.mifa.cloud.voice.server.utils.BaseRetrofitUtils2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * @author: sxm
 * @date: 2018/4/10 12:34
 * @version: v1.0.0
 */
@Configuration
@PropertySource(value = {"classpath:application.properties"},ignoreResourceNotFound = true,encoding = "UTF-8")
public class VoiceApiConfig {
    private static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";

    @Autowired(required = false)
    AppProperties appProperties;

    @Bean
    public MontnetsVoiceApi montnetsVoiceApi(){
        return BaseRetrofitUtils2.newBuilder(appProperties.getMontnetsVoice().getVoiceUrl())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .retryWhenTimeout(2)
                .build().create(MontnetsVoiceApi.class);
    }

    @Bean
    public AliyunVoiceApi aliyunVoiceApi(){
        return BaseRetrofitUtils2.newBuilder(appProperties.getAliyunVoice().getVoiceUrl())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .retryWhenTimeout(2)
                .build().create(AliyunVoiceApi.class);
    }

    @Bean
    public JxVoiceApi jxVoiceApi(){
        return BaseRetrofitUtils2.newBuilder(appProperties.getJixinVoice().getVoiceUrl())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .headers(ImmutableMap.of("Content-Type",JSON_CONTENT_TYPE,"Accept",JSON_CONTENT_TYPE))
                .retryWhenTimeout(2)
                .build().create(JxVoiceApi.class);
    }

   private  String getSig(String timestamp){
        return DigestUtils.sha1Hex(this.appProperties.getJixinVoice().getAccountId()+this.appProperties.getJixinVoice().getAuthToken()+timestamp).toLowerCase();
    }
}
