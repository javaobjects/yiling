package com.yiling.bi.resource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("upload_resource")
public class UploadResourceDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private String dataId;

    private byte[] fileStream;

}
