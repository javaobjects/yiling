package com.yiling.admin.pop.navigation.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryNavigationInfoPageForm extends QueryPageListForm {
    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     *结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    /**
     * 状态1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer state;

    /**
     *导航名称
     */
    @ApiModelProperty(value = "导航名称")
    private String name;
    
}