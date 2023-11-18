package com.yiling.user.usercustomer.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCertificateRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新用户客户 Request
 * 
 * @author lun.yu
 * @date 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUserCustomerRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户ID
     */
    private Long customerEid;

    /**
     * 企业类型
     */
    private Integer type;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 社会统一信用代码
     */
    private String licenseNumber;

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
     * 联系人
     */
    private String contactor;

    /**
     * 联系人电话
     */
    private String contactorPhone;

    /**
     * 企业资质列表
     */
    private List<UpdateEnterpriseCertificateRequest> certificateList;

}
