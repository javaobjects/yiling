package com.yiling.dataflow.sale;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.sale.service.SaleTargetService;
import org.junit.Test;

import javax.annotation.Resource;

public class SaleTargetServiceTest extends BaseTest {
    @Resource
    private SaleTargetService saleTargetService;
    @Test
    public void testRemoveId(){
        long id=0l;
        boolean b = saleTargetService.removeById(id);
    }
}
