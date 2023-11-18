package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateUnlockCustomerWashTaskRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 对应日程表ID
     */
    private Long fmwcId;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * 非锁分类是否已经发送：0未发送1已接收
     */
    private Integer classificationStatus;

    private Integer lockWashGbStatus;
    /**
     * 非锁规则是否已经发送：0未发送1已接收
     */
    private Integer ruleStatus;

}
