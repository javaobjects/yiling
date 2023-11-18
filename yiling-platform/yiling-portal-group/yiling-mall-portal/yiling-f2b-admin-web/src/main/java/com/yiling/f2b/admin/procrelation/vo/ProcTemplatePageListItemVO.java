package com.yiling.f2b.admin.procrelation.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 以岭客户
 *
 * @author: dexi.yao
 * @date: 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcTemplatePageListItemVO extends BaseVO {

    /**
     * 模板编号
     */
    @ApiModelProperty("模板编号")
    private String templateNumber;

    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 修改人
     */
    @JsonIgnore
    @ApiModelProperty(value = "操作人",hidden = true)
    private Long updateUser;

    /**
     * 修改人
     */
    @ApiModelProperty("操作人")
    private String updateUserStr;

    /**
     * 修改时间
     */
    @ApiModelProperty("操作时间")
    private Date updateTime;

}
