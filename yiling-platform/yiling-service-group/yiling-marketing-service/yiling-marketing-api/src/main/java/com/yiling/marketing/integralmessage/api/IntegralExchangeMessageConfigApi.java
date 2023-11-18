package com.yiling.marketing.integralmessage.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.integralmessage.dto.IntegralExchangeMessageConfigDTO;
import com.yiling.marketing.integralmessage.dto.request.DeleteIntegralMessageRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessageListRequest;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessagePageRequest;
import com.yiling.marketing.integralmessage.dto.request.SaveIntegralExchangeMessageConfigRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageOrderRequest;
import com.yiling.marketing.integralmessage.dto.request.UpdateIntegralMessageStatusRequest;

/**
 * 积分兑换消息配置 API
 *
 * @author: lun.yu
 * @date: 2023-01-16
 */
public interface IntegralExchangeMessageConfigApi {

    /**
     * 查询积分兑换消息配置分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralExchangeMessageConfigDTO> queryListPage(QueryIntegralMessagePageRequest request);

    /**
     * 查询积分兑换消息列表
     *
     * @param request
     * @return
     */
    List<IntegralExchangeMessageConfigDTO> queryList(QueryIntegralMessageListRequest request);

    /**
     * 保存积分兑换消息配置
     *
     * @param request
     * @return
     */
    boolean saveConfig(SaveIntegralExchangeMessageConfigRequest request);

    /**
     * 删除积分兑换消息配置
     *
     * @param request
     * @return
     */
    boolean deleteConfig(DeleteIntegralMessageRequest request);

    /**
     * 更新积分兑换消息配置状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateIntegralMessageStatusRequest request);

    /**
     * 更新积分兑换消息配置排序
     *
     * @param request
     * @return
     */
    boolean updateOrder(UpdateIntegralMessageOrderRequest request);

    /**
     * 查看积分兑换消息配置
     *
     * @param id
     * @return
     */
    IntegralExchangeMessageConfigDTO get(Long id);

}
