package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterprisePartVO extends BaseVO {

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    @ApiModelProperty("供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("crm系统对应客户名称")
    private String name;

    /**
     * 统一信用代码
     */
    @ApiModelProperty("统一信用代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("简称")
    private String shortName;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;
    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;
    /**
     * 所属区区域名称
     */
    @ApiModelProperty("所属区区域名称")
    private String regionName;

    @ApiModelProperty("所属区区域名称")
    private EnterpriseDisableVO enterpriseDisableVO;

    /**
     * 是否目标 1-是；2-否(零售机构专用)
     */
    @ApiModelProperty("是否目标 1-是；2-否(零售机构专用)")
    private Integer targetFlag;
}
