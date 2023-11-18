package com.yiling.user.system.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色下的人dto
 * </p>
 *
 * @author dexi.yao
 * @date 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleStaffDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * eid
     */
    private Long eid;

}
