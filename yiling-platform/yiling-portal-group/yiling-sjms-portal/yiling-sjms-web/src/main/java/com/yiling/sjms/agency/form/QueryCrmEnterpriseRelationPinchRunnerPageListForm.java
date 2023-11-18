package com.yiling.sjms.agency.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseRelationPinchRunnerPageListForm extends QueryPageListForm {

    /**
     * 销量计入主管工号
     */
    @ApiModelProperty(value = "销量计入主管工号")
    private String businessSuperiorCode;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id")
    private Long crmEnterpriseId;

    /**
     * erp供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    @ApiModelProperty(value = "供应链角色")
    private Integer crmSupplyChainRole;

    /**
     * 最后操作时间-开始
     */
    @ApiModelProperty(value = "最后操作时间-开始")
    private Date opTimeStart;

    /**
     * 品种分类ID
     */
    @ApiModelProperty(value = "品种ID")
    private Long categoryId;

    /**
     * 辖区ID
     */
    @ApiModelProperty(value = "辖区ID")
    private String manorId;

    /**
     * 最后操作时间-结束
     */
    @ApiModelProperty(value = "最后操作时间-结束")
    private Date opTimeEnd;

}
