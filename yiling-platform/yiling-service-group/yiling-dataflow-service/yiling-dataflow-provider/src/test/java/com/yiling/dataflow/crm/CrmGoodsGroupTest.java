package com.yiling.dataflow.crm;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmGoodsCategoryService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: shuang.zhang
 * @date: 2022/9/21
 */
public class CrmGoodsGroupTest extends BaseTest {

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    private CrmGoodsCategoryService crmGoodsCategoryService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Test
    public void test() {
        crmEnterpriseRelationShipService.getUseByDepartRelationIds(ListUtil.toList(80L));
        //crmGoodsGroupService.getCrmGoodsGroupAll();
    }

    @Test
    public void test1() {
        crmGoodsCategoryService.findFirstCategoryByFinal(5L,"wash_202304");
    }
}
