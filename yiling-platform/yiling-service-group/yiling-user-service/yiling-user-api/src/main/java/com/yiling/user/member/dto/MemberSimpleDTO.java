package com.yiling.user.member.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员简单对象 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022/01/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberSimpleDTO extends BaseDTO {

    /**
     * 会员名称
     */
    private String name;

}
