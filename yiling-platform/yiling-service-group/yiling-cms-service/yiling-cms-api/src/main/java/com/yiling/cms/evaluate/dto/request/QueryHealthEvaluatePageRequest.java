package com.yiling.cms.evaluate.dto.request;

import com.yiling.cms.content.dto.request.QueryContentPageRequest;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHealthEvaluatePageRequest extends QueryContentPageRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 量表名称
     */
    private String healthEvaluateName;

    /**
     * 量表类型 1-健康，2-心理，3-诊疗
     */
    private Integer healthEvaluateType;

    /**
     * 发布状态 1-已发布，0-未发布
     */
    private Integer publishFlag;

    /**
     * 创建时间-开始
     */
    private Date createTimeStart;

    /**
     * 创建时间-截止
     */
    private Date createTimeEnd;

    /**
     * 修改时间-开始
     */
    private Date updateTimeStart;

    /**
     * 修改时间-开始
     */
    private Date updateTimeEnd;
}
