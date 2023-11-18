package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 身份证照片正面照 OCR DTO
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
public class IdCardFrontPhotoOcrDTO implements java.io.Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 身份证
     */
    private String idNo;

    /**
     * 性别
     */
    private String gender;

    /**
     * 民族
     */
    private String nation;

    /**
     * 生日
     */
    private String birth;

    /**
     * 省code
     */
    private String provinceCode;
    private String provinceName;

    /**
     * 市code
     */
    private String cityCode;
    private String cityName;

    /**
     * 区code
     */
    private String regionCode;
    private String regionName;

    /**
     * 地址
     */
    private String address;

}
