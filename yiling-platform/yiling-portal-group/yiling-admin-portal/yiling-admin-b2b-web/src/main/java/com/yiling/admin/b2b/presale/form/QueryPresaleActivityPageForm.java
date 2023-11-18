package com.yiling.admin.b2b.presale.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPresaleActivityPageForm extends QueryPageListForm {

    @ApiModelProperty("活动名称")
    private String name;
    
    @ApiModelProperty("状态：0-全部 1-启用 2-停用 3-废弃")
    private Integer status;

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Integer progress;

    @ApiModelProperty("分类 0-全部 1-平台活动；2-商家活动；")
    private Integer sponsorType;

    @ApiModelProperty("企业id")
    private Long eid;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建人手机号")
    private String createTel;

    @ApiModelProperty("创建开始时间")
    private Date startTime;

    @ApiModelProperty("创建截止时间")
    private Date stopTime;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    @ApiModelProperty("预售活动id")
    private Long id;
}
