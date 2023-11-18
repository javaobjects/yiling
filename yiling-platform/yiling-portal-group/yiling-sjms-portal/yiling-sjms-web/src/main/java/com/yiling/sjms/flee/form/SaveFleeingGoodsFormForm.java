package com.yiling.sjms.flee.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
public class SaveFleeingGoodsFormForm extends BaseForm {

    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 申报类型 1-电商 2-非电商
     */
    @ApiModelProperty("申报类型 1-电商 2-非电商")
    @NotNull(message = "申报类型不能为空")
    private Integer reportType;

    @ApiModelProperty("上传的窜货申诉信息")
    private List<SaveFleeingGoodsForm> saveFleeingGoodsList;
}
