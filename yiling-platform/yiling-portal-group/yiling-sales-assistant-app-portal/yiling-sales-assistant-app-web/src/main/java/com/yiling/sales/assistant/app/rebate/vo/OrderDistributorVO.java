package com.yiling.sales.assistant.app.rebate.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.sales.assistant.app.order.vo.OrderGoodsVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配送商订单信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@Data
public class OrderDistributorVO {

    @ApiModelProperty("配送商企业ID")
    private Long distributorEid;

    @ApiModelProperty("配送商名称")
    private String distributorName;

    @ApiModelProperty("商品列表")
    private List<OrderGoodsVO> orderGoodsList;

    @ApiModelProperty("商品种数")
    private Long goodsSpeciesNum;

    @ApiModelProperty("商品件数")
    private Long goodsNum;

    @ApiModelProperty("商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费")
    private BigDecimal freightAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("购销合同文件列表")
    private List<FileInfoVO> contractFileList;

    @ApiModelProperty(value = "买家留言")
    private String buyerMessage;

    @ApiModelProperty("是否是以岭配送商")
    private Boolean yilingFlag;

    @ApiModelProperty("是否需要购销合同")
    private Boolean showContractFile;

    @ApiModelProperty("是否参与任务")
    private Boolean isHasTask;
}
