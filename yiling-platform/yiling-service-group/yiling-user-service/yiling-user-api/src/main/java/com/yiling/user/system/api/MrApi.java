package com.yiling.user.system.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.AddOrRemoveMrSalesGoodsRequest;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;
import com.yiling.user.system.dto.request.UpdateMrSalesGoodsRequest;

/**
 * 医药代表 API
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
public interface MrApi {

    /**
     * 查询企业医药代表分页列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.user.system.bo.MrBO>
     * @author xuan.zhou
     * @date 2022/6/7
     **/
    Page<MrBO> pageList(QueryMrPageListRequest request);

    /**
     * 批量获取医药代表信息
     *
     * @param ids 医药代表ID列表
     * @return java.util.List<com.yiling.user.system.bo.MrBO>
     * @author xuan.zhou
     * @date 2022/6/9
     **/
    List<MrBO> listByIds(List<Long> ids);

    /**
     * 获取医药代表信息
     *
     * @param id 医药代表ID
     * @return com.yiling.user.system.bo.MrBO
     * @author xuan.zhou
     * @date 2022/6/9
     **/
    MrBO getById(Long id);

    /**
     * 批量获取医药代表可售药品配置信息列表
     * @param employeeIds 医药代表员工ID列表
     * @return java.util.Map<java.lang.Long,java.util.List<java.lang.Long>>
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    Map<Long, List<Long>> listGoodsIdsByEmployeeIds(List<Long> employeeIds);

    /**
     * 根据员工ID和多个商品ID查询配置信息，返回已添加的商品ID列表
     *
     * @param employeeId 员工ID
     * @param goodsIds 商品ID列表
     * @return java.util.List<com.yiling.user.system.entity.MrSalesGoodsDetailDO>
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    List<Long> listGoodsIdsByEmployeeIdAndGoodsIds(Long employeeId, List<Long> goodsIds);

    /**
     * 批量根据商品ID查询绑定的医药代表ID
     *
     * @param goodsIds 商品ID列表
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2022/6/9
     **/
    List<Long> listEmoloyeeIdsByGoodsIds(List<Long> goodsIds);

    /**
     * 添加或删除医药代表可售商品
     * 
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    Boolean addOrRemoveSalesGoods(AddOrRemoveMrSalesGoodsRequest request);

    /**
     * 更新医药代表可售商品信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/6/7
     **/
    Boolean updateSalesGoods(UpdateMrSalesGoodsRequest request);
}
