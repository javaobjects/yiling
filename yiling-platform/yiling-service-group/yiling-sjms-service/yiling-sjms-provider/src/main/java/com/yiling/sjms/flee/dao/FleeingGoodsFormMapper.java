package com.yiling.sjms.flee.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sjms.flee.bo.FleeingFormBO;
import com.yiling.sjms.flee.dto.request.QueryFleeingFormPageRequest;
import com.yiling.sjms.flee.entity.FleeingGoodsFormDO;

/**
 * <p>
 * 窜货申报表单 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
@Repository
public interface FleeingGoodsFormMapper extends BaseMapper<FleeingGoodsFormDO> {

    /**
     * 窜货申报主表单分页关联查询
     *
     * @param request 查询条件
     * @return 表单信息
     */
    Page<FleeingFormBO> pageForm(Page<FleeingGoodsFormDO> page, @Param("request") QueryFleeingFormPageRequest request);

}
