package com.yiling.admin.data.center.report.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EntInfoPageListItemVO extends BaseVO {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;
}
