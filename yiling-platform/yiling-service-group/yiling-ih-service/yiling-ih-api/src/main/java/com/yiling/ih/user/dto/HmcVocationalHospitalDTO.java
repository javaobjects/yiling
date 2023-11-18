package com.yiling.ih.user.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/05
 */
@Data
@Accessors(chain = true)
public class HmcVocationalHospitalDTO implements Serializable {

    private static final long serialVersionUID = -7697103564248255897L;
    private Integer id;

    /**
     * 医院名称
     */
    private String designation;

}
