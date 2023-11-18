package com.yiling.ih.user.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 身份证照片正面照 OCR request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class IdCardFrontPhotoOcrRequest implements java.io.Serializable{

    /**
     * 身份证照片正面照 base64
     */
    private String imageContent;

}
