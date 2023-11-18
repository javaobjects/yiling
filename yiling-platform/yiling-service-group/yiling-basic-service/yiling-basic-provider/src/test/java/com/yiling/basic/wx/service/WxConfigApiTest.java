package com.yiling.basic.wx.service;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.yiling.basic.BaseTest;
import com.yiling.basic.wx.api.WxAccountApi;

/**
 * @author zhigang.guo
 * @date: 2022/2/28
 */
public class WxConfigApiTest extends BaseTest {
    @Autowired
    private WxAccountApi wxAccountApi;


    @Test
    public void Test () {

        Map<String,String> map = Maps.newHashMap();

        map.put("url","https%3A%2F%2Fh-test.59yi.com%2Fsale%2Ftest");
        Map<String,String> resultMap = wxAccountApi.wxSign(map,"jsapi");

        System.out.println(resultMap + "====" );


    }
}
