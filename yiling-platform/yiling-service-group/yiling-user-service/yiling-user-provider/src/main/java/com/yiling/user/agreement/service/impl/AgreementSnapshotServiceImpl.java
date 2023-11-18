package com.yiling.user.agreement.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementSnapshotMapper;
import com.yiling.user.agreement.dto.AgreementConditionDTO;
import com.yiling.user.agreement.dto.AgreementPolicyDTO;
import com.yiling.user.agreement.dto.AgreementSnapshotDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.entity.AgreementConditionSnapshotDO;
import com.yiling.user.agreement.entity.AgreementSnapshotDO;
import com.yiling.user.agreement.enums.AgreementConditionRuleEnum;
import com.yiling.user.agreement.enums.AgreementMultipleConditionEnum;
import com.yiling.user.agreement.service.AgreementConditionSnapshotService;
import com.yiling.user.agreement.service.AgreementMultipleConditionSnapshotService;
import com.yiling.user.agreement.service.AgreementSnapshotService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 协议快照表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
@Service
public class AgreementSnapshotServiceImpl extends BaseServiceImpl<AgreementSnapshotMapper, AgreementSnapshotDO> implements AgreementSnapshotService {

    @Autowired
    AgreementConditionSnapshotService         agreementConditionSnapshotService;
    @Autowired
    AgreementMultipleConditionSnapshotService agreementMultipleConditionSnapshotService;

    @Override
    public AgreementSnapshotDTO queryAgreementSnapshotById(Long id) {
        Assert.notNull(id, "协议快照id不能为空");
        return PojoUtils.map(getById(id), AgreementSnapshotDTO.class);
    }

    @Override
    public List<AgreementSnapshotDTO> queryAgreementSnapshotByAgreementId(Long agreementId) {
        Assert.notNull(agreementId, "协议快照的协议id不能为空");
        LambdaQueryWrapper<AgreementSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementSnapshotDO::getAgreementId, agreementId);
        List<AgreementSnapshotDO> list = list(wrapper);
        return PojoUtils.map(list, AgreementSnapshotDTO.class);
    }

    @Override
    public SupplementAgreementDetailDTO querySupplementAgreementsDetailById(Long id) {
        // 查询协议快照
        AgreementSnapshotDO agreementSnapshotDO = getById(id);
        Long agreementId = agreementSnapshotDO.getAgreementId();
        Integer version = agreementSnapshotDO.getVersion();
        // 查询协议返利条件快照
        List<AgreementConditionSnapshotDO> conditionSnapshotDos = agreementConditionSnapshotService
            .getAgreementConditionSnapshotByAgreementId(agreementId, version);
        // 查询协议多选条件表快照
        Map<String, List<Integer>> multipleMap = agreementMultipleConditionSnapshotService.getConditionValueByAgreementId(agreementId, version);

        // 协议版本详情，组装数据
        SupplementAgreementDetailDTO dto = PojoUtils.map(agreementSnapshotDO, SupplementAgreementDetailDTO.class);
        if (CollUtil.isNotEmpty(conditionSnapshotDos)) {
            // 协议下条件
            List<AgreementConditionDTO> conditionDTOList = PojoUtils.map(conditionSnapshotDos, AgreementConditionDTO.class);
            // 判断是否有多选的情况
            {
                if (dto.getRestitutionType().equals(1)) {
                    dto.setRestitutionTypeValues(multipleMap.get(AgreementMultipleConditionEnum.RESTITUTION_TYPE.getCode()));
                }
                if (conditionSnapshotDos.get(0).getPayType().equals(1)) {
                    conditionDTOList.forEach(e -> e.setPayTypeValues(multipleMap.get(AgreementMultipleConditionEnum.PAY_TYPE.getCode())));
                }
                if (conditionSnapshotDos.get(0).getBackAmountType().equals(1)) {
                    conditionDTOList
                        .forEach(e -> e.setBackAmountTypeValues(multipleMap.get(AgreementMultipleConditionEnum.BACK_AMOUNT_TYPE.getCode())));
                }
            }
            // 如果条件规则类型为梯度
            if (ObjectUtil.equal(AgreementConditionRuleEnum.GRADIENT.getCode(), agreementSnapshotDO.getConditionRule())) {
                List<AgreementPolicyDTO> policyDTOS = PojoUtils.map(conditionSnapshotDos, AgreementPolicyDTO.class);
                dto.setPolicys(policyDTOS);
            } else {
                AgreementPolicyDTO policyDTO = PojoUtils.map(conditionSnapshotDos.get(0), AgreementPolicyDTO.class);
                dto.setPolicys(ListUtil.toList(policyDTO));
            }
            dto.setAgreementsConditionList(conditionDTOList);
        }
        return dto;
    }

}
