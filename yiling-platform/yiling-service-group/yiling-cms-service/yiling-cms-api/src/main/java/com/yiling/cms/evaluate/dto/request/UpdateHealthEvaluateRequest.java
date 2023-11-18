package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class UpdateHealthEvaluateRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    private Long id;

    /**
     * 量表名称
     */
    private String healthEvaluateName;

    /**
     * 量表类型 1-健康，2-心理，3-诊疗
     */
    private Integer healthEvaluateType;

    /**
     * 量表描述
     */
    private String healthEvaluateDesc;

    /**
     * 预计答题时间(m)
     */
    private Integer evaluateTime;

    /**
     * 是否医生私有 0-否，1-是
     */
    private Integer docPrivate;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 封面
     */
    private String coverImage;

    /**
     * 分享背景图
     */
    private String backImage;

    /**
     * 答题须知
     */
    private String answerNotes;

    /**
     * 是否有结果 0-否，1-是
     */
    private Integer resultFlag;

    /**
     * 引用业务线id list
     */
    private List<Long> lineIdList;

    /**
     * 科室
     */
    private List<Long> deptIdList;

    /**
     * 疾病
     */
    private List<Long> diseaseIdList;

}
