package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询员工分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEmployeePageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名（全模糊查询）
     */
    private String name;

    /**
     * 工号
     */
    private String code;

    /**
     * 员工类型：1-商务代表 2-医药代表 100-其他
     */
    private Integer type;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;
}
