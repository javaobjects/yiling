package com.yiling.sjms.flee.form;


import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sjms.flee.vo.AppendixDetailVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shixing.sun
 * @date: 2023/2/25 0025
 */
@Data
public class SubmitSalesAppealForm extends BaseForm {

    @ApiModelProperty("父表单id")
    @NotNull(message = "提交表单信息不完整")
    private Long formId;

    /**
     * 申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他
     */
    @ApiModelProperty("申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货 " +
            "9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误")
    private Integer appealType;

    /**
     * 申诉金额
     */
    @ApiModelProperty("申诉金额")
    private BigDecimal appealAmount;

    /**
     * 申诉描述
     */
    @ApiModelProperty(value = "申诉描述")
    private String appealDescribe;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<AppendixDetailVO> appendixList;

    @ApiModelProperty(value = "更新表单信息")
    private List<UpdateSalesAppealForm>updateSalesAppealForms;

    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    @ApiModelProperty(value = "传输类型：1、上传excel; 2、选择流向")
    private Integer transferType;

//    @Valid
//    @ApiModelProperty(value = "选择流向的表单数据")
//    private List<AppealFlowDataDetailForm> appealFlowDataDetailFormList;
}
