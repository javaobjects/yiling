package com.yiling.dataflow.crm.api;

import java.util.List;
import java.util.Map;

import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsCategoryRequest;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryApi
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
public interface CrmGoodsCategoryApi {

    /**
     * 保存或修改品类
     *
     * @param request
     * @return
     */
    Long saveOrUpdateCategory(SaveOrUpdateCrmGoodsCategoryRequest request);

    /**
     * 通过code或者名称查询商品品类
     *
     * @param code
     * @param name
     * @return
     */
    CrmGoodsCategoryDTO findByCodeOrName(String code, String name);

    /**
     * 获取末级品类
     *
     * @return
     */
    List<CrmGoodsCategoryDTO> getFinalStageCategory(String category);

    /**
     * 获取所有层级
     *
     * @return
     */
    List<Integer> getAllLevel();

    /**
     * 获取全部品类
     *
     * @return
     */
    List<CrmGoodsCategoryDTO> queryCategoryList(QueryCrmGoodsCategoryRequest request);

    /**
     * 品类商品计数
     *
     * @param categoryIds
     * @return
     */
    Map<Long, Long> getGoodsCountByCategory(List<Long> categoryIds);

    /**
     * ids 查询商品品类
     *
     * @param ids
     * @return
     */
    List<CrmGoodsCategoryDTO> findByIds(List<Long> ids);

    /**
     * id查询商品品类
     *
     * @param id
     * @return
     */
    CrmGoodsCategoryDTO findById(Long id);

    /**
     * 查询当前父级下所有子级
     *
     * @param parentId
     * @return
     */
    List<CrmGoodsCategoryDTO> findByParentId(Long parentId);


    /**
     * 查询当前父级下所有子级
     *
     * @param parentId
     * @return
     */
    List<CrmGoodsCategoryDTO> findBakByParentId(Long parentId, String tableSuffix);

    /**
     * 通过末级获取一级品种
     *
     * @param categoryId
     * @param tableSuffix
     * @return
     */
    Long findFirstCategoryByFinal(Long categoryId, String tableSuffix);

    /**
     * id 删除品类
     *
     * @param id
     * @param opUserId
     */
    void deleteCategoryById(Long id, Long opUserId);
}
