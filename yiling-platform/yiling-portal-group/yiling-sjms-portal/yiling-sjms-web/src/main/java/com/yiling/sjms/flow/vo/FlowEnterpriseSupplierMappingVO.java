package com.yiling.sjms.flow.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingVO
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class FlowEnterpriseSupplierMappingVO extends BaseVO {

    /**
     * 流向供应商名称
     */
    @ApiModelProperty(value = "原始供应商名称")
    private String flowSupplierName;

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

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;
}
