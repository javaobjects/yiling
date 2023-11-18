package com.yiling.hmc.control.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.control.bo.GoodsPurchaseControlBO;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.dto.request.AddGoodsPurchaseRequest;
import com.yiling.hmc.control.dto.request.QueryGoodsPurchaseControlPageRequest;
import com.yiling.hmc.control.dto.request.UpdateGoodsPurchaseRequest;

/**
 * @author: gxl
 * @date: 2022/3/31
 */
public interface GoodsPurchaseControlApi {
    /**
     * 添加
     * @param addGoodsPurchaseRequest
     */
    void add(AddGoodsPurchaseRequest addGoodsPurchaseRequest);

    /**
     * 查询单个
     * @param id
     * @return
     */
    GoodsPurchaseControlDTO getOneById(Long id);

    /**
     * 修改
     * @param request
     */
    void update(UpdateGoodsPurchaseRequest request);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<GoodsPurchaseControlDTO> queryPage(QueryGoodsPurchaseControlPageRequest request);

    /**
     * 获取管控状态
     * @param controlId
     * @return
     */
    Integer getByGoodControlId(Long controlId);

    /**
     * 根据controlId查询已开启管控的渠道
     * @param controlIdList
     * @return
     */
    List<GoodsPurchaseControlDTO> queryByControlIds(List<Long> controlIdList);

    /**
     * 查询商品管控状态和管控渠道
     * @param sellSpecificationsIdList
     * @return
     */
    List<GoodsPurchaseControlBO> queryGoodsPurchaseControlList(List<Long> sellSpecificationsIdList);
}
