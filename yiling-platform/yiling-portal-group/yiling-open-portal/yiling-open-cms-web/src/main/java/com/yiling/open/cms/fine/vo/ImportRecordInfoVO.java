package com.yiling.open.cms.fine.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ImportRecordInfoVO extends BaseVO {

    private String resultUrl;

    private Integer successNumber;

    private Integer failNumber;

    /**
     * 0-未读取 1-正在上传 2-上传成功 3-上传失败
     */
    private Integer status;

    private String remark;

}
