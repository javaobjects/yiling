package com.yiling.dataflow.gb.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbOrderPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveOrUpdateGbOrderRequest;

/**
 * 月流向销售数据 API
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
public interface GbOrderApi {

    /**
     * 保存或者更新
     * @param request
     * @return
     */
    boolean save(SaveOrUpdateGbOrderRequest request);

    /**
     * 保存或者更新
     * @param list
     * @return
     */
    List<Long> saveOrUpdateBatch(List<SaveOrUpdateGbOrderRequest> list);

    /**
     * 团购数据匹配流向数据
     * @param id
     * @return
     */
    boolean mateFlow(Long id);

    /**
     * 通过主键ID获取团购主数据
     * @param id
     * @return
     */
    GbOrderDTO getById(Long id);

    /**
     * 分页查询团购数据
     * @param request
     * @return
     */
    Page<GbOrderDTO> getGbOrderPage(QueryGbOrderPageRequest request);

    /**
     * 通过主键ID列表获取团购主数据
     *
     * @param idList
     * @return
     */
    List<GbOrderDTO> getByIdList(List<Long> idList);

    /**
     * 根据表单id查询
     *
     * @param formId 表单id
     * @return
     */
    List<GbOrderDTO> listByFormId(Long formId);

    /**
     * 根据表单id列表查询
     *
     * @param formIds 表单id
     * @return
     */
    List<GbOrderDTO> listByFormIdList(List<Long> formIds);

}
