package com.yiling.dataflow.wash.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.wash.dao.ErpClientWashPlanMapper;
import com.yiling.dataflow.wash.dto.ErpClientWashPlanDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;
import com.yiling.dataflow.wash.entity.ErpClientWashPlanDO;
import com.yiling.dataflow.wash.service.ErpClientWashPlanService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * erp对接企业清洗计划表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-30
 */
@Service
public class ErpClientWashPlanServiceImpl extends BaseServiceImpl<ErpClientWashPlanMapper, ErpClientWashPlanDO> implements ErpClientWashPlanService {

    @Override
    public boolean generate(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList) {
        List<ErpClientWashPlanDO> list = PojoUtils.map(erpClientWashPlanList, ErpClientWashPlanDO.class);
        return this.saveBatch(list);
    }

    @Override
    public boolean updateByFmwcId(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList) {
        for (SaveOrUpdateErpClientWashPlanRequest saveOrUpdateErpClientWashPlanRequest : erpClientWashPlanList) {
            LambdaQueryWrapper<ErpClientWashPlanDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ErpClientWashPlanDO::getFmwcId, saveOrUpdateErpClientWashPlanRequest.getFmwcId());
            wrapper.eq(ErpClientWashPlanDO::getCrmEnterpriseId, saveOrUpdateErpClientWashPlanRequest.getCrmEnterpriseId());
            wrapper.eq(ErpClientWashPlanDO::getStatus, 1);
            wrapper.gt(ErpClientWashPlanDO::getPlanTime, DateUtil.parseDate("1970-01-01 00:00:00"));

            ErpClientWashPlanDO erpClientWashPlanDO = new ErpClientWashPlanDO();
            erpClientWashPlanDO.setPlanTime(saveOrUpdateErpClientWashPlanRequest.getPlanTime());
            erpClientWashPlanDO.setOpTime(new Date());
            this.update(erpClientWashPlanDO, wrapper);
        }
        return false;
    }

    @Override
    public List<ErpClientWashPlanDTO> findListByFmwcId(Long fmwcId) {
        LambdaQueryWrapper<ErpClientWashPlanDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ErpClientWashPlanDO::getFmwcId, fmwcId);
        wrapper.eq(ErpClientWashPlanDO::getStatus, 0);
        return PojoUtils.map(this.list(wrapper), ErpClientWashPlanDTO.class);
    }

    @Override
    public boolean updateById(SaveOrUpdateErpClientWashPlanRequest request) {
        ErpClientWashPlanDO erpClientWashPlanDO = PojoUtils.map(request, ErpClientWashPlanDO.class);
        return this.updateById(erpClientWashPlanDO);
    }
}
