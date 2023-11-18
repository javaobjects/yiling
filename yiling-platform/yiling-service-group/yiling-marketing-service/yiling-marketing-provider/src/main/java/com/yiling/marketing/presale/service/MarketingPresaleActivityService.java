package com.yiling.marketing.presale.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.PresaleActivityOrderDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleOrderRequest;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 预售活动主表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
public interface MarketingPresaleActivityService extends BaseService<MarketingPresaleActivityDO> {


    List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEid(QueryPresaleInfoRequest request);

    List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEidNoNum(QueryPresaleInfoRequest request);

    Page<PresaleActivityOrderDTO> queryOrderInfoByPresaleId(QueryPresaleOrderRequest request);
}
