package com.yiling.ih.disease.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DiseaseDTO extends BaseDTO {

    private static final long serialVersionUID = 3768586786173659462L;
    private String name;

    private String code;
}