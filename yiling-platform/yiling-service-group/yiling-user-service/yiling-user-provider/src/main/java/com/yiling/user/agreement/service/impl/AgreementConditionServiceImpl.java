package com.yiling.user.agreement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementConditionMapper;
import com.yiling.user.agreement.entity.AgreementConditionDO;
import com.yiling.user.agreement.service.AgreementConditionService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 协议返利条件 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-06-21
 */
@Service
public class AgreementConditionServiceImpl extends BaseServiceImpl<AgreementConditionMapper, AgreementConditionDO> implements AgreementConditionService {

    @Override
    public Map<Long, List<AgreementConditionDO>> getAgreementConditionListByAgreementId(List<Long> agreementIds) {
        LambdaQueryWrapper<AgreementConditionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(AgreementConditionDO::getAgreementId,agreementIds);

        List<AgreementConditionDO> list = this.list(wrapper);
        Map<Long, List<AgreementConditionDO>> map = new HashMap<>();
        List<AgreementConditionDO> agreementConditionDOList = null;
        for (AgreementConditionDO agreementConditionDO : list) {
            if (map.containsKey(agreementConditionDO.getAgreementId())) {
                agreementConditionDOList = map.get(agreementConditionDO.getAgreementId());
            } else {
                agreementConditionDOList = new ArrayList<>();
                map.put(agreementConditionDO.getAgreementId(), agreementConditionDOList);
            }
            agreementConditionDOList.add(agreementConditionDO);
        }
        return map;
    }

    @Override
    public List<AgreementConditionDO> queryBuyConditionByAgreementId(List<Long> agreementIds) {
        if (CollUtil.isEmpty(agreementIds)){
            return ListUtil.toList();
        }
        LambdaQueryWrapper<AgreementConditionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(AgreementConditionDO::getAgreementId,agreementIds);

        List<AgreementConditionDO> list = this.list(wrapper);
        return list;
    }
}
