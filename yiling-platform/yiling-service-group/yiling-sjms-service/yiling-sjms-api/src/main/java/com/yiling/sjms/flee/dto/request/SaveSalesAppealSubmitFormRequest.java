package com.yiling.sjms.flee.dto.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSalesAppealSubmitFormRequest extends BaseRequest {


    @NotNull(message = "提交表单信息不完整")
    private Long formId;

    /**
     * 申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他
     */
    private Integer appealType;

    /**
     * 调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货
     */
   private Integer monthAppealType;

    /**
     * 申诉金额
     */
    private BigDecimal appealAmount;

    /**
     * 申诉描述
     */
    private String appealDescribe;
    /**
     * 工号
     */
    private String empId;

    /**
     * 姓名
     */
    private String empName;

    /**
     * 附件
     */
    private List<SaveAppendixRequest> appendixList;

    /**
     * 附件
     */
    private List<UpdateSalesAppealRequest> updateSalesAppealForms;



    /**
     * 上传表单数据（为了兼容销售申诉二期 上传excel数据）
     */
    private List<SaveSalesAppealFormUploadRequest> saveSalesAppealDetailForms;

//    /**
//     * 选择流向数据
//     */
//    private List<SelectAppealFlowFormRequest> appealFlowDataDetailFormList;
    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    private Integer transferType;
}
