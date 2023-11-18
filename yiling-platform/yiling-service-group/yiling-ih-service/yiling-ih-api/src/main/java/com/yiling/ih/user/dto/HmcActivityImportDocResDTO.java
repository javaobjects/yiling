package com.yiling.ih.user.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动医生导入结果
 *
 * @author: fan.shen
 * @data: 2023/02/01
 */
@Data
@Accessors(chain = true)
public class HmcActivityImportDocResDTO implements java.io.Serializable {

    private static final long serialVersionUID = 6232095217976779033L;

    /**
     * 当前记录索引
     */
    private Integer index;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 失败原因
     */
    private String failureReason;


}
