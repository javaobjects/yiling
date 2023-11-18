package com.yiling.sjms.crm.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateCrmGoodsGroupForm
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmGoodsGroupForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 产品组名称
     */
    @NotEmpty(message = "")
    @ApiModelProperty(value = "产品组名称")
    private String name;

    /**
     * 产品组状态
     */
    @ApiModelProperty(value = "产品组状态 0 有效 1无效")
    private Integer status;

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "关联商品")
    private List<SaveOrUpdateCrmGoodsRelationForm> goodsRelationList;

    /**
     * 关联部门
     */
    @ApiModelProperty(value = "关联部门")
    private List<SaveOrUpdateCrmDepartProductGroupForm> departmentRelationList;

}
