package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.erp.service.ErpSettlementService;

public class ErpSettlementTest extends BaseTest {

    @Autowired
    private ErpSettlementService erpSettlementService;

//    @Autowired
//    private ErpGoodsDao erpGoodsDao;

    @Test
    public void ErpSettlementTest() {
        erpSettlementService.syncSettlement();
    }

}
