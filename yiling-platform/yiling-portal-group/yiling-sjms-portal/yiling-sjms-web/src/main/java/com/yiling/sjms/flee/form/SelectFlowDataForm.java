package com.yiling.sjms.flee.form;


import java.math.BigDecimal;
import java.util.List;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选择流向字段
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
public class SelectFlowDataForm  extends BaseForm {

//    @Valid
//    @ApiModelProperty("选择流向数据")
//    private AppealFlowDataDetailForm appealFlowDataDetailForm;

    @ApiModelProperty("formId-不是第一次添加的时候带上")
    private Long formId;
    /**
     * 申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货
     * 9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误
     */
    @ApiModelProperty(value = "申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货 " +
            "9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误")
    private Integer appealType;

    /**
     * 传输方式 2、选择流向
     */
    @ApiModelProperty("传输方式 2、选择流向")
    private Integer transferType;

    @ApiModelProperty("选择流向数据确认")
    private List<AppealConfirmFlowDataDetailForm> appealFlowDataDetailFormList;

    @ApiModelProperty(value = "申诉数量")
//    @NotNull(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写", groups = { SaveOperateGroup.class})
    private BigDecimal appealFinalQuantity;


    @ApiModelProperty("根据变化类型变化的机构名称编码、产品名称编码、机构属性编码")
//    @NotNull(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写", groups = { SaveOperateGroup.class})
    private Long changeCode;

    @ApiModelProperty("根据申诉类型变化的机构名称、产品名称、机构属性字段")
//    @NotEmpty(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写", groups = { SaveOperateGroup.class})
    private String changeName;

    @ApiModelProperty("根据申诉类型变化的变化类型：1、标准机构名称 2、标准产品名称 3、终端类型")
//    @NotNull(message = "申诉数量/标准客户名称/标准产品名称/机构属性未填写", groups = { SaveOperateGroup.class})
    private Integer changeType;

    /**
     * 申诉后产品品规
     */
    @ApiModelProperty("申诉后产品品规")
    private String appealGoodsSpec;
    /**
     * 选择流向id
     */
    @ApiModelProperty("选择流向id")
    private Long id;

}
