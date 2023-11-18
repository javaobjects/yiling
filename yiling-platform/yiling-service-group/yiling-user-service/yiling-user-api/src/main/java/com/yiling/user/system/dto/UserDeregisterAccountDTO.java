package com.yiling.user.system.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserDeregisterAccountDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 申请注销时间
     */
    private Date applyTime;

    /**
     * 来源：1-销售助手APP 2-大运河APP 3-医生助手APP
     */
    private Integer source;

    /**
     * 终端类型：1-Android 2-IOS
     */
    private Integer terminalType;

    /**
     * 注销原因
     */
    private String applyReason;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 注销状态：1-待注销 2-已注销 3-已撤销
     */
    private Integer status;

    /**
     * 注销/撤销时间
     */
    private Date updateStatusTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

}
