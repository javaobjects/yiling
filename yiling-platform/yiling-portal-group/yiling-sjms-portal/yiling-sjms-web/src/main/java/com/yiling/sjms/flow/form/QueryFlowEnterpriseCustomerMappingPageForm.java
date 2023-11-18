package com.yiling.sjms.flow.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryFlowEnterpriseCustomerMappingPageForm
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowEnterpriseCustomerMappingPageForm extends QueryPageListForm {
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
     * 开始更新时间
     */
    @ApiModelProperty(value = "最后操作时间-开始时间")
    private Date startUpdateTime;

    /**
     * 结束更新时间
     */
    @ApiModelProperty(value = "最后操作时间-结束时间")
    private Date endUpdateTime;

    /**
     * 开始最后上传时间
     */
    @ApiModelProperty(value = "最后上传时间-开始时间")
    private Date startLastUploadTime;

    /**
     * 结束最后上传时间
     */
    @ApiModelProperty(value = "最后上传时间-结束时间")
    private Date endLastUploadTime;

    @ApiModelProperty(value = "是否查询推荐度>90%的数据 0-否（查询全部） 1-是")
    private Integer recommendFlag;
}
