package com.yiling.admin.cms.evaluate.form;

import cn.hutool.core.date.DateUtil;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * 健康测评分页列表查询参数
 * @author: fan.shen
 * @date: 2022/12/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHealthEvaluatePageForm extends QueryPageListForm {

    /**
     * 量表名称
     */
    @ApiModelProperty(value = "量表名称")
    private String healthEvaluateName;

    /**
     * 量表类型 1-健康，2-心理，3-诊疗
     */
    @ApiModelProperty(value = "量表类型 1-健康，2-心理，3-诊疗")
    private Integer healthEvaluateType;

    /**
     * 发布状态 1-已发布，0-未发布
     */
    @ApiModelProperty(value = "发布状态 1-已发布，0-未发布")
    private Integer publishFlag;

    /**
     * 创建时间-开始
     */
    @ApiModelProperty(value = "创建时间-开始")
    private Date createTimeStart;

    /**
     * 创建时间-截止
     */
    @ApiModelProperty(value = "创建时间-截止")
    private Date createTimeEnd;

    /**
     * 修改时间-开始
     */
    @ApiModelProperty(value = "修改时间-开始")
    private Date updateTimeStart;

    /**
     * 修改时间-开始
     */
    @ApiModelProperty(value = "修改时间-开始")
    private Date updateTimeEnd;

    public void build() {
        if(Objects.nonNull(this.createTimeStart)) {
            this.setCreateTimeStart(DateUtil.beginOfDay(createTimeStart));
        }
        if(Objects.nonNull(this.createTimeEnd)) {
            this.setCreateTimeEnd(DateUtil.endOfDay(createTimeEnd));
        }
        if(Objects.nonNull(this.updateTimeStart)) {
            this.setUpdateTimeStart(DateUtil.beginOfDay(updateTimeStart));
        }
        if(Objects.nonNull(this.updateTimeEnd)) {
            this.setUpdateTimeEnd(DateUtil.endOfDay(updateTimeEnd));
        }
    }
}