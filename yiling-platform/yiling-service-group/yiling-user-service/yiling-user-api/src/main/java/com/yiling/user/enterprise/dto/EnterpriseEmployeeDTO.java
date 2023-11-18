package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业员工信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseEmployeeDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 员工用户ID
     */
    private Long userId;

    /**
     * 工号
     */
    private String code;

    /**
     * 员工类型：1-商务代表 2-医药代表 100-其他
     */
    private Integer type;

    /**
     * 上级领导ID
     */
    private Long parentId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 是否为企业管理员：0-否 1-是
     */
    private Integer adminFlag;

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
