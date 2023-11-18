package com.yiling.user.system.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询个人完善信息审核分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStaffExternaAuditPageListRequest extends QueryPageListRequest {

    /**
     * 姓名（模糊查询）
     */
    private String name;

    /**
     * 身份证号（模糊查询）
     */
    private String idNumber;

    /**
     * 审核时间-开始
     */
    private Date auditTimeBegin;

    /**
     * 审核时间-结束
     */
    private Date auditTimeEnd;

    /**
     * 审核状态：0-全部 1-待审核 2-审核通过 3-审核驳回
     */
    private Integer auditStatus;

    /**
     * 审核人姓名（模糊查询）
     */
    private String auditUserName;
}
