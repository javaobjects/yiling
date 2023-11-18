package com.yiling.sjms.gb.form;

import lombok.Data;


/**
 * 保存团购信息
 */
@Data
public class SaveGBAttachInfoForm{

    /**
     * 文件类型：1-团购证据 2-费用申请提交
     */
    private Integer fileType;

    /**
     * 文件KEY
     */
    private String fileKey;

    /**
     * 文件名称
     */
    private String fileName;
}
