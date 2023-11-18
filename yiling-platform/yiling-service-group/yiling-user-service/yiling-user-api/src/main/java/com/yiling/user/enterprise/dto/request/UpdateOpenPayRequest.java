package com.yiling.user.enterprise.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 开通线下支付 Request
 *
 * @author: lun.yu
 * @date: 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOpenPayRequest extends BaseRequest {

    /**
     * 查询条件request
     */
    private QueryCustomerPageListRequest queryRequest;

    /**
     * 客户ID
     */
    private List<Long> customerEidList;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;

    /**
     * 操作类型：1-开通 2-关闭
     */
    private Integer opType;

}
