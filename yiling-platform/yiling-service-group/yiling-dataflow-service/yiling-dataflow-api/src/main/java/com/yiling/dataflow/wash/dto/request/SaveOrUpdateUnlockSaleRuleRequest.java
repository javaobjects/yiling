package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateUnlockSaleRuleRequest extends BaseRequest {

    private Long id;

    private String code;
    /**
     * 规则名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 来源：1-手动设置2-小三批备案3-区域备案4-集采明细
     */
    private Integer source;

    /**
     * 商业公司范围：1-全部2-指定商业公司
     */
    private Integer businessRange;

    /**
     * 客户公司范围：1-全部2-指定商业公司3-指定范围
     */
    private Integer customerRange;

    /**
     * 商品范围：1-全部2-指定商品3-指定标签4-指定品规
     */
    private Integer goodsRange;

    /**
     * 销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人
     */
    private Integer saleRange;
    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    private Integer judgment;

    /**
     * 非锁分配备注
     */
    private String notes;

    /**
     * 状态：0-开启1-关闭
     */
    private Integer status;

    private Integer  saleIncludedRange;

    private SaveUnlockSaleCustomerRangeRequest saveUnlockSaleCustomerRangeRequest;

    private SaveUnlockSaleDepartmentRequest saveUnlockSaleDepartmentRequest;
}
