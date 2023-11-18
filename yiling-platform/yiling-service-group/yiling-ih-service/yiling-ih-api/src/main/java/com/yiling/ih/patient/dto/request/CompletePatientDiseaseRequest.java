package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 完善既往史 Request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class CompletePatientDiseaseRequest implements java.io.Serializable {

    /**
     * 患者id
     */
    private Integer patientId;

    /**
     * 过往病史-集合
     */
    private List<String> historyDisease;

    /**
     * 家族病史-集合
     */
    private List<String> familyDisease;

    /**
     * 过敏史-集合
     */
    private List<String> allergyHistory;

}
