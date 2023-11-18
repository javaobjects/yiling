package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议兑付日志表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddRebateLogRequest extends BaseRequest {


    /**
     * 兑付者的easCode
     */
    private String easCode;

    /**
     * 日志记录名称
     */
    private String logName;

    /**
     * 协议id
     */
    private Long agreementId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 兑付类型：1-协议兑付 2-其他
     */
    private Integer cashType;

    /**
     * 兑付金额
     */
    private BigDecimal discountAmount;

    /**
     * 备注
     */
    private String remark;


}
