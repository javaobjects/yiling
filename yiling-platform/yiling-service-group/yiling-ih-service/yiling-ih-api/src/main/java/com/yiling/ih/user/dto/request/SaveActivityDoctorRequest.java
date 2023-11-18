package com.yiling.ih.user.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class SaveActivityDoctorRequest extends BaseRequest {

    private Integer activityId;

    private List<HmcActivityDoctorQrcodeUrlQuest> hmcActivityDoctorQrcodeUrlFormList;

    private Integer createUser;

}
