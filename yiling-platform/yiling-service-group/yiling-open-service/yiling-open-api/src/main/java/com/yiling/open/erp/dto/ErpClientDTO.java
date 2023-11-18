package com.yiling.open.erp.dto;


import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpClientDTO extends BaseDTO {

    /**
     * crm企业ID
     */
    private Long crmEnterpriseId;

    /**
     * crm所属省份编码
     */
    private String crmProvinceCode;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 商业公司编码
     */
    private Long suId;

    /**
     * 分公司标识
     */
    private String suDeptNo;

    /**
     * 公司ID
     */
    private Long rkSuId;

    /**
     * 客户端实例Id
     */
    private String clientKey;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 终端名称
     */
    private String clientName;

    /**
     * 终端激活状态（0未激活1已激活）
     */
    private Integer clientStatus;

    /**
     * 同步状态
     */
    private Integer syncStatus;

    /**
     * 同步状态开启时间
     */
    private Date syncStatusTime;

    /**
     * 数据初始化状态：0-未初始化 1-已完成
     */
    private Integer dataInitStatus;

    /**
     * 对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
     */
    private Integer depth;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向，-1 全部
     */
    private Integer flowLevel;

    /**
     * 对接时间
     */
    private Date depthTime;

    /**
     * 实施人员
     */
    private String installEmployee;

    /**
     * erp品牌
     */
    private String erpBrand;

    /**
     * 商务人员
     */
    private String businessEmployee;

    /**
     * 技术人员
     */
    private String technologyEmployee;

    /**
     * 技术人员联系方式
     */
    private String technologyEmployeePhone;

    /**
     * 库存读取规则0读取商品库存1读取库存汇总
     */
    private Integer goodsNumberRule;

    /**
     * 上下架规格0默认上架1库存为0下架
     */
    private Integer goodsStatusRule;

    /**
     * 发货单驳回时间
     */
    private BigDecimal orderTimeRule;

    /**
     * 远程执行命令0执行完成1远程更新版本2重启服务
     */
    private Integer command;

    /**
     * 监控状态：0-未开启 1-开启，-1 全部
     */
    private Integer monitorStatus;

    /**
     * 心跳最后时间
     */
    private Date heartBeatTime;

    private Integer commandStatus;

    private Date commandTime;

    /**
     * 最新采集日期
     */
    private Date lastestCollectDate;

    /**
     * 最新流向日期
     */
    private Date lastestFlowDate;

    private String versions;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    private Integer flowMode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date   createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date   updateTime;

}
