package com.yiling.bi.protocol;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.protocol.api.InputTthreelsflAuxdisplayRecordApi;
import com.yiling.bi.protocol.api.InputTthreelsflLhauxdisplayRecordApi;
import com.yiling.bi.protocol.api.InputTthreelsflRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflRecordRequest;


/**
 * @author fucheng.bai
 * @date 2023/1/10
 */
public class InputTthreelsflRecordTest extends BaseTest {

    @Autowired
    public InputTthreelsflRecordApi inputTthreelsflRecordApi;

    @Test
    public void saveOrUpdateByUniqueTest() {
        InputTthreelsflRecordRequest request = new InputTthreelsflRecordRequest();
        request.setProvince("江西");
        request.setBzCode("yl004");
        request.setBzName("测试4");
        request.setNkaZb("测试NKA1");
        request.setLsType("连锁");
        request.setStoreNum(new BigDecimal("10"));
        request.setGoodsType("连花系列");
        request.setBreed("双花");
        request.setGoodsId("103");
        request.setDosageForm("片剂");
        request.setGoodsSpec("");
        request.setHsPrice("8.01");
        request.setLsPrice(new BigDecimal("21"));
        request.setSfLhpe("1");
        request.setQdType("1");
        request.setDataTime(new Date());
        request.setDataName("29169");
        request.setDyear("2022");

        inputTthreelsflRecordApi.saveOrUpdateByUnique(request);
    }

}
