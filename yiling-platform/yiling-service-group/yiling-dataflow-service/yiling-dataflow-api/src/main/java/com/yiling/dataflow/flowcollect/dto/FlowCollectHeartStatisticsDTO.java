package com.yiling.dataflow.flowcollect.dto;

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
 * 日流向心跳统计表
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCollectHeartStatisticsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司编码
     */
    private Long crmEnterpriseId;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 流向类型：1-采购 2-销售 3库存
     */
    private Integer flowType;

    /**
     * 统计的时间
     */
    private Date statisticsTime;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口。字典：erp_client_flow_mode
     */
    private Integer flowMode;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 对接时间
     */
    private Date depthTime;

    /**
     * 近3天有一项未上传状态：1-是 2-否
     */
    private Integer nearThreeStatus;

    /**
     * 近5天有一项未上传状态：1-是 2-否
     */
    private Integer nearFiveStatus;

    /**
     * 近3天所有项未上传状态：1-是 2-否
     */
    private Integer nearThreeAllStatus;

    /**
     * 近5天所有项未上传状态：1-是 2-否
     */
    private Integer nearFiveAllStatus;

}
