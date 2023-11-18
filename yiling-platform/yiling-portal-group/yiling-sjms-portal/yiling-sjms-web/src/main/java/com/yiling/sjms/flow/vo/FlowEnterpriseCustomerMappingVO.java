package com.yiling.sjms.flow.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.sjms.agency.vo.EnterpriseDisableVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingVO
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class FlowEnterpriseCustomerMappingVO extends BaseVO {

    /**
     * 流向客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String flowCustomerName;

    /**
     * 标准机构编码
     */
    @ApiModelProperty(value = "标准机构编码")
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    @ApiModelProperty(value = "标准机构名称")
    private String orgName;

    /**
     * 标准机构社会信用代码
     */
    @ApiModelProperty(value = "标准机构社会信用代码")
    private String orgLicenseNumber;
    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String enterpriseName;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    @ApiModelProperty(value = "省份代码")
    private String provinceCode;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "客户省份")
    private String customerProvince;

    @ApiModelProperty(value = "客户市")
    private String customerCity;

    @ApiModelProperty(value = "客户区")
    private String customerRegion;

    /**
     * 最后上传时间
     */
    @ApiModelProperty(value = "最后上传时间")
    private Date lastUploadTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "推荐度>90%列表")
    private List<RecommendCustomerInfoVO> recommendList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecommendCustomerInfoVO {

        @ApiModelProperty("机构编码")
        private Long id;

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

    }

}
