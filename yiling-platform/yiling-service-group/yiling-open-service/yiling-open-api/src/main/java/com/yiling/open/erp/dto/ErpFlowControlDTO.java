package com.yiling.open.erp.dto;

import java.io.IOException;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-22
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpFlowControlDTO extends BaseErpEntity {

    /**
     * 任务编号
     */
    @ToString.Include
    @NotNull(message = "不能为空")
    @JSONField(name = "task_code")
    private Integer taskCode;

    /**
     * 文件时间
     */
    @ToString.Include
    @NotNull(message = "不能为空")
    @JSONField(name = "file_time",format = "yyyy-MM-dd")
    private Date fileTime;

    /**
     * 文件的md5
     */
    @ToString.Include
    @NotBlank(message = "不能为空")
    @JSONField(name = "file_md5")
    private String fileMd5;

    @ToString.Include
    @NotBlank(message = "不能为空")
    @JSONField(name = "file_key")
    private String fileKey;

    @JSONField(name = "success_number")
    private Integer successNumber;

    @JSONField(name = "failed_number")
    private Integer failedNumber;

    /**
     * 本地缓存的md5
     */
//    @ToString.Include
    @JSONField(name = "cache_file_md5")
    private String cacheFilemd5;

    @Override
    public Long getPrimaryKey() {
        return this.getId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        this.setId(primaryKey);
    }

    @Override
    public String getErpPrimaryKey() {
        if (StringUtils.isEmpty(suDeptNo)) {
            return fileMd5;
        }
        return fileMd5 + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    @Override
    public String sign() {
        try {
            return SignatureAlgorithm.byte2hex(SignatureAlgorithm.encryptMD5(toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpFlowControl.getMethod();
    }

    /**
     * 流向缓存本地时间
     * @return
     */
    @Override
    public String getFlowKey(){
        if (StringUtils.isEmpty(suDeptNo)) {
            return DateUtil.format(fileTime, "yyyy-MM-dd");
        }
        return DateUtil.format(fileTime, "yyyy-MM-dd")+ OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    /**
     * 流向缓存本地md5
     * @return
     */
    @Override
    public String getFlowCacheFileMd5(){
        return cacheFilemd5;
    }
}
