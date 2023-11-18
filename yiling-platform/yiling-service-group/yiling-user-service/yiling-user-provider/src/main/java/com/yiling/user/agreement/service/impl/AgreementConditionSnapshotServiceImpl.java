package com.yiling.user.agreement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreement.dao.AgreementConditionSnapshotMapper;
import com.yiling.user.agreement.entity.AgreementConditionSnapshotDO;
import com.yiling.user.agreement.service.AgreementConditionSnapshotService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议返利条件快照表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
@Slf4j
@Service
public class AgreementConditionSnapshotServiceImpl extends BaseServiceImpl<AgreementConditionSnapshotMapper, AgreementConditionSnapshotDO>
                                                   implements AgreementConditionSnapshotService {

    @Override
    public List<AgreementConditionSnapshotDO> getAgreementConditionSnapshotByAgreementId(Long agreementId, Integer version) {
        if (ObjectUtil.isNull(agreementId) && ObjectUtil.isNull(version)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<AgreementConditionSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(agreementId), AgreementConditionSnapshotDO::getAgreementId, agreementId).eq(ObjectUtil.isNotNull(version),
            AgreementConditionSnapshotDO::getVersion, version);
        List<AgreementConditionSnapshotDO> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.toList();
        }
        return list;
    }

}
