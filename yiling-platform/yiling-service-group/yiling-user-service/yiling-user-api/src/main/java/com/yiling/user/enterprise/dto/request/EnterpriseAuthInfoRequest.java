package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业副本信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseAuthInfoRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 联系人
     */
    private String contactor;

    /**
     * 联系人电话
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
     * 详细地址
     */
    private String address;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    private Integer authStatus;

    /**
     * 数据来源：1-B2B 2-销售助手 3-企业信息更新
     */
    private Integer source;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 审核驳回原因
     */
    private String authRejectReason;

}
