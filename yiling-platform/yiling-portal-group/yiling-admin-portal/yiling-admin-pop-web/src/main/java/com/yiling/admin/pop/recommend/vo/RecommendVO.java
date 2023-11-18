package com.yiling.admin.pop.recommend.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * banner分页列表 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * Recommend标题
     */
    @ApiModelProperty(value = "Recommend标题")
    private String title;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

}
