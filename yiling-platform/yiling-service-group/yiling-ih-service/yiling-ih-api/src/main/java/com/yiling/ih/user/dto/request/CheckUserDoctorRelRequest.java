package com.yiling.ih.user.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查看用户是否和医生绑定-医带患
 * @author: fan.shen
 * @data: 2023-01-31
 */
@Data
@Accessors(chain = true)
public class CheckUserDoctorRelRequest implements java.io.Serializable{

    private List<Integer> userIdList;

    private Integer doctorId;

}
