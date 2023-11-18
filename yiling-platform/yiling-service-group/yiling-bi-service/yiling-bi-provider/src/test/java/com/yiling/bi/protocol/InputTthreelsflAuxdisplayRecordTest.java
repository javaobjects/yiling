package com.yiling.bi.protocol;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.protocol.api.InputTthreelsflAuxdisplayRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflAuxdisplayRecordRequest;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;

/**
 * @author fucheng.bai
 * @date 2023/1/10
 */
public class InputTthreelsflAuxdisplayRecordTest extends BaseTest {

    @Autowired
    public InputTthreelsflAuxdisplayRecordApi inputTthreelsflAuxdisplayRecordApi;

    @Test
    public void saveOrUpdateByUniqueTest() {
        InputTthreelsflAuxdisplayRecordRequest request = new InputTthreelsflAuxdisplayRecordRequest();
        request.setProvince("河北");
        request.setBzCode("yl002");
        request.setBzName("河北大运河医药物流有限公司");
        request.setDisplayXm("辅助陈列");
        request.setDisplayNr("端架");
        request.setTxl("1");
        request.setSsyx("1");
        request.setQlqx("1");
        request.setJld("1");
        request.setXlq("1");
        request.setRjt("1");
        request.setYzxj("1");
        request.setSll("1");
        request.setJycf("1");
        request.setYsyx("0");
        request.setLhqw("0");
        request.setLhqk("0");
        request.setXsfh("0");
        request.setSh("0");
        request.setBzbs("0");
        request.setWba("0");
        request.setZsas("0");
        request.setDataTime(new Date());
        request.setDataName("29169");
        request.setDyear("2022");

        inputTthreelsflAuxdisplayRecordApi.saveOrUpdateByUnique(request);
    }
}
