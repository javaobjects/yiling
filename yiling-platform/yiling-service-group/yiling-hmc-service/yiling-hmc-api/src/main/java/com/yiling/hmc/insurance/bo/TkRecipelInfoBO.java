package com.yiling.hmc.insurance.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class TkRecipelInfoBO implements Serializable {

    /**
     * 渠道处方号
     */
    private String thirdId;

    /**
     * 处方开具时间  格式yyyy-MM-dd HH:mm:ss
     */
    private Date prescribingTime;

    /**
     * 疾病编码类型   1-ICD10编码 2-中医疾病症候
     */
    private Integer diseaseCodeTable;

    /**
     * 疾病编码
     */
    private String diseaseCode;

    /**
     * 疾病名称
     */
    private String diseaseName;

//    /**
//     * 开方医生姓名
//     */
//    private String doctorName;
//
//    /**
//     * 开方科室名称
//     */
//    private String deptName;
//
//    /**
//     * 开方医院名称   互联网医院名称
//     */
//    private String hospitalName;

    /**
     * 处方药品市场总价（元）
     */
    private String recipelTotalPrices;
}
