package com.yiling.user.system.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号校验信息 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserDeregisterValidDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 不通过原因集合
     */
    private List<String> rejectReasonList;


}
