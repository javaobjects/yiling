package com.yiling.admin.system.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 EsWordVO
 * @描述
 * @创建时间 2022/4/27
 * @修改人 shichen
 * @修改时间 2022/4/27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EsWordVO extends BaseVO {
    /**
     * 词语
     */
    @ApiModelProperty("词语")
    private String word;
    /**
     * 扩展词类型 1：扩展词，2：停止词，3：单向同义词，4：双向同义词
     */
    @ApiModelProperty("1：扩展词，2：停止词，3：单向同义词，4：双向同义词")
    private Integer type;
    /**
     * 关联id
     */
    @ApiModelProperty("关联id")
    private Long refId;
    /**
     * 状态 0：正常 1：停用
     */
    @ApiModelProperty("0：正常 1：停用")
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
}
