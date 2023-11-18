package com.yiling.dataflow.crm;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;

import cn.hutool.json.JSONUtil;

/**
 * @author fucheng.bai
 * @date 2023/6/20
 */
public class CrmHospitalDrugstoreRelationTest extends BaseTest {

    @Autowired
    private CrmHospitalDrugstoreRelationApi crmHospitalDrugstoreRelationApi;

    @Test
    public void saveOrUpdateTest() {
        String reqStr = "{\"lastOpTime\":1687231130482,\"effectStartTime\":1686326400000,\"lastOpUser\":220,\"hospitalOrgId\":229566,\"effectEndTime\":1688054400000,\"opUserId\":220,\"categoryName\":\"通心络\",\"drugstoreOrgName\":\"杭州瑞人堂医药连锁有限公司月明路健康药房\",\"crmGoodsSpec\":\"0.26g*30粒*400盒\",\"opTime\":1687231130479,\"crmGoodsCode\":11,\"hospitalOrgName\":\"浙江大学附属邵逸夫医院下沙院区\",\"crmGoodsName\":\"通心络胶囊0.26g*30粒*400盒\",\"dataSource\":1,\"categoryId\":2,\"drugstoreOrgId\":472458}\n";
        SaveOrUpdateCrmHospitalDrugstoreRelRequest request = JSONUtil.toBean(reqStr, SaveOrUpdateCrmHospitalDrugstoreRelRequest.class);
        crmHospitalDrugstoreRelationApi.saveOrUpdate(request);
    }
}
