package com.yiling.user.system.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户销售区域 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserSalesAreaDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 销售区域描述
     */
    private String description;

    /**
     * 销售区域是否全国：0-否 1-是
     */
    private Integer salesAreaAllFlag;

    /**
     * 销售区域Json串
     */
    private String jsonContent;


    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

}
