package com.yiling.admin.cms.evaluate.vo;

import com.yiling.admin.cms.content.vo.DiseaseVO;
import com.yiling.admin.cms.content.vo.HospitalDeptVO;
import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateVO extends BaseVO {

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
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String coverImage;

    /**
     * 封面url
     */
    @ApiModelProperty(value = "封面")
    private String coverImageUrl;

    /**
     * 分享背景图
     */
    @ApiModelProperty(value = "分享背景图")
    private String backImage;

    /**
     * 分享背景图url
     */
    @ApiModelProperty(value = "分享背景图url")
    private String backImageUrl;

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
     * 发布状态 1-已发布，0-未发布
     */
    @ApiModelProperty(value = "发布状态 1-已发布，0-未发布")
    private Integer publishFlag;

    /**
     * 所属医生id
     */
    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    /**
     * 所属医生名称
     */
    @ApiModelProperty(value = "所属医生名称")
    private String docName;

    /**
     *所在医疗机构
     */
    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    /**
     *所在医疗机构科室
     */
    @ApiModelProperty(value = "所在医疗机构科室")
    private String hospitalDepartment;

    /**
     * 引用业务线id list
     */
    @ApiModelProperty(value = "引用业务线id list")
    private List<Long> lineIdList;


    /**
     * 创建人
     */
    private Long createUser;
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;
    private String updateUserName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 疾病
     */
    @ApiModelProperty(value = "疾病", hidden = true)
    private List<Integer> diseaseIdList;

    @ApiModelProperty(value = "疾病")
    private List<DiseaseVO> diseaseVOList;

    /**
     * 科室
     */
    @ApiModelProperty(value = "疾病id列表", hidden = true)
    private List<Integer> deptIdList;

    @ApiModelProperty(value = "科室")
    private List<HospitalDeptVO> hospitalDeptVOS;

    /**
     * 参与人数
     */
    @ApiModelProperty(value = "参与人数")
    private Long userCount;

    /**
     * 完成测试人数
     */
    @ApiModelProperty(value = "完成测试人数")
    private Long finishCount;

    /**
     * 完成测试人次
     */
    @ApiModelProperty(value = "完成测试人次")
    private Long finishDistinctCount;

}
