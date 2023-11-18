package com.yiling.sjms.wash.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowTerminalOrderListForm extends BaseForm {

    /**
     * 所属月份
     */
    @NotBlank(message = "所属年月不能为空")
    @ApiModelProperty(value = "所属年月")
    private String gbDetailMonth;

    /**
     * 终端机构编码
     */
    @NotNull(message = "终端机构编码不能为空")
    @Min(1)
    @ApiModelProperty(value = "终端机构编码")
    private Long crmEnterpriseId;

    /**
     * 终端机构名称
     */
    @NotBlank(message = "终端机构名称不能为空")
    @ApiModelProperty(value = "终端机构名称")
    private String name;

    /**
     * 经销商所属省份名称
     */
    @ApiModelProperty(value = "经销商所属省份名称")
    private String crmProvinceName;

    /**
     * 经销商所属省份编码
     */
    @ApiModelProperty(value = "经销商所属省份编码")
    private String crmProvinceCode;

    /**
     * 保存列表
     */
    @NotEmpty(message = "保存列表不能为空")
    @ApiModelProperty(value = "保存列表")
    private List<SaveFlowTerminalOrderForm> saveList;

}
