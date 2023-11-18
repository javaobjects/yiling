package com.yiling.user.esb.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单ESB人员信息 BO
 *
 * @author: xuan.zhou
 * @date: 2022/11/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEsbEmployeeInfoBO implements java.io.Serializable {

    /**
     * 工号
     */
    private String empId;

    /**
     * 姓名
     */
    private String empName;
}
