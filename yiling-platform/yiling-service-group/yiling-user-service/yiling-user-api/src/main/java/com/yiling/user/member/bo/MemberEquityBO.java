package com.yiling.user.member.bo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-权益DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberEquityBO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 权益名称
     */
    private String name;

    /**
     * 权益类型
     */
    private Integer type;

    /**
     * 权益图标
     */
    private String icon;

    /**
     * 权益说明
     */
    private String description;

    /**
     * 权益状态：0-关闭 1-开启
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 更新时间
     */
    private Date updateTime;


}
