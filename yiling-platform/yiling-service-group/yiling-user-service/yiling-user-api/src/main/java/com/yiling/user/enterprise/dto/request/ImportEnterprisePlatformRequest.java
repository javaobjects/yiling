package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 导入企业开通平台 Request
 *
 * @author: lun.yu
 * @date: 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ImportEnterprisePlatformRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 社会统一信用代码/医疗机构执业许可证
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

}
