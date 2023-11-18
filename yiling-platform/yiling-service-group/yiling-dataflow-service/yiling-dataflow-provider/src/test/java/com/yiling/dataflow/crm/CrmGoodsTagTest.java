package com.yiling.dataflow.crm;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.service.CrmGoodsTagService;

import cn.hutool.core.collection.ListUtil;

/**
 * @author shichen
 * @类名 CrmGoodsTagTest
 * @描述
 * @创建时间 2023/4/13
 * @修改人 shichen
 * @修改时间 2023/4/13
 **/
public class CrmGoodsTagTest extends BaseTest {
    @Autowired
    private CrmGoodsTagService crmGoodsTagService;

    @Test
    public void test1(){
        Boolean tags = crmGoodsTagService.batchSaveTagsByGoods(ListUtil.toList(1L), 1507L, 0L);
        System.out.println(tags);
    }
}
