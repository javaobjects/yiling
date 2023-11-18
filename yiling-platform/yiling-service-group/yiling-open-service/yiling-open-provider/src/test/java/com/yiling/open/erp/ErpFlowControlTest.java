package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpFlowControlDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpFlowControlHandler;
import com.yiling.open.erp.service.ErpFlowControlService;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpFlowControlTest extends BaseTest {

    @Autowired
    private ErpFlowControlService erpFlowControlService;

    @Autowired
    private ErpFlowControlHandler erpFlowControlHandler;

    @Test
    public void test1() {
        erpFlowControlService.syncFlowControl();
    }

    @Test
    public void test2() {
        Long tag = 222714L;
        String msg = "[{\"addTime\":1666600863521,\"cache_file_md5\":\"d41d8cd98f00b204e9800998ecf8427e\",\"cnt\":1,\"dataMd5\":\"6A37D9B7EA44B533005DBF772A04CF0E\",\"erpPrimaryKey\":\"82hp5ADVxt2oVfohoM41bg==\",\"failedCount\":0,\"file_key\":\"dev/erpPurchaseFlow/901/2022/09/17/f0ea0538b71e4a89878139f5759fee21.zip\",\"file_md5\":\"82hp5ADVxt2oVfohoM41bg==\",\"file_time\":\"2022-09-17\",\"flowCacheFileMd5\":\"d41d8cd98f00b204e9800998ecf8427e\",\"flowKey\":\"2022-09-17\",\"opTime\":1666594271582,\"oper_type\":1,\"su_dept_no\":\"\",\"su_id\":1,\"taskNo\":\"40000005\",\"task_code\":40000006}]";
        erpFlowControlHandler.handleFlowControlMqSync(tag, ErpTopicName.ErpFlowControl.getMethod(), JSONArray.parseArray(msg, ErpFlowControlDTO.class));
    }

    @Test
    public void test3() {
        erpFlowControlService.synFlowControlPage();
    }


}
