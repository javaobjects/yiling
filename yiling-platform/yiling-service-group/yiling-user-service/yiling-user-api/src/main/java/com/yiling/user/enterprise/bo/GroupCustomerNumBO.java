package com.yiling.user.enterprise.bo;

import lombok.Data;

/**
 * 分组客户数量 BO
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
public class GroupCustomerNumBO {

    /**
     * 客户分组ID
     */
    private Long groupId;

    /**
     * 客户数量
     */
    private Long customerNum;
}
