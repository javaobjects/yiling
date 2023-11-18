package com.yiling.ih.patient.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 已认证详情
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class DoctorVerifyDetailRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    private Integer doctorId;

}
