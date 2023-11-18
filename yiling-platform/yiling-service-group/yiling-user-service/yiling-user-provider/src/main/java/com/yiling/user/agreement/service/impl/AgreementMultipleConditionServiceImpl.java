package com.yiling.user.agreement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreement.dao.AgreementMultipleConditionMapper;
import com.yiling.user.agreement.entity.AgreementMultipleConditionDO;
import com.yiling.user.agreement.service.AgreementMultipleConditionService;

/**
 * <p>
 * 协议多选条件表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-06-21
 */
@Service
public class AgreementMultipleConditionServiceImpl extends BaseServiceImpl<AgreementMultipleConditionMapper, AgreementMultipleConditionDO> implements AgreementMultipleConditionService {

    @Override
    public List<Integer> getConditionValueByTypeAndAgreementId(Long agreementId, String type) {
        LambdaQueryWrapper<AgreementMultipleConditionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementMultipleConditionDO::getAgreementId, agreementId).eq(AgreementMultipleConditionDO::getConditionType, type);
        return this.list(wrapper).stream().map(e -> e.getConditionValue().intValue()).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Integer>> getConditionValueByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementMultipleConditionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementMultipleConditionDO::getAgreementId, agreementId);
        List<AgreementMultipleConditionDO> list = this.list(wrapper);
        return tranAgreementMultipleConditionMap(list);
    }

    @Override
    public Map<Long, Map<String, List<Integer>>> getConditionValueByAgreementId(List<Long> agreementIds) {
        LambdaQueryWrapper<AgreementMultipleConditionDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(AgreementMultipleConditionDO::getAgreementId, agreementIds);
        List<AgreementMultipleConditionDO> list = this.list(wrapper);
        Map<Long, List<AgreementMultipleConditionDO>> map=new HashMap<>();
        for(AgreementMultipleConditionDO agreementMultipleConditionDO:list){
            if (map.containsKey(agreementMultipleConditionDO.getAgreementId())) {
                map.get(agreementMultipleConditionDO.getAgreementId()).add(agreementMultipleConditionDO);
            } else {
                List<AgreementMultipleConditionDO> agreementMultipleConditionList = new ArrayList<>();
                agreementMultipleConditionList.add(agreementMultipleConditionDO);
                map.put(agreementMultipleConditionDO.getAgreementId(), agreementMultipleConditionList);
            }
        }

        Map<Long, Map<String, List<Integer>>> returnMap=new HashMap<>();
        for(Map.Entry<Long,List<AgreementMultipleConditionDO>> entry:map.entrySet()){
            Map<String,List<Integer>> tranMap= tranAgreementMultipleConditionMap(entry.getValue());
            returnMap.put(entry.getKey(),tranMap);
        }
        return returnMap;
    }

    private Map<String,List<Integer>>  tranAgreementMultipleConditionMap(List<AgreementMultipleConditionDO> list){
        Map<String, List<Integer>> map = new HashMap<>();
        for (AgreementMultipleConditionDO agreementMultipleConditionDO : list) {
            if (map.containsKey(agreementMultipleConditionDO.getConditionType())) {
                map.get(agreementMultipleConditionDO.getConditionType()).add(agreementMultipleConditionDO.getConditionValue());
            } else {
                List<Integer> ids = new ArrayList<>();
                ids.add(agreementMultipleConditionDO.getConditionValue());
                map.put(agreementMultipleConditionDO.getConditionType(), ids);
            }
        }
        return map;
    }
}
