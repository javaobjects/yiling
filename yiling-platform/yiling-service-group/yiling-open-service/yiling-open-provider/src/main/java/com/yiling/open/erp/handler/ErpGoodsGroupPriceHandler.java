package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.entity.ErpGoodsGroupPriceDO;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpGoodsGroupPriceService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service
@Slf4j
public class ErpGoodsGroupPriceHandler extends ErpServiceImpl {

    @Autowired(required = false)
    public ErpGoodsGroupPriceService erpGoodsGroupPriceService;

    @Resource(name = "erpGoodsGroupPriceService")
    public ErpEntityService erpEntityService;


    public Boolean handleGoodsGroupPriceMqSync(Long suId, String taskNo, List<ErpGoodsGroupPriceDTO> erpGoodsGroupPriceList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpGoodsGroupPriceDTO erpGoodsGroupPrice : erpGoodsGroupPriceList) {
            ErpGoodsGroupPriceDO erpGoodsGroupPriceDO = PojoUtils.map(erpGoodsGroupPrice, ErpGoodsGroupPriceDO.class);
            entityList.add(erpGoodsGroupPriceDO);
        }
        long start = System.currentTimeMillis();
        boolean bool = this.syncBaseData(suId, taskNo, entityList);
        long time = System.currentTimeMillis() - start;
        if (time > 3000) {
            log.warn("ERP数据同步耗时:{}ms,suId:{},taskNo:{}", time, suId, taskNo);
        }
        return bool;
    }

    @Override
    public ErpEntityService getErpEntityManager() {
        return erpEntityService;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        //todo 需要加上分布式锁 到最细
        return erpGoodsGroupPriceService.onlineData(baseErpEntity);
    }


}
