package com.yiling.bi.protocol;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.protocol.api.InputTthreelsflLhauxdisplayRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflLhauxdisplayRecordRequest;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;

/**
 * @author fucheng.bai
 * @date 2023/1/10
 */
public class InputTthreelsflLhauxdisplayRecordTest extends BaseTest {

    @Autowired
    public InputTthreelsflLhauxdisplayRecordApi inputTthreelsflLhauxdisplayRecordApi;

    @Test
    public void saveOrUpdateByUniqueTest() {
        InputTthreelsflLhauxdisplayRecordRequest request = new InputTthreelsflLhauxdisplayRecordRequest();
        request.setProvince("河北");
        request.setBzCode("yl002");
        request.setBzName("河北大运河医药物流有限公司");
        request.setDisplayXm("辅助陈列");
        request.setDisplayStorenum(new BigDecimal("10"));
        request.setStoreLevel("门店级别");
        request.setBracket("1");
        request.setPilehead("1");
        request.setFlowerCar("1");
        request.setGtPilehead("1");
        request.setCashDesk("1");
        request.setStud("0");
        request.setLampBox("0");
        request.setShowbill("0");
        request.setShopwindow("0");
        request.setDataTime(new Date());
        request.setDataName("29169");
        request.setDyear("2022");

        inputTthreelsflLhauxdisplayRecordApi.saveOrUpdateByUnique(request);
    }
}
