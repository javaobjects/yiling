package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 保存医带患活动关系 DTO
 *
 * @author: fan.shen
 * @date: 2022-09-09
 */
@Data
public class SaveActivityDocPatientRelDTO implements java.io.Serializable {

    private static final long serialVersionUID = -2295160299359559926L;

    /**
     * 1-患者已被其他用户绑定，无需重复绑定，2-患者通过问诊已绑定，无需重复绑定，3-添加成功
     */
    private Integer flag;
}
