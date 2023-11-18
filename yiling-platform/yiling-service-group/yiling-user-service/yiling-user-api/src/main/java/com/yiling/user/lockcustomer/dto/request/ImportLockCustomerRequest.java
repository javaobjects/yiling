package com.yiling.user.lockcustomer.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 导入锁定用户 Request
 *
 * @author: lun.yu
 * @date: 2022-04-02
 */
@Data
public class ImportLockCustomerRequest extends BaseRequest {

    /**
     * 企业名称
     */
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
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
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 所属板块
     */
    private String plate;

    /**
     * 状态：1-启用 2-禁用
     */
    private Integer status;
}
