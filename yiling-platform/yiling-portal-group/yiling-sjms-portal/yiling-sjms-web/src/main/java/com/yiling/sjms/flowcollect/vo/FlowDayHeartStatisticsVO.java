package com.yiling.sjms.flowcollect.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FlowDayHeartStatisticsVO extends BaseVO {
    /**
     * 商业公司编码
     */
    @ApiModelProperty("经销商编码")
    private Long crmEnterpriseId;


    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    //扩展信息
    @ApiModelProperty("商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商")
    private Integer supplierLevel;

    /**
     * 流向打取人工号
     */
    @ApiModelProperty("流向打取人工号")
    private String flowJobNumber;

    /**
     * 流向打取人姓名
     */
    @ApiModelProperty("流向打取人姓名")
    private String flowLiablePerson;

    /**
     * 商务负责人工号
     */
    @ApiModelProperty("商务负责人工号")
    private String commerceJobNumber;

    /**
     * 商务负责人姓名
     */
    @ApiModelProperty("商务负责人姓名")
    private String commerceLiablePerson;
    //---end

    /**
     * 流向类型：1-采购 2-销售 3库存
     */
    @ApiModelProperty("日流向类型：1-采购 2-销售 3库存")
    private Integer flowType;
    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口。字典：erp_client_flow_mode
     */
    @ApiModelProperty("流向收集方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口")
    private Integer flowMode;

    /**
     * 实施负责人
     */
    @ApiModelProperty("实施负责人")
    private String installEmployee;

    /**
     * 对接时间
     */
    @ApiModelProperty("对接时间")
    private Date depthTime;

    @ApiModelProperty("动态时间头数据Map数据信息")
    private Map<String, FlowDayHeartStatisticsDetailVO> dateHeadersInfoMap;

}
