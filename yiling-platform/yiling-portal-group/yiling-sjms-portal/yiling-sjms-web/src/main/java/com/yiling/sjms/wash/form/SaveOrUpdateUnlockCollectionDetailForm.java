package com.yiling.sjms.wash.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateUnlockCollectionDetailForm extends BaseForm {

    private Long id;

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    /**
     * 采集价格
     */
    @NotNull(message = "采集价格不可为空")
    @ApiModelProperty(value = "采集价格")
    private BigDecimal collectionPrice;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 设置区域列表
     */
    @ApiModelProperty(value = "设置区域列表")
    private List<String> regionCodeList;
}
