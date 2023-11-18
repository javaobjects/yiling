package com.yiling.admin.data.center.report.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员简单对象 VO
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-09
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
