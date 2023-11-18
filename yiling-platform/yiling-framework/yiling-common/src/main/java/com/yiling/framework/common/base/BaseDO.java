package com.yiling.framework.common.base;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * Data Object持久对象，数据库表中的记录在java对象中的显示状态
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
@Data
public class BaseDO implements java.io.Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作人
     */
    @TableField(exist = false)
    private Long opUserId;

    /**
     * 操作时间
     */
    @TableField(exist = false)
    private Date opTime = new Date();
}
