package com.yiling.user.system.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAdminPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 4902284484551285170L;

    /**
     * 用户名（全模糊查询）
     */
    private String username;

    /**
     * 姓名（全模糊查询）
     */
    private String name;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;

    /**
     * 创建时间-起
     */
    private Date beginTime;

    /**
     * 创建时间-止
     */
    private Date endTime;

}
