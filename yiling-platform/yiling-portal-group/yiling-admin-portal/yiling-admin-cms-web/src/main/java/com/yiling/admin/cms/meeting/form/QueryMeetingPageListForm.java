package com.yiling.admin.cms.meeting.form;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询会议管理分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
@ApiModel("查询会议管理分页列表Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingPageListForm extends QueryPageListForm {

    /**
     * 标题
     */
    @Length(max = 20)
    @ApiModelProperty("标题")
    private String title;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    @Range(min = 0, max = 4)
    @ApiModelProperty("引用业务线：0-全部 1-2C患者 2-医生 3-患者 4-店员 5-医代")
    private Integer useLine;

    /**
     * 开始创建时间
     */
    @ApiModelProperty("开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty("结束创建时间")
    private Date endCreateTime;

}
