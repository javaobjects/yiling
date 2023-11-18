package com.yiling.user.agreement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreement.dao.AgreementMultipleConditionSnapshotMapper;
import com.yiling.user.agreement.entity.AgreementMultipleConditionSnapshotDO;
import com.yiling.user.agreement.service.AgreementMultipleConditionSnapshotService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议多选条件表快照表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
@Slf4j
@Service
public class AgreementMultipleConditionSnapshotServiceImpl extends
                                                           BaseServiceImpl<AgreementMultipleConditionSnapshotMapper, AgreementMultipleConditionSnapshotDO>
                                                           implements AgreementMultipleConditionSnapshotService {

    @Override
    public Map<String, List<Integer>> getConditionValueByAgreementId(Long agreementId, Integer version) {
        if (ObjectUtil.isNull(agreementId) && ObjectUtil.isNull(version)) {
            return MapUtil.newHashMap();
        }
        LambdaQueryWrapper<AgreementMultipleConditionSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementMultipleConditionSnapshotDO::getAgreementId, agreementId).eq(AgreementMultipleConditionSnapshotDO::getVersion, version);
        List<AgreementMultipleConditionSnapshotDO> list = this.list(wrapper);
        return tranAgreementMultipleConditionMap(list);
    }

    private Map<String, List<Integer>> tranAgreementMultipleConditionMap(List<AgreementMultipleConditionSnapshotDO> list) {
        Map<String, List<Integer>> map = new HashMap<>();
        if (CollUtil.isEmpty(list)) {
            return map;
        }
        for (AgreementMultipleConditionSnapshotDO agreementMultipleConditionSnapshotDO : list) {
            if (map.containsKey(agreementMultipleConditionSnapshotDO.getConditionType())) {
                map.get(agreementMultipleConditionSnapshotDO.getConditionType()).add(agreementMultipleConditionSnapshotDO.getConditionValue());
            } else {
                List<Integer> ids = new ArrayList<>();
                ids.add(agreementMultipleConditionSnapshotDO.getConditionValue());
                map.put(agreementMultipleConditionSnapshotDO.getConditionType(), ids);
            }
        }
        return map;
    }
}
