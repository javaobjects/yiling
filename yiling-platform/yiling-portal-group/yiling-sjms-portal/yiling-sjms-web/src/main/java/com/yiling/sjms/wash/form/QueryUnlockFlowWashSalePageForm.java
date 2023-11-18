package com.yiling.sjms.wash.form;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流向销售合并报
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockFlowWashSalePageForm extends QueryPageListForm {

    /**
     * 月份(计入)
     */
    @ApiModelProperty(value = "月份")
    private String month;

    /**
     * 年份(计入)
     */
    @ApiModelProperty(value = "年份")
    private String year;

    /**
     * 商业编码
     */
    @ApiModelProperty(value = "商业编码")
    private Long crmId;

    /**
     * 原始客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String originalEnterpriseName;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long customerCrmId;

    /**
     * 原始商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String soGoodsName;

    /**
     * 产品(sku)编码
     */
    @ApiModelProperty(value = "产品(sku)编码")
    private Long goodsCode;

    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    @ApiModelProperty(value = "判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构")
    private Integer judgment;

    /**
     * 分配状态：1-未分配2-已分配
     */
    @ApiModelProperty(value = "分配状态：1-未分配2-已分配")
    private Integer distributionStatus;

    /**
     * 分配结果来源 1-规则 2-人工
     */
    @ApiModelProperty(value = "分配结果来源 1-规则 2-人工")
    private Integer distributionSource;

    @ApiModelProperty(value = "开始的最后修改时间")
    private Date startUpdate;

    @ApiModelProperty(value = "结束的最后修改时间")
    private Date endUpdate;

}
