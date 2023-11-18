package com.yiling.dataflow.crm.service;

import java.util.List;
import java.util.Map;

import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsCategoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryService
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
public interface CrmGoodsCategoryService extends BaseService<CrmGoodsCategoryDO> {
    /**
     * 保存或修改品类
     * @param request
     * @return
     */
    Long saveOrUpdateCategory(SaveOrUpdateCrmGoodsCategoryRequest request);

    /**
     * 通过code或者名称查询商品品类
     * @param code
     * @param name
     * @return
     */
    CrmGoodsCategoryDTO findByCodeOrName(String code,String name);

    /**
     * 获取末级品类
     * @return
     */
    List<CrmGoodsCategoryDTO> getFinalStageCategory(String category);

    /**
     * 获取所有层级
     * @return
     */
    List<Integer> getAllLevel();

    /**
     * 条件查询品类
     * @return
     */
    List<CrmGoodsCategoryDTO> queryCategoryList(QueryCrmGoodsCategoryRequest request);

    /**
     * 品类商品计数
     * @param categoryIds
     * @return
     */
    Map<Long,Long> getGoodsCountByCategory(List<Long> categoryIds);

    /**
     * 查询当前父级下所有子级
     * @param parentId
     * @return
     */
    List<CrmGoodsCategoryDTO> findByParentId(Long parentId);

    /**
     * 查询当前父级下所有子级
     * @param parentId
     * @return
     */
    List<CrmGoodsCategoryDTO> findBakByParentId(Long parentId,String tableSuffix);

    /**
     * categoryIds 查询备份品类
     * @param categoryIds
     * @return
     */
    List<CrmGoodsCategoryDTO> findBakByCategoryIds(List<Long> categoryIds,String tableSuffix);

    /**
     * 通过末级品种查询一级品种
     * @param categoryId
     * @param tableSuffix
     * @return
     */
    Long findFirstCategoryByFinal(Long categoryId,String tableSuffix);

    /**
     * 获取全部品类
     * @return
     */
    List<CrmGoodsCategoryDTO> findAllCategoryList();

    /**
     * 获取备份全部品类
     * @param tableSuffix 备份表后缀
     * @return
     */
    List<CrmGoodsCategoryDTO> findBakAllCategoryList(String tableSuffix);

    /**
     * 末级和一级的映射
     * @return key：末级品类id  value：一级品类对象
     */
    Map<Long,CrmGoodsCategoryDTO> getFinalAndFirstMapping();

    /**
     * 获取备份末级和一级的映射
     * @return key：末级品类id  value：一级品类对象
     */
    Map<Long,CrmGoodsCategoryDTO> getBakFinalAndFirstMapping(String tableSuffix);
}
