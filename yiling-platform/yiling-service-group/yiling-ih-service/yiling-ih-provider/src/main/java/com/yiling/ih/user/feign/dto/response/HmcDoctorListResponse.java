package com.yiling.ih.user.feign.dto.response;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
public class HmcDoctorListResponse implements java.io.Serializable{

    private Integer doctorId;

    private String name;

    private String hospitalName;

    private Integer source;

    private Date createTime;

    private Integer activityState;
}
