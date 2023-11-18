package com.yiling.user.integral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralBehaviorBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.request.QueryIntegralBehaviorRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分行为表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralBehaviorService extends BaseService<IntegralBehaviorDO> {

    /**
     * 积分行为分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralBehaviorBO> queryListPage(QueryIntegralBehaviorRequest request);

    /**
     * 根据行为名称获取行为
     *
     * @param name
     * @return
     */
    IntegralBehaviorDTO getByName(String name);
}
