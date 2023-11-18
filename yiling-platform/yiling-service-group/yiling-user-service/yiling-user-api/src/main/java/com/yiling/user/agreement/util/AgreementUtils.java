package com.yiling.user.agreement.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.yiling.user.agreement.dto.AgreementConditionDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.enums.AgreementChildTypeEnum;
import com.yiling.user.agreement.enums.AgreementConditionRuleEnum;
import com.yiling.user.agreement.enums.AgreementRebateCycleEnum;
import com.yiling.user.agreement.enums.AgreementTypeEnum;

import cn.hutool.core.util.ObjectUtil;

/**
 * 协议工具类
 *
 * @author: shuang.zhang
 * @date: 2021/7/6
 */
public class AgreementUtils {

    /**
     * 协议对象
     *
     * @param agreement 协议对象
     * @return
     */
    public static String getAgreementText(SupplementAgreementDetailDTO agreement) {
        // 过滤掉主协议，主协议的parentId=0 并且主协议没有入账的协议类型等
        if (ObjectUtil.isNull(agreement.getParentId()) || agreement.getParentId() <= 0) {
            return null;
        }
        List<String> con = new ArrayList<>();
        StringBuffer sb = new StringBuffer("协议类型:").append(AgreementTypeEnum.getByCode(agreement.getType()).getName()).append("-")
                .append(AgreementChildTypeEnum.getByCode(agreement.getChildType()).getName());
        sb.append(",").append(AgreementConditionRuleEnum.getByCode(agreement.getConditionRule()).getName());
        //协议条件
        List<AgreementConditionDTO> conditionDetailDTOList = agreement.getAgreementsConditionList();
        Calendar cal = Calendar.getInstance();
        if (agreement.getConditionRule().equals(AgreementConditionRuleEnum.GRADIENT.getCode())) {
            sb.append("(");
            for (AgreementConditionDTO conditionDetailDTO : conditionDetailDTOList) {
                sb.append(conditionDetailDTO.getMixValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("-")
                        .append(conditionDetailDTO.getMaxValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append(":")
                        .append(conditionDetailDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%;");
            }
            sb.append(")");
        } else if (agreement.getConditionRule().equals(AgreementConditionRuleEnum.MONTHLY.getCode())) {
            int month = cal.get(Calendar.MONTH) + 1;
            for (AgreementConditionDTO conditionDetailDTO : conditionDetailDTOList) {
                if (conditionDetailDTO.getRangeNo().equals(month)) {
                    sb.append("-").append(conditionDetailDTO.getRangeNo() + "(")
                            .append(conditionDetailDTO.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)).append(")");
//                        .append(",").append(AgreementPolicyTypeEnum.getByCode(conditionDetailDTO.getPolicyType()).getName())
//                        .append(conditionDetailDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
                }
            }
        } else if (agreement.getConditionRule().equals(AgreementConditionRuleEnum.SEASONS.getCode())) {
            int month = cal.get(Calendar.MONTH) + 1;
            int quarter = month % 3 == 0 ? month / 3 : month / 3 + 1;
            for (AgreementConditionDTO conditionDetailDTO : conditionDetailDTOList) {
                if (conditionDetailDTO.getRangeNo().equals(quarter)) {
                    sb.append("-").append(conditionDetailDTO.getRangeNo() + "(")
                            .append(conditionDetailDTO.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)).append(")");
//                        .append(",").append(AgreementPolicyTypeEnum.getByCode(conditionDetailDTO.getPolicyType()).getName())
//                        .append(conditionDetailDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
                }
            }
        } else if (agreement.getConditionRule().equals(AgreementConditionRuleEnum.CONFIRM_DATA.getCode())) {
            AgreementConditionDTO conditionDetailDTO = conditionDetailDTOList.get(0);
            sb.append("(").append(conditionDetailDTO.getTimeNode()).append(")");
//                .append(",").append(AgreementPolicyTypeEnum.getByCode(conditionDetailDTO.getPolicyType()).getName())
//                .append(conditionDetailDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
        } else if (agreement.getConditionRule().equals(AgreementConditionRuleEnum.TOTAL_AMOUNT.getCode())) {
            AgreementConditionDTO conditionDetailDTO = conditionDetailDTOList.get(0);
            sb.append("(").append(conditionDetailDTO.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)).append(")");
//                .append(",").append(AgreementPolicyTypeEnum.getByCode(conditionDetailDTO.getPolicyType()).getName())
//                .append(conditionDetailDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
        } else {
            AgreementConditionDTO conditionDetailDTO = conditionDetailDTOList.get(0);
            sb.append("(协议商品采购实际总额)");
//                    .append(",").append(AgreementPolicyTypeEnum.getByCode(conditionDetailDTO.getPolicyType()).getName())
//                    .append(conditionDetailDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append("%");
        }
        sb.append(",返利周期(").append(AgreementRebateCycleEnum.getByCode(agreement.getRebateCycle()).getName()).append(")");
        return sb.toString();
    }
}
