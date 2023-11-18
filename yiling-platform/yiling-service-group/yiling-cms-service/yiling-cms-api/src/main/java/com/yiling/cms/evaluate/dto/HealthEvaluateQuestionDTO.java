package com.yiling.cms.evaluate.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionBlankRequest;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionBmiRequest;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionSelectRequest;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评题目
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateQuestionDTO extends BaseDTO {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 题目类型 1-单选题，2-填空题，3-多选题，4-BMI计算题
     */
    private Integer questionType;

    /**
     * 题干
     */
    private String questionTopic;

    /**
     * 是否计分：0-否 1-是
     */
    private Integer ifScore;

    /**
     * 是否必填：0-否 1-是
     */
    private Integer ifBlank;

    /**
     * 题目排序
     */
    private Integer questionRank;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


    /**
     * 选择题
     */
    private List<HealthEvaluateQuestionSelectDTO> selectList;

    /**
     * BMI
     */
    private List<HealthEvaluateQuestionBmiDTO> bmiList;

    /**
     * 填空题
     */
    private HealthEvaluateQuestionBlankDTO blank;

}
