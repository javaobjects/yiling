package com.yiling.user.enterprise.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业员工信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseEmployeeInfoDTO extends EnterpriseEmployeeDTO {

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 上级姓名
     */
    private String parentName;

}
