package com.yiling.basic.contract.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentParamDTO extends BaseDTO {

    private static final long serialVersionUID = 7531595885398415838L;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 乙方名称
     */
    private String partybName;

    /**
     * 乙方地址
     */
    private String partybAdress;

    /**
     * 乙方电话
     */
    private String partybPhone;

    /**
     * 乙方传真
     */
    private String partybFax;

    /**
     * 乙方邮编
     */
    private String partybPostCode;

    /**
     * 总销售目标
     */
    private String saleTotalTarget;

    /**
     * 处方成长产品目标
     */
    private String growUpGoodsTarget;

    /**
     * 处方新产品目标
     */
    private String newGoodsTarget;

    /**
     * 连花产品目标
     */
    private String lhTarget;

    /**
     * OTC健康产品目标
     */
    private String otcTarget;

    /**
     * 指定购进商业1
     */
    private String purchaseEname1;

    /**
     * 指定购进商业2
     */
    private String purchaseEname2;

    /**
     * 指定购进商业3
     */
    private String purchaseEname3;

    /**
     * 其他支付方式
     */
    private String otherPayMode;

    /**
     * 选择支付方式
     */
    private String paymentFlag;

    /**
     * 乙方对接人姓名
     */
    private String partybOperatorName;

    /**
     * 乙方对接人职务
     */
    private String partybOperatorJob;

    /**
     * 乙方对接人电话
     */
    private String partybOperatorPhone;

    /**
     * 乙方对接人邮箱
     */
    private String partybOperatorMail;

    /**
     * 乙方收款方全称
     */
    private String partybPayeeName;

    /**
     * 乙方开户银行
     */
    private String partybBankName;

    /**
     * 乙方账号
     */
    private String partybAccountNum;

    /**
     * 乙方签字处
     */
    private String partybSignature;

    /**
     * 处方药成长品销售目标表
     */
    private List<GrowUpDTO> growUpTable;

    /**
     * 服务费用奖励标准和推荐级别表
     */
    private List<IncentiveStandardDTO> incentiveStandardTable;
}
