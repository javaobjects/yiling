package com.yiling.open.erp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dao.ErpSaleFlowMapper;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.entity.ErpSaleFlowDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpSaleFlowHandler;
import com.yiling.open.erp.service.ErpSaleFlowService;

import cn.hutool.json.JSONUtil;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpSaleFlowServiceTest extends BaseTest {

    @Autowired
    ErpSaleFlowService erpSaleFlowService;

    @Autowired
    ErpSaleFlowHandler erpSaleFlowHandler;

    @Autowired
    ErpSaleFlowMapper erpSaleFlowMapper;

    @Test
    public void Test1() {
        erpSaleFlowService.synSaleFlow();
    }

    @Test
    public void Test2() {
        Long suId = 1246L;
        String taskNo = ErpTopicName.ErpSaleFlow.getMethod();
        String msg = "[{\"cnt\":1,\"dataMd5\":\"DD101DCC0855F447860971CACD803541\",\"enterprise_inner_code\":\"203333\",\"enterprise_name\":\"上海电力医院\",\"erpPrimaryKey\":\"\",\"failedCount\":0,\"goods_in_sn\":\"12004210|400\",\"goods_name\":\"芪苈强心胶囊\",\"license_number\":\"\",\"opTime\":1668045364000,\"oper_type\":3,\"order_time\":\"2022-11-10\",\"so_batch_no\":\"B2206029\",\"so_effective_time\":\"2024-11-30 00:00:00\",\"so_id\":\"f30be0daa5d6b88a7624401203a1e386\",\"so_license\":\"国药准字Z20040141\",\"so_manufacturer\":\"石家庄以岭药业股份有限公司\",\"so_no\":\"100003220624211\",\"so_price\":821.25,\"so_product_time\":\"2022-10-02 00:00:00\",\"so_quantity\":25.00,\"so_source\":\"其它三方渠道\",\"so_specifications\":\"0.3g*36S\",\"so_time\":\"2022-10-31 00:00:00\",\"order_time\":\"\",\"so_unit\":\"盒\",\"su_dept_no\":\"CMS-0461\",\"su_id\":1246,\"taskNo\":\"40000007\"}]";
        //        String msg = "[{\"cnt\":1,\"dataMd5\":\"4370D3B8BADEC803FD8E7873F36D5D01\",\"enterprise_inner_code\":\"69645\",\"enterprise_name\":\"泰州市京东医药有限责任公司\",\"erpPrimaryKey\":\"5226ac8b1292d71bae19da39d071ad01\",\"failedCount\":0,\"goods_in_sn\":\"123409\",\"goods_name\":\"参松养心胶囊\",\"opTime\":1651856642310,\"oper_type\":1,\"so_batch_no\":\"2203003\",\"so_effective_time\":\"2023-06-30 00:00:00\",\"so_id\":\"5226ac8b1292d71bae19da39d071ad01\",\"so_license\":\"国药准字Z20103032\",\"so_manufacturer\":\"北京以岭药业有限公司\",\"so_no\":\"XSAFDJ142198001\",\"so_price\":11.5,\"so_product_time\":\"2021-07-07 00:00:00\",\"so_quantity\":-350,\"so_source\":\"POP采购\",\"so_specifications\":\"0.4gx36粒\",\"so_time\":\"2022-05-05 00:00:00\",\"so_unit\":\"盒\",\"su_dept_no\":\"\",\"su_id\":4333,\"taskNo\":\"40000007\"}]";
        List< ErpSaleFlowDTO > erpSaleFlowList = JSONArray.parseArray(msg, ErpSaleFlowDTO.class);

        Boolean result = erpSaleFlowHandler.handleSaleFlowMqSync(suId, taskNo, erpSaleFlowList);
        System.out.println(">>>>> result:" + result);
    }

    @Test
    public void test3() {
        String jsonStr = "  {\n" + "    \"id\": 13010,\n" + "    \"su_id\": 4333,\n" + "    \"su_dept_no\": \"\",\n" + "    \"so_id\": \"5226ac8b1292d71bae19da39d071ad01\",\n" + "    \"so_no\": \"XSAFDJ142198001\",\n" + "    \"so_time\": \"2022-05-05 00:00:00\",\n" + "    \"order_time\": \"1970-01-01 00:00:00\",\n" + "    \"enterprise_inner_code\": \"69645\",\n" + "    \"enterprise_name\": \"河北大运河医药物流有限公司\",\n" + "    \"license_number\": \"\",\n" + "    \"so_batch_no\": \"2203003\",\n" + "    \"so_quantity\": -350.00,\n" + "    \"so_product_time\": \"2021-07-07 00:00:00\",\n" + "    \"so_effective_time\": \"2023-06-30 00:00:00\",\n" + "    \"so_price\": 11.50,\n" + "    \"goods_in_sn\": \"123409\",\n" + "    \"goods_name\": \"参松养心胶囊\",\n" + "    \"so_license\": \"国药准字Z20103032\",\n" + "    \"so_specifications\": \"0.4gx36粒\",\n" + "    \"so_unit\": \"盒\",\n" + "    \"so_manufacturer\": \"北京以岭药业有限公司\",\n" + "    \"so_source\": \"POP采购\",\n" + "    \"cnt\": 1,\n" + "    \"control_id\": 0,\n" + "    \"data_md5\": \"DD101DCC0855F447860971CACD803541\",\n" + "    \"add_time\": \"2022-06-16 21:29:16\",\n" + "    \"edit_time\": null,\n" + "    \"oper_type\": 1,\n" + "    \"sync_status\": 0,\n" + "    \"sync_time\": \"2022-06-16 21:29:21\",\n" + "    \"sync_msg\": \"未开启同步规则\",\n" + "    \"update_count\": 0,\n" + "    \"failed_count\": 0,\n" + "    \"data_tag\": 1\n" + "  }";
        ErpSaleFlowDO erpSaleFlowDO = JSONUtil.toBean(jsonStr, ErpSaleFlowDO.class);
        erpSaleFlowDO.setId(null);
//        erpSaleFlowDO.setDataTag(null);

        List<ErpSaleFlowDO> list = new ArrayList<>();
        list.add(erpSaleFlowDO);
        erpSaleFlowMapper.batchInsert(list);
    }
}
