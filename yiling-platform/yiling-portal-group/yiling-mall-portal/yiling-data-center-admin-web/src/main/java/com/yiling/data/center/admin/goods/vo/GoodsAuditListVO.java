package com.yiling.data.center.admin.goods.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
public class GoodsAuditListVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "待审核主键")
    private Long id;

    /**
     * 审核人员
     */
    @ApiModelProperty(value = "审核人员")
    private String auditUser;

    /**
     * 审核人员
     */
    private Long updateUser;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date updateTime;

    /**
     * 审核状态：0待审核1审核通过2审核不通3忽略
     */
    @ApiModelProperty(value = "审核状态：0待审核1审核通过2审核不通3忽略")
    private Integer auditStatus;

    /**
     * 驳回信息
     */
    @ApiModelProperty(value = "驳回信息")
    private String rejectMessage;
}
