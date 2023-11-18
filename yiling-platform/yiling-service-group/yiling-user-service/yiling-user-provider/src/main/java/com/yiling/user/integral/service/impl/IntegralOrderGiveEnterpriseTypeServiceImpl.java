package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseTypeDO;
import com.yiling.user.integral.dao.IntegralOrderGiveEnterpriseTypeMapper;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseTypeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 订单送积分-指定企业类型表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Service
public class IntegralOrderGiveEnterpriseTypeServiceImpl extends BaseServiceImpl<IntegralOrderGiveEnterpriseTypeMapper, IntegralOrderGiveEnterpriseTypeDO> implements IntegralOrderGiveEnterpriseTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEnterpriseType(Long giveRuleId, List<Integer> enterpriseTypeList, Long opUserId) {
        // 删除存在的企业类型
        IntegralOrderGiveEnterpriseTypeDO enterpriseTypeDO = new IntegralOrderGiveEnterpriseTypeDO();
        enterpriseTypeDO.setOpUserId(opUserId);
        LambdaQueryWrapper<IntegralOrderGiveEnterpriseTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveEnterpriseTypeDO::getGiveRuleId, giveRuleId);
        this.batchDeleteWithFill(enterpriseTypeDO, wrapper);
        // 添加企业类型
        if (CollUtil.isNotEmpty(enterpriseTypeList)) {
            List<IntegralOrderGiveEnterpriseTypeDO> enterpriseTypeDOList = enterpriseTypeList.stream().map(enterpriseType -> {
                IntegralOrderGiveEnterpriseTypeDO giveEnterpriseTypeDO = new IntegralOrderGiveEnterpriseTypeDO();
                giveEnterpriseTypeDO.setGiveRuleId(giveRuleId);
                giveEnterpriseTypeDO.setEnterpriseType(enterpriseType);
                giveEnterpriseTypeDO.setOpUserId(opUserId);
                return giveEnterpriseTypeDO;
            }).collect(Collectors.toList());

            this.saveBatch(enterpriseTypeDOList);
        }

        return true;
    }

    @Override
    public List<Integer> getEnterpriseTypeList(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderGiveEnterpriseTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveEnterpriseTypeDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper).stream().map(IntegralOrderGiveEnterpriseTypeDO::getEnterpriseType).distinct().collect(Collectors.toList());
    }

}
