package com.yiling.ih.patient.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 问诊医生
 */
@Data
@Accessors(chain = true)
public class DiagnosisDoctorResultDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    private Long total;

    private List<DiagnosisDoctorDTO> list;

}