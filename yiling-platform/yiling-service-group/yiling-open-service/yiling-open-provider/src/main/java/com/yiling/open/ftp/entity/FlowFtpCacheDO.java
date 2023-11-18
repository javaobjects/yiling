package com.yiling.open.ftp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_ftp_cache")
public class FlowFtpCacheDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 公司编码
     */
    private Long suId;

    /**
     * key
     */
    private String erpKey;

    /**
     * 加密
     */
    private String md5;

    /**
     * 类型
     */
    private String type;


}
