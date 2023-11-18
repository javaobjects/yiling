package com.yiling.open.erp.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpFlowSealedApi;
import com.yiling.open.erp.bo.ErpClientBO;
import com.yiling.open.erp.dto.ErpFlowSealedDTO;
import com.yiling.open.erp.dto.request.ErpFlowSealedLockOrUnlockRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowSealedRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedEnterprisePageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedPageRequest;
import com.yiling.open.erp.dto.request.QueryErpSealedSaveRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpFlowSealedService;
import com.yiling.open.erp.util.FlowUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向封存
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Slf4j
@DubboService
public class ErpFlowSealedApiImpl implements ErpFlowSealedApi {

    @Autowired
    private ErpFlowSealedService erpFlowSealedService;
    @Autowired
    private ErpClientService erpClientService;

    @Override
    public Page<ErpFlowSealedDTO> page(QueryErpSealedPageRequest request) {
        try {
            Page<ErpFlowSealedDTO> page = PojoUtils.map(erpFlowSealedService.page(request), ErpFlowSealedDTO.class);
            if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
                page.getRecords().forEach(p -> {
                    p.setMonth(FlowUtil.flowSealedMonthConvertChinese(p.getMonth()));
                });
            }

            return page;
        } catch (Exception e) {
            log.error("[ErpFlowSealedApiImpl][page] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpFlowSealedDTO> getByEidList(List<Long> eidList) {
        return PojoUtils.map(erpFlowSealedService.getByEidList(eidList), ErpFlowSealedDTO.class);
    }

    @Override
    public List<String> monthList(Integer count) {
        return FlowUtil.monthList(count);
    }

    @Override
    public Boolean save(QueryErpSealedSaveRequest request) {
        try {
            return erpFlowSealedService.save(request);
        } catch (Exception e) {
            log.error("[ErpFlowSealedApiImpl][save] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ErpFlowSealedDTO getErpFlowSealedByEidAndTypeAndMonth(Long id) {
        try {
            return PojoUtils.map(erpFlowSealedService.getErpFlowSealedById(id), ErpFlowSealedDTO.class);
        } catch (Exception e) {
            log.error("[ErpFlowSealedApiImpl][getById] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean lockOrUnLock(ErpFlowSealedLockOrUnlockRequest request) {
        try {
            return erpFlowSealedService.lockOrUnLock(request);
        } catch (Exception e) {
            log.error("[ErpFlowSealedApiImpl][getById] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ErpFlowSealedDTO getErpFlowSealedByEidAndTypeAndMonth(QueryErpFlowSealedRequest requerst) {
        try {
            return PojoUtils.map(erpFlowSealedService.getErpFlowSealedByEidAndTypeAndMonth(requerst), ErpFlowSealedDTO.class);
        } catch (Exception e) {
            log.error("[ErpFlowSealedApiImpl][getById] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
