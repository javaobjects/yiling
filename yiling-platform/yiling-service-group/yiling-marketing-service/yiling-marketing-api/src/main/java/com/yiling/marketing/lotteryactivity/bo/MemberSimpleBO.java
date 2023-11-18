package com.yiling.marketing.lotteryactivity.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员简单对象 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-20
 */
@Data
@Accessors(chain = true)
public class MemberSimpleBO implements Serializable {

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 会员名称
     */
    private String name;

}
