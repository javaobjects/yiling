package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;

/**
 * @author: houjie.sun
 * @date: 2022/4/27
 */
public class ErpFlowGoodsConfigTest extends BaseTest {

    @Autowired
    private ErpFlowGoodsConfigService erpFlowGoodsConfigService;

    @Test
    public void getByIdTest() {
        Long id = 1L;
        erpFlowGoodsConfigService.getById(id);
    }

}
