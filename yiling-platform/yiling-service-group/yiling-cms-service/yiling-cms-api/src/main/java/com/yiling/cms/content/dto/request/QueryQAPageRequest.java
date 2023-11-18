package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * QA分页列表查询参数
 *
 * @author: fan.shen
 * @date: 2023/3/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryQAPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * 问答id
     */
    private Long id;

    /**
     * 上级问答id
     */
    private Long qaId;

    /**
     * 创建人类型 1-患者，2-医生
     */
    private Long userType;

    /**
     * contentIdList
     */
    private List<Long> contentIdList;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 业务线id 1-HMC,2-IH-doc,3-IH-patient
     */
    private Integer lineId;
}