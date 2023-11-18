package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseInfoByNamePageListForm extends QueryPageListForm {

    @ApiModelProperty(value = "机构基础信息id")
    private Long crmEnterpriseId;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;

    @ApiModelProperty(value = "业务状态 1有效 2失效")
    private Integer businessCode;

    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    @ApiModelProperty(value = "所属城市编码")
    private String cityCode;

    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    @ApiModelProperty(value = "权限控制 0-无权限,1-有权限,默认0无权限")
    private Integer permit;

    @ApiModelProperty(value = "备份表年月")
    private Date yearMonth;
}
