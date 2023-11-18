package com.yiling.user.system.bo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 管理员信息带角色
 *
 * @author: lun.yu
 * @date: 2021/8/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdminRoleBO extends Admin {

    /**
     * 角色id
     */
    private List<Long> roleIdList;
}
