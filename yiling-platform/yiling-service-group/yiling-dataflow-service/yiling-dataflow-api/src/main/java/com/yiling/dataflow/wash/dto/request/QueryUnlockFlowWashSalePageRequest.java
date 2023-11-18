package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
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
public class QueryUnlockFlowWashSalePageRequest extends QueryPageListRequest {

    /**
     * 月份(计入)
     */
    private String month;

    /**
     * 年份(计入)
     */
    private String year;

    /**
     * 商业编码
     */
    private Long crmId;

    /**
     * 原始客户名称
     */
    private String originalEnterpriseName;

    /**
     * 机构编码
     */
    private Long customerCrmId;

    /**
     * 原始商品名称
     */
    private String soGoodsName;

    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    private Integer judgment;

    /**
     * 分配状态：1-未分配2-已分配
     */
    private Integer distributionStatus;

    /**
     * 分配结果来源 1-规则 2-人工
     */
    private Integer distributionSource;

    private Date startUpdate;

    private Date endUpdate;
}
