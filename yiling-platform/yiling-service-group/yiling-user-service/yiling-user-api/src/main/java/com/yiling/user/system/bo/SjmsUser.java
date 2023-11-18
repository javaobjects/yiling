package com.yiling.user.system.bo;

import java.util.Date;

import lombok.Data;

/**
 * 神机妙算系统用户信息
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Data
public class SjmsUser extends BaseUser {

    private static final long serialVersionUID = 5091712035981948686L;

    /**
     * ESB人员工号
     */
    private String empId;

    /**
     * 是否为超级管理员：0-否 1-是
     */
    private Integer adminFlag;

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

    /**
     * 是否为超级管理员
     *
     * @return
     */
    public boolean isAdmin() {
        return this.getAdminFlag() == 1;
    }
}
