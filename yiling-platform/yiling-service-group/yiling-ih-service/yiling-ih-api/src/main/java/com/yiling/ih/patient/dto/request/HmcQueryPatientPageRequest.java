package com.yiling.ih.patient.dto.request;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

/**
 * 查询患者入参
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class HmcQueryPatientPageRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 患者年龄-开始
     */
    private Integer startPatientAge;

    /**
     * 患者年龄-结束
     */
    private Integer endPatientAge;

    /**
     * 患者性别
     */
    private Integer gender;

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 每页显示条数，默认10
     */
    private Integer size = 10;

    /**
     * 当前页，默认1
     */
    private Integer current = 1;

    public <T> Page<T> getPage() {
        return new Page<>(this.current, this.size);
    }

}
