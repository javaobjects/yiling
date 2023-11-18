package com.yiling.admin.pop.hotWords.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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
public class GoodsHotWordsVO extends BaseVO {
    /**
     * 热词名称
     */
    @ApiModelProperty(value = "热词名称")
    private String name;

    /**
     * 状态 1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer state;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 投放开始时间
     */
    @ApiModelProperty(value = "投放开始时间")
    private Date startTime;

    /**
     * 投放结束时间
     */
    @ApiModelProperty(value = "投放结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
