package com.yiling.dataflow.statistics.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商业公司每天平衡表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowBalanceStatisticsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 统计时间
     */
    private Date dateTime;

    /**
     * 采购数量
     */
    private Long poQuantity;

    /**
     * 销售数量
     */
    private Long soQuantity;

    /**
     * 库存数量
     */
    private Long gbQuantity;

    /**
     * 上一次库存数量
     */
    private Long lastGbQuantity;

    /**
     * 相差数量=(上一天库存数量+采购数量-当天库存数量-销售数量)
     */
    private Long differQuantity;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口。字典：erp_client_flow_mode
     */
    private Integer flowMode;

}
