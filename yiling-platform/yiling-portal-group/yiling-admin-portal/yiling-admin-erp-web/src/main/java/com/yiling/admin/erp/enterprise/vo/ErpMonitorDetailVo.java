package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/9
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("erp监控信息详情VO")
public class ErpMonitorDetailVo extends BaseVO {

    /**
     * 父类企业id
     */
    @ApiModelProperty(value = "父类企业id")
    private Long suId;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String  clientName;

    /**
     * 对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
     */
    @ApiModelProperty(value = "对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接")
    private Integer depth;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    @ApiModelProperty(value = "流向级别：0-未对接 1-以岭流向 2-全品流向")
    private Integer flowLevel;

    /**
     * 对接负责人
     */
    @ApiModelProperty(value = "对接负责人")
    private String  installEmployee;

    /**
     * 对接时间
     */
    @ApiModelProperty(value = "对接时间")
    private Date    depthTime;

    /**
     * 同步状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启")
    private Integer syncStatus;

    /**
     * 监控状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "监控状态：0-未开启 1-开启")
    private Integer monitorStatus;

    /**
     * 终端激活状态：0-未激活 1-已激活
     */
    @ApiModelProperty(value = "企业对接状态：0-未激活 1-已激活")
    private Integer clientStatus;

    /**
     * 最后一次心跳时间
     */
    @ApiModelProperty(value = "最后一次心跳时间")
    private Date    heartBeatTime;

//    /**
//     * 心跳请求次数
//     */
//    @ApiModelProperty(value = "心跳请求次数")
//    private Long    heartBeatCont;

    /**
     * 当天请求次数：新增
     */
    @ApiModelProperty(value = "当天请求次数：新增")
    private Long    requestAddCont;

    /**
     * 当天请求次数：更新
     */
    @ApiModelProperty(value = "当天请求次数：更新")
    private Long    requestUpdateCont;

    /**
     * 当天请求次数：删除
     */
    @ApiModelProperty(value = "当天请求次数：删除")
    private Long    requestDeleteCont;

    /**
     * 当天请求次数：总数
     */
    @ApiModelProperty(value = "当天请求次数：总数")
    private Long    requestTotalCont;

    /**
     *命令心跳
     */
    @ApiModelProperty(value = "命令心跳时间")
    private Date redisHeartTime;

    /**
     *命令状态
     */
    @ApiModelProperty(value = "命令状态 0-未打开执行状态1-可执行状态但按钮不可点 2-按钮可点击")
    private Integer commandButtonStatus;

    /**
     *命令状态
     */
    @ApiModelProperty(value = "按钮描述")
    private String commandButtonDesc;

    /**
     *命令状态
     */
    @ApiModelProperty(value = "版本号")
    private String versions;

    private Integer commandStatus;

    private Date commandTime;


}
