package com.yiling.user.system.dto.request;

import lombok.Data;

/**
 * 查询员工账号列表 Request
 *
 * @author: xuan.zhou
 * @date: 2022/9/26
 */
@Data
public class QueryStaffListRequest implements java.io.Serializable {

    private static final long serialVersionUID = 6402000251379425660L;

    /**
     * 姓名精准查询
     */
    private String nameEq;

    /**
     * 姓名模糊查询
     */
    private String nameLike;

    /**
     * 员工账号状态等于，参见 UserStatusEnum 枚举
     */
    private Integer statusEq;

    /**
     * 员工账号状态不等于，参见 UserStatusEnum 枚举
     */
    private Integer statusNe;
}
