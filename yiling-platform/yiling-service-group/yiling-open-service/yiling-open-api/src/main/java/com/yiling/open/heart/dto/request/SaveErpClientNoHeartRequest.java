package com.yiling.open.heart.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveErpClientNoHeartRequest extends BaseRequest {

    /**
     * 商业公司编码
     */
    private Long suId;

    /**
     * 分公司标识
     */
    private String suDeptNo;

    /**
     * 公司ID
     */
    private Long rkSuId;

    /**
     * 终端名称
     */
    private String clientName;

    /**
     * 对接方式：工具、ftp 、第三方接口、以岭平台接口
     */
    private String flowMode;

    /**
     * 实施人员
     */
    private String installEmployee;

    /**
     * 部门
     */
    private String crmDepartment;

    /**
     * 省区
     */
    private String crmProvince;

    /**
     * 商务负责人工号
     */
    private String crmCommerceJobNumber;

    /**
     * 商务负责人
     */
    private String crmCommerceLiablePerson;

    /**
     * 统计时间
     */
    private Date taskTime;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
