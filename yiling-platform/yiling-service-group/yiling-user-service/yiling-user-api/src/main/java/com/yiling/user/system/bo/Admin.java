package com.yiling.user.system.bo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 管理员信息
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Admin extends BaseUser {

    private static final long serialVersionUID = 5776721698126681531L;

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
