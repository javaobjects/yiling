package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpFlowGoodsConfigApi;
import com.yiling.open.erp.dao.ErpFlowGoodsConfigMapper;
import com.yiling.open.erp.dto.ErpFlowGoodsConfigDTO;
import com.yiling.open.erp.dto.request.DeleteErpFlowGoodsConfigRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowGoodsConfigPageRequest;
import com.yiling.open.erp.dto.request.SaveErpFlowGoodsConfigRequest;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;
import com.yiling.open.erp.service.ErpFlowGoodsConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Slf4j
@DubboService
public class ErpFlowGoodsConfigApiImpl implements ErpFlowGoodsConfigApi {

    @Autowired
    private ErpFlowGoodsConfigService erpFlowGoodsConfigService;
    @Autowired
    private ErpFlowGoodsConfigMapper erpFlowGoodsConfigMapper;

    @Override
    public Page<ErpFlowGoodsConfigDTO> page(QueryErpFlowGoodsConfigPageRequest request) {
        try {
            return PojoUtils.map(erpFlowGoodsConfigService.page(request), ErpFlowGoodsConfigDTO.class);
        } catch (Exception e) {
            log.error("[ErpFlowGoodsConfigApiImpl][page] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean save(SaveErpFlowGoodsConfigRequest request) {
        try {
            return erpFlowGoodsConfigService.save(request);
        } catch (Exception e) {
            log.error("[ErpFlowGoodsConfigApiImpl][save] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ErpFlowGoodsConfigDTO getByEidAndGoodsInSn(Long eid, String goodsInSn) {
        try {
            return PojoUtils.map(erpFlowGoodsConfigService.getByEidAndGoodsInSn(eid, goodsInSn), ErpFlowGoodsConfigDTO.class);
        } catch (Exception e) {
            log.error("[ErpFlowGoodsConfigApiImpl][getByEidAndGoodsInSn] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ErpFlowGoodsConfigDTO getById(Long id) {
        Assert.notNull(id, "id不能为空");
        try {
            return PojoUtils.map(erpFlowGoodsConfigService.getById(id), ErpFlowGoodsConfigDTO.class);
        } catch (Exception e) {
            log.error("[ErpFlowGoodsConfigApiImpl][getById] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean deleteById(DeleteErpFlowGoodsConfigRequest request) {
        Assert.notNull(request, "参数不能为空");
        Assert.notNull(request.getId(), "id不能为空");
        Assert.notNull(request.getEid(), "eid不能为空");
        try {
            int count = erpFlowGoodsConfigService.deleteFlowGoodsConfigById(request);
            if (count == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("[ErpFlowGoodsConfigApiImpl][deleteById] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

}
