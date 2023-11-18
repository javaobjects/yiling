package com.yiling.hmc.control.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.control.bo.GoodsPurchaseControlBO;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.dto.request.AddGoodsPurchaseRequest;
import com.yiling.hmc.control.dto.request.QueryGoodsPurchaseControlPageRequest;
import com.yiling.hmc.control.dto.request.UpdateGoodsPurchaseRequest;
import com.yiling.hmc.control.entity.GoodsPurchaseControlDO;

/**
 * <p>
 * 药品进货渠道管控 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-03-31
 */
public interface GoodsPurchaseControlService extends BaseService<GoodsPurchaseControlDO> {

    /**
     * 添加
     * @param addGoodsPurchaseRequest
     */
    void add(AddGoodsPurchaseRequest addGoodsPurchaseRequest);


    /**
     * 查询单个
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
     * 查询管控状态
     * @param controlId
     * @return
     */
    Integer getByGoodControlId(Long controlId);



    /**
     * 根据controlIdList查询已开启管控的渠道
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

    /**
     * 根据规格id查询管控渠道
     * @param sellSpecificationsIdList
     * @return
     */
    List<GoodsPurchaseControlDTO>   queryControlListBySpecificationsId(List<Long> sellSpecificationsIdList);
}
