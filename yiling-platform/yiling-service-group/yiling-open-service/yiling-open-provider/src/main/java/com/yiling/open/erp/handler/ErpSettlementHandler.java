package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpSettlementDTO;
import com.yiling.open.erp.entity.ErpSettlementDO;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpSettlementService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service
@Slf4j
public class ErpSettlementHandler extends ErpServiceImpl {

    @Autowired(required = false)
    public ErpSettlementService erpSettlementService;

    @Resource(name = "erpSettlementService")
    public ErpEntityService erpEntityService;

    public Boolean handleSettlementMqSync(Long suId, String taskNo, List<ErpSettlementDTO> erpSettlementList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpSettlementDTO erpSettlement : erpSettlementList) {
            ErpSettlementDO erpSettlementDO = PojoUtils.map(erpSettlement, ErpSettlementDO.class);
            entityList.add(erpSettlementDO);
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
        return erpSettlementService.onlineData(baseErpEntity);
    }


}
