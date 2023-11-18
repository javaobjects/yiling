package com.yiling.bi.protocol.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputTthreelsflLhauxdisplayRecordDTO extends BaseDTO {

    private static final long serialVersionUID = -5078782104786217588L;

    /**
     * 省份
     */
    private String province;

    /**
     * 连锁分公司编码
     */
    private String bzCode;

    /**
     * 连锁分公司名称
     */
    private String bzName;

    /**
     * 陈列项目
     */
    private String displayXm;

    /**
     * 辅助陈列门店家数
     */
    private BigDecimal displayStorenum;

    /**
     * 门店级别
     */
    private String storeLevel;

    /**
     * 端架
     */
    private String bracket;

    /**
     * 堆头
     */
    private String pilehead;

    /**
     * 花车
     */
    private String flowerCar;

    /**
     * 柜台堆头
     */
    private String gtPilehead;

    /**
     * 收银台
     */
    private String cashDesk;

    /**
     * 立柱
     */
    private String stud;

    /**
     * 灯箱
     */
    private String lampBox;

    /**
     * 吊旗
     */
    private String showbill;

    /**
     * 橱窗
     */
    private String shopwindow;

    /**
     * 提交时间
     */
    private Date dataTime;

    /**
     * 提交人
     */
    private String dataName;

    /**
     * 数据状态：-1 默认状态 0 省区业务人员提报 1 运营经理审批通过 2 运营经理打回 3 运营经理提报 4 财务审核通过 5 财务打回
     */
    private String dataStatus;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 年份
     */
    private String dyear;
}
