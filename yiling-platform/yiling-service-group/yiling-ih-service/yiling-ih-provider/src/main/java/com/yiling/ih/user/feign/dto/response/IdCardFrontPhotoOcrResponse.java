package com.yiling.ih.user.feign.dto.response;

import com.yiling.ih.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 身份证照片正面照 OCR DTO
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IdCardFrontPhotoOcrResponse extends BaseResponse {

    /**
     * 名称
     */
    private String name;

    /**
     * 身份证
     */
    private String id;

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

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    /**
     * 地址
     */
    private String address;

}
