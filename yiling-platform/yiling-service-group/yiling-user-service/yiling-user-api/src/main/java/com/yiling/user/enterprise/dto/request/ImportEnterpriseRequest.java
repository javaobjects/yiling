package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 导入企业信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ImportEnterpriseRequest extends BaseRequest {

    /**
     * 企业名称
     */
    private String name;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 所属企业ID
     */
    private Long parentId;

    /**
     * 社会统一信用代码/医疗机构执业许可证
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
     * 开通产品线
     */
    private List<PlatformEnum> platformEnumList;

    /**
     * 渠道类型ID
     */
    private Long channelId;

    /**
     * 药+险业务类型
     */
    private EnterpriseHmcTypeEnum hmcTypeEnum;

    /**
     * 企业编码
     */
    private String code;

    /**
     * ERP编码
     */
    private String erpCode;

}
