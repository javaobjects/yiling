package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询员工账号分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStaffPageListRequest extends QueryPageListRequest {

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 企业类型
     */
    private Integer etype;
}

