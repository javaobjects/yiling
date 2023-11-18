package com.yiling.sjms.agency.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmAgencyPageListForm extends QueryPageListForm {

    @ApiModelProperty(value = "机构编码")
    private String id;
    @ApiModelProperty(value = "CRM编码")
    private String code;
    @ApiModelProperty(value = "以岭编码")
    private String ylCode;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构简称")
    private String shortName;

    @ApiModelProperty(value = "'所属省份编码'")
    private String provinceCode;

    @ApiModelProperty(value = "'所属城市编码'")
    private String cityCode;

    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    @ApiModelProperty(value = "状态 0全部 1有效 2失效")
    private String businessCode;

    @ApiModelProperty(value = "创建时间-开始")
    private Date beginTime;

    @ApiModelProperty(value = "创建时间-结束")
    private Date endTime;

    @ApiModelProperty(value = "erp供应链角色：1-经销商 2-终端医院 3-终端药店")
    private Integer supplyChainRole;
}
