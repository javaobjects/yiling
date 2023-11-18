package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业客户分组 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseCustomerGroupDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组描述
     */
    private String description;

    /**
     * 分组类型：1-平台创建 2-ERP同步
     */
    private Integer type;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

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

    /**
     * 备注
     */
    private String remark;
}
