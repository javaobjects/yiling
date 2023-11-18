package com.yiling.f2b.admin.procrelation.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcRelationModifyRecordVO extends BaseVO {

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private Integer version;

    /**
     * 版本id全局唯一，用于其他业务关联
     */
    @ApiModelProperty("版本id")
    private String versionId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
}
