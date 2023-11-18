package com.yiling.user.integral.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralBehaviorBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.request.QueryIntegralBehaviorRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 积分行为表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Repository
public interface IntegralBehaviorMapper extends BaseMapper<IntegralBehaviorDO> {

    /**
     * 分页查询积分行为
     *
     * @param page
     * @param request
     * @return
     */
    Page<IntegralBehaviorDTO> queryListPage(Page page, @Param("request") QueryIntegralBehaviorRequest request);

}
