package com.yiling.b2b.app.common.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 热词管理表
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppHotWordsVO extends BaseVO {

    @ApiModelProperty(value = "热词名称")
    private String content;

    @ApiModelProperty(value = "启用状态：0-启用 1-停用")
    private Integer useStatus;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date stopTime;

    @ApiModelProperty(value = "排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    private Integer sort;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
