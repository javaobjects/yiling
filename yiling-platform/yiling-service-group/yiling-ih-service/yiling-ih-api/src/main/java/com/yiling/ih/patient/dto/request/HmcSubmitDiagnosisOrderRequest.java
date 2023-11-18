package com.yiling.ih.patient.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 提交预约问诊单
 *
 * @author fan.shen
 * @date 2023-02-07
 */
@Data
public class HmcSubmitDiagnosisOrderRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 问诊类型，0图文1音频2视频
     */
    private Integer type;

    /**
     * 精确日期
     */
    private Date date;

    /**
     * 所属时段
     */
    private String belongTime;

    /**
     * 就诊人id
     */
    private Integer patientId;

    /**
     * 病情资料-症状描述
     */
    private String diseaseDescribe;

    /**
     * 病情资料-上传既往病历和查验报告(图片key)
     */
    private List<String> diseaseDescribePicture;

    /**
     * 用户id
     */
    private Integer fromUserId;

    /**
     * groupId
     */
    private String groupId;

}
