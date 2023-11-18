package com.yiling.sales.assistant.task.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务选择终端
 * @author gaoxinlei
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskTerminalDTO extends BaseDTO {



    /**
     *
     */
    private Long customerEid;

    /**
     * 企业名称
     */
    private String   name;


    /**
     * 联系人
     */
    private String   contactor;

    /**
     * 区号-号码
     */
    private String   contactorPhone;

    /**
     * 省份
     */
    private String   provinceName;

    /**
     * 市
     */
    private String   cityName;

    /**
     * 区域
     */
    private String   regionName;

    /**
     * 企业地址
     */
    private String   address;


    /**
     * 0可选 1已选 2被其他人选中
     */
    private Integer status ;
}
