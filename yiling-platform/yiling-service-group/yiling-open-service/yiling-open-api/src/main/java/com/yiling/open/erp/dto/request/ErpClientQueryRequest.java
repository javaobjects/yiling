package com.yiling.open.erp.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpClientQueryRequest extends QueryPageListRequest {

    /**
     * crm企业id
     */
    private Long crmEnterpriseId;

    /**
     * 商业公司编码
     */
    private Long suId;

    /**
     * 企业id
     */
    private Long rkSuId;

    /**
     * 对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接，-1 全部
     */
    private Integer depth;

    /**
     * 同步状态：0-未开启 1-开启，-1 全部
     */
    private Integer syncStatus;

    /**
     * 终端激活状态：0-未激活 1-已激活
     */
    private Integer clientStatus;

    /**
     * 企业名称
     */
    private String clientName;

    /**
     * 对接负责人
     */
    private String installEmployee;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向，-1 全部
     */
    private Integer flowLevel;

    /**
     * 监控状态：0-未开启 1-开启，-1 全部
     */
    private Integer monitorStatus;

    /**
     * bi对接状态：0对接 1未对接
     */
    private Integer biStatus;

    /**
     * 生成任务状态
     */
    private Integer flowStatus;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    private Integer flowMode;

    /**
     * 心跳最后时间
     */
    private Date heartBeatTimStart;

    /**
     * 心跳最后时间
     */
    private Date heartBeatTimEnd;

    /**
     * 数据初始化状态：0-未初始化 1-已完成
     */
    private Integer dataInitStatus;
}
