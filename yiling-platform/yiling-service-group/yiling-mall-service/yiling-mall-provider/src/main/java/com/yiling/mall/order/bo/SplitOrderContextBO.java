package com.yiling.mall.order.bo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.dto.UserDTO;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/** 拆单上下文环境数据
 * @author zhigang.guo
 * @date: 2022/4/26
 */
@Data
@Builder
@ToString
public class SplitOrderContextBO {

    private OrderSubmitRequest request;

    /**
     * 促销活动信息
     */
    private List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList;

    /**
     * 客户信息
     */
    private List<EnterpriseCustomerDTO> customerDTOList;

    /**
     * 所有企业信息
     */
    private List<EnterpriseDTO> allEnterpriseList;

    /**
     * 所有商品信息
     */
    private List<GoodsSkuInfoDTO> allGoodsDTOList;

    /**
     * 购物车信息
     */
    private List<CartDO> allCartDOList;

    /**
     * 以岭子公司企业Eid
     */
    private List<Long> yilingSubEids;

    /**
     * 获取省区经理函数
     */
    private Function<Long,EnterpriseEmployeeDTO> provinceManagerFunction;

    /**
     * 商务联系人函数
     */
    private Function<Long,UserDTO> contacterFunction;

    /**
     * 地址信息
     */
    private DeliveryAddressDTO deliveryAddressDTO;

    /**
     * 拆单批次号
     */
    private String orderBatchNo;

    /**
     * 工业直属企业Eid
     */
    private List<Long> industryDirectEids;

    /**
     * 获取订单号函数
     */
    private Function<NoEnum,String> orderNoFunction;

    /**
     * 获取价格函数
     */
    private Function<QueryGoodsPriceRequest,Map<Long, GoodsPriceDTO>> priceFunction;

    /**
     * 预售活动信息list
     */
    private List<PresaleActivityGoodsDTO>  presaleActivityGoodsDTOList;

    /**
     * 初始化订单函数
     */
    private Consumer<CreateOrderRequest>   initOrderFunction;
}
