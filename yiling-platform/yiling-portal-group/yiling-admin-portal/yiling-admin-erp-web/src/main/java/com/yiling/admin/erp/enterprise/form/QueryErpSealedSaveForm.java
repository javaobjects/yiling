package com.yiling.admin.erp.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpSealedSaveForm extends BaseForm {

    /**
     * 商业ID
     */
    @NotEmpty
    @ApiModelProperty(value = "商业ID")
    private List<Long> eidList;

    /**
     * 流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部
     */
    @NotEmpty
    @ApiModelProperty(value = "流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 3-连锁纯销流向 0-全部")
    private List<Integer> typeList;

    /**
     * 封存月份, 仅支持前推6个整月
     */
    @NotEmpty
    @ApiModelProperty(value = "封存月份, 仅支持前推6个整月")
    private List<String> monthList;

}
