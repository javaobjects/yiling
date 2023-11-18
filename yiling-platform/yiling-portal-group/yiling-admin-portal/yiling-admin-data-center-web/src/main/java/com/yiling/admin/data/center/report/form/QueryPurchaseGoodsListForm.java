package com.yiling.admin.data.center.report.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPurchaseGoodsListForm extends BaseForm {

    @ApiModelProperty("年月")
    private String time;

    @ApiModelProperty("采购商业列表")
    private List<Long> purchaseEnterpriseIds;

    @ApiModelProperty("采购商品名称")
    private List<String> goodsNameList;

    @ApiModelProperty("最小入库数量")
    private Integer minQuantity;

    @ApiModelProperty("最大入库数量")
    private Integer maxQuantity;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("区编码")
    private String regionCode;
}
