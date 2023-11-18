package com.yiling.user.system.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 医药代表信息 BO
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
public class MrBO implements java.io.Serializable {

    private static final long serialVersionUID = 8335805513861470456L;

    /**
     * 医药代表ID（员工ID）
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 工号
     */
    private String code;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;
}
