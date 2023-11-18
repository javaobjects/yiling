package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpCustomerMapper;
import com.yiling.open.erp.dao.ErpGoodsCustomerPriceMapper;
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.erp.entity.ErpGoodsCustomerPriceDO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service
@Slf4j
public class ErpGoodsCustomerPriceHandler extends ErpServiceImpl {

    @Autowired(required = false)
    public ErpGoodsCustomerPriceService erpGoodsCustomerPriceService;

    @Autowired
    private ErpGoodsCustomerPriceMapper erpGoodsCustomerPriceMapper;

    @Resource(name = "erpGoodsCustomerPriceService")
    public ErpEntityService erpEntityService;


    public Boolean handleGoodsCustomerPriceMqSync(Long suId, String taskNo, List<ErpGoodsCustomerPriceDTO> erpGoodsCustomerPriceList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpGoodsCustomerPriceDTO erpGoodsCustomerPrice : erpGoodsCustomerPriceList) {
            ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO = PojoUtils.map(erpGoodsCustomerPrice, ErpGoodsCustomerPriceDO.class);
            entityList.add(erpGoodsCustomerPriceDO);
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
        ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO = (ErpGoodsCustomerPriceDO) baseErpEntity;
        ErpGoodsCustomerPriceDO tmpErpGoodsCustomerPriceDO = erpGoodsCustomerPriceMapper.selectById(erpGoodsCustomerPriceDO.getId());
        if (tmpErpGoodsCustomerPriceDO == null) {
            log.error("主键查询OP库信息空值,id=" + tmpErpGoodsCustomerPriceDO.getId());
            return false;
        }
        long start = System.currentTimeMillis();
        String lockName = this.getGoodsCustomerPriceLockName(tmpErpGoodsCustomerPriceDO.getSuId(), tmpErpGoodsCustomerPriceDO.getGcpIdNo());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            return erpGoodsCustomerPriceService.onlineData(tmpErpGoodsCustomerPriceDO);
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "ERP数据同步出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("ERP数据同步耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    public String getGoodsCustomerPriceLockName(Long suId, String gcpIdNo) {
        StringBuilder sb = new StringBuilder();
        sb.append("mph-erp-online-lock-erp-goodsCustomerPrice:").append(suId).append("-").append(gcpIdNo);
        return sb.toString();
    }

}
