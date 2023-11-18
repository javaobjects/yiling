package com.yiling.ih.user.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
public class HmcDoctorListDTO implements java.io.Serializable{

    private static final long serialVersionUID = -7860173438087501692L;
    private Integer doctorId;

    private String name;

    private String hospitalName;

    private Integer source;

    private Date createTime;

    private Integer activityState;

}
