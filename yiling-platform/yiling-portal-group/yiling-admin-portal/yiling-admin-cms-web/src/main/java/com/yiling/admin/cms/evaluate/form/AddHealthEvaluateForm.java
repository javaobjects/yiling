package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddHealthEvaluateForm extends BaseForm {

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
     * 量表描述
     */
    @ApiModelProperty(value = "量表描述")
    private String healthEvaluateDesc;

    /**
     * 预计答题时间(m)
     */
    @ApiModelProperty(value = "预计答题时间(m)")
    private Integer evaluateTime;

    /**
     * 是否医生私有 0-否，1-是
     */
    @ApiModelProperty(value = "是否医生私有 0-否，1-是")
    private Integer docPrivate;

    /**
     * 所属医生id
     */
    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String coverImage;

    /**
     * 分享背景图
     */
    @ApiModelProperty(value = "分享背景图")
    private String backImage;

    /**
     * 答题须知
     */
    @ApiModelProperty(value = "答题须知")
    private String answerNotes;

    /**
     * 是否有结果 0-否，1-是
     */
    @ApiModelProperty(value = "是否有结果 0-否，1-是")
    private Integer resultFlag;

    /**
     * 引用业务线id list
     */
    @ApiModelProperty(value = "引用业务线id list")
    private List<Long> lineIdList;

    /**
     * 科室
     */
    @ApiModelProperty(value = "科室")
    private List<Long> deptIdList;

    /**
     * 疾病
     */
    @ApiModelProperty(value = "疾病")
    private List<Long> diseaseIdList;
}
