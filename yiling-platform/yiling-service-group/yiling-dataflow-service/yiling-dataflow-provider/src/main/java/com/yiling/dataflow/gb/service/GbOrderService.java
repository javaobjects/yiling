package com.yiling.dataflow.gb.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbOrderPageRequest;
import com.yiling.dataflow.gb.entity.GbOrderDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 团购主表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
public interface GbOrderService extends BaseService<GbOrderDO> {

    Page<GbOrderDO> getGbOrderPage(QueryGbOrderPageRequest request);

    boolean mateFlow(Long id);

    /**
     * 通过主键ID列表获取团购主数据
     *
     * @param idList
     * @return
     */
    List<GbOrderDO> getByIdList(List<Long> idList);

    /**
     * 根据表单id查询
     *
     * @param formId 表单id
     * @return
     */
    List<GbOrderDO> listByFormId(Long formId);

    /**
     * 根据表单id列表查询
     *
     * @param formIds 表单id
     * @return
     */
    List<GbOrderDO> listByFormIdList(List<Long> formIds);
}
