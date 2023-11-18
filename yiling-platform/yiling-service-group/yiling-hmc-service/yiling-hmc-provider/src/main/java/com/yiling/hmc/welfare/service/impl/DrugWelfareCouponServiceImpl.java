package com.yiling.hmc.welfare.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.dao.DrugWelfareCouponMapper;
import com.yiling.hmc.welfare.dto.request.DrugWelfareCouponUpdateRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareCouponDO;
import com.yiling.hmc.welfare.service.DrugWelfareCouponService;

/**
 * <p>
 * 药品福利券包表 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Service
public class DrugWelfareCouponServiceImpl extends BaseServiceImpl<DrugWelfareCouponMapper, DrugWelfareCouponDO> implements DrugWelfareCouponService {

    @Override
    public List<DrugWelfareCouponDO> getByWelfareId(Long id) {
        QueryWrapper<DrugWelfareCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareCouponDO::getDrugWelfareId, id);
        return this.list(queryWrapper);
    }
    @Override
    public List<DrugWelfareCouponDO> queryByDrugWelfareId(Long drugWelfareId) {
        QueryWrapper<DrugWelfareCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareCouponDO::getDrugWelfareId, drugWelfareId);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean updateDrugWelfareCoupon(List<DrugWelfareCouponUpdateRequest> requestList) {
        List<DrugWelfareCouponDO> drugWelfareCouponDOList = PojoUtils.map(requestList, DrugWelfareCouponDO.class);
        return this.saveOrUpdateBatch(drugWelfareCouponDOList);
    }
}
