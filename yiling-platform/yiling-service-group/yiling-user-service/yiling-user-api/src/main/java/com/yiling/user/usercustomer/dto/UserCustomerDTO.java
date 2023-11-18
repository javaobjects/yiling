package com.yiling.user.usercustomer.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户客户信息
 *
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserCustomerDTO extends BaseDTO {

    private static final long serialVersionUID = -7066366355146577047L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 客户企业ID
     */
    private Long customerEid;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业类型
     */
    private Integer type;

    /**
     * 企业信用代码
     */
    private String licenseNumber;

    /**
     * 联系人
     */
    private String contactor;

    /**
     * 区号-号码
     */
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer status;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 认证时间
     */
    private Date createTime;

    /**
     * 提交待审核时锁定状态：1-启用 2-禁用
     */
    private Integer lockStatus;

}
