package com.yiling.ih.user.feign.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 保存医带患活动信息 DTO
 *
 * @author: fan.shen
 * @date: 2023-01-31
 */
@Data
public class SaveActivityDocPatientRelResponse implements java.io.Serializable {

    private static final long serialVersionUID = 7707287975078132869L;

    /**
     * 1-患者已被其他用户绑定，无需重复绑定，2-患者通过问诊已绑定，无需重复绑定，3-添加成功
     */
    private Integer flag;

}
