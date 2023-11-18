package com.yiling.ih.user.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 医带患医生导入
 * @author: fan.shen
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class HMCImportActivityDocRequest implements java.io.Serializable{

    private static final long serialVersionUID = -544667472485532361L;

    /**
     * 当前记录索引
     */
    private Integer index;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 活动码地址
     */
    private String qrcodeUrl;

    /**
     * 创建人id
     */
    private Integer createUser;


}
