package com.yiling.user.system.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xuan.zhou
 * @date: 2023/1/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MrUserRegisterDTO extends BaseDTO {

    private static final long serialVersionUID = 5246406105928406076L;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属企业名称
     */
    private String ename;

    /**
     * 审核状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer auditStatus;

    /**
     * 驳回原因
     */
    private String rejectText;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
