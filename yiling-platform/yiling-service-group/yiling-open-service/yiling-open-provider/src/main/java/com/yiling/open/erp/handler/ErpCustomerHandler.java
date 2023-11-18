package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpCustomerMapper;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.entity.ErpCustomerDO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.service.ErpCustomerService;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户信息消息处理类
 *
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@Service
public class ErpCustomerHandler extends ErpServiceImpl {

    @Autowired
    public ErpCustomerService erpCustomerService;

    @Autowired
    private ErpCustomerMapper erpCustomerMapper;

    @Resource(name = "erpCustomerService")
    public ErpEntityService erpEntityService;

    public Boolean handleCustomerMqSync(Long suId, String taskNo, List<ErpCustomerDTO> erpCustomerList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpCustomerDTO erpCustomer : erpCustomerList) {
            ErpCustomerDO erpCustomerDO = PojoUtils.map(erpCustomer, ErpCustomerDO.class);
            entityList.add(erpCustomerDO);
        }

        long start = System.currentTimeMillis();
        boolean bool = this.syncBaseData(suId, taskNo, entityList);
        long time = System.currentTimeMillis() - start;
        if (time > 3000) {
            log.warn("ERP数据同步耗时:{}ms, suId:{}, taskNo:{}", time, suId, taskNo);
        }
        return bool;
    }

    @Override
    public ErpEntityService getErpEntityManager() {
        return erpEntityService;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpCustomerDO erpCustomerDO = (ErpCustomerDO) baseErpEntity;
        ErpCustomerDO tmpErpCustomerDO = erpCustomerMapper.selectById(erpCustomerDO.getId());
        if (tmpErpCustomerDO == null) {
            log.error("主键查询OP库信息空值,id=" + erpCustomerDO.getId());
            return false;
        }
        long start = System.currentTimeMillis();
        String lockName = this.getCustomerLockName(tmpErpCustomerDO.getSuId(), tmpErpCustomerDO.getInnerCode());
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            return erpCustomerService.onlineData(baseErpEntity);
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

    public String getCustomerLockName(Long suId, String innerCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("mph-erp-online-lock-erp-customer:").append(suId).append("-").append(innerCode);
        return sb.toString();
    }
}
