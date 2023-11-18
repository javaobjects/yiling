package com.yiling.sjms.crm.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmDepartmentGoodsGroupVO
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmDepartmentGoodsGroupVO extends BaseVO {

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 产品组
     */
    @ApiModelProperty(value = "产品组")
    private String productGroup;

    /**
     * 产品组id
     */
    @ApiModelProperty(value = "产品组id")
    private Long productGroupId;


    /**
     * 是否有数据引用
     */
    @ApiModelProperty(value = "是否有数据引用")
    private Boolean isDataUse;
}
