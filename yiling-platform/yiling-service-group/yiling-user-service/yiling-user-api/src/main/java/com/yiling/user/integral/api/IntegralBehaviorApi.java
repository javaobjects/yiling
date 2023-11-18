package com.yiling.user.integral.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralBehaviorBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.request.QueryIntegralBehaviorRequest;

/**
 * 积分行为 API
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
public interface IntegralBehaviorApi {

    /**
     * 积分行为分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralBehaviorBO> queryListPage(QueryIntegralBehaviorRequest request);

    /**
     * 根据ID获取积分行为
     *
     * @param id
     * @return
     */
    IntegralBehaviorDTO getById(Long id);

}
