package com.yiling.sjms.flowcollect.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDayCollectStatisticsForm extends QueryPageListForm {

    @ApiModelProperty("经销商编码")
    private Long crmEnterpriseId;
    @ApiModelProperty("经销商级别")
    private Integer supplierLevel;
    @ApiModelProperty(value = "流向收集方式")
    private Integer flowMode;
    @ApiModelProperty(value = "流向类型")
    private  Integer flowType;
    @ApiModelProperty(value = "实施负责人")
    private String installEmployee;
    /**
     * 上传状态 1-近3天有一项未上传、2-近3天全部未上传、3-近5天有一项未上传、4-近5天全部未上传
     */
    @ApiModelProperty(value = "上传情况")
    private Integer uploadStatus;
}
