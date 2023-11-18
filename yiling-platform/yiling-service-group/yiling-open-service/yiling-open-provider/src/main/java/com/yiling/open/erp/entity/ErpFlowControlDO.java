package com.yiling.open.erp.entity;

import java.io.IOException;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_flow_control")
public class ErpFlowControlDO extends BaseErpEntity {
    /**
     * 任务编号
     */
    @ToString.Include
    @NotEmpty(message = "不能为空")
    @JSONField(name = "task_code")
    private Integer taskCode;

    /**
     * 文件时间
     */
    @ToString.Include
    @NotEmpty(message = "不能为空")
    @JSONField(name = "file_time",format = "yyyy-MM-dd")
    private Date fileTime;

    /**
     * 文件的md5
     */
    @ToString.Include
    @NotEmpty(message = "不能为空")
    @JSONField(name = "file_md5")
    private String fileMd5;

    @ToString.Include
    @NotEmpty(message = "不能为空")
    @JSONField(name = "file_key")
    private String fileKey;

    @JSONField(name = "success_number")
    private Integer successNumber;

    @JSONField(name = "failed_number")
    private Integer failedNumber;

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

}
