package com.yiling.dataflow.flow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelDTO
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseChannelDTO extends BaseDTO {

    /**
     * 机构编码
     */
    private Long crmOrgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 区
     */
    private String region;

    /**
     * 区代码
     */
    private String regionCode;

    /**
     * 采购渠道机构编码
     */
    private Long crmPurchaseOrgId;

    /**
     * 采购渠道机构名称
     */
    private String purchaseOrgName;

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

    /**
     * 备注
     */
    private String remark;
}
