package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class HmcActivityDoctorQrcodeUrlQuest implements java.io.Serializable {

    private static final long serialVersionUID = 266801155261954677L;

    private Integer doctorId;

    private String qrcodeUrl;
}
