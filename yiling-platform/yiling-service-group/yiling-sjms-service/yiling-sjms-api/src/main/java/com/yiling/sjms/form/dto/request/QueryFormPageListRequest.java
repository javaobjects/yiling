package com.yiling.sjms.form.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询业务表单分页列表
 *
 * @author: xuan.zhou
 * @date: 2023/2/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFormPageListRequest extends QueryPageListRequest {

    /**
     * 单据编号
     */
    private String code;

    /**
     * 团购类型（参见FormTypeEnum枚举）
     */
    private Integer type;

    /**
     * 发起人工号
     */
    private String empId;

    /**
     * 状态：0-全部 （参见FormStatusEnum枚举）
     */
    private Integer status;

    /**
     * 创建时间-起
     */
    private Date createTimeBegin;

    /**
     * 创建时间-止
     */
    private Date createTimeEnd;

    /**
     * 提交审批时间-起
     */
    private Date submitTimeBegin;

    /**
     * 提交审批时间-起
     */
    private Date submitTimeEnd;

    /**
     * 不包含的状态
     */
    private List<Integer> excludeStatusList;

}
