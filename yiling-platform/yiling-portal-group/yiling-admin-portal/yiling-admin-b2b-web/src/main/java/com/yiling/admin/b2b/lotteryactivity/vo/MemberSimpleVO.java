package com.yiling.admin.b2b.lotteryactivity.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员简单对象 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberSimpleVO extends BaseVO {

    /**
     * 会员名称
     */
    private String name;

}