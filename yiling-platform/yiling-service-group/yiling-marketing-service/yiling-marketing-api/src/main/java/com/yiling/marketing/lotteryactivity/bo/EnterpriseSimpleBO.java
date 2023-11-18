package com.yiling.marketing.lotteryactivity.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业信息 BO
 *
 * @author: lun.yu
 * @date: 2022-09-20
 */
@Data
@Accessors(chain = true)
public class EnterpriseSimpleBO implements Serializable {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

}
