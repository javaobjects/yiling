package com.yiling.data.center.admin.enterprisecustomer.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业客户使用产品线表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/29
 */
@Data
@Accessors(chain = true)
public class EnterpriseCustomerLineVO {

    /**
     * 使用产品线：1-POP 2-B2B
     */
    private Integer useLine;

    private Boolean checked;

}
