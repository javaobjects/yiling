package com.yiling.goods.standard.service;

import java.util.List;
import java.util.Set;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.dto.request.SaveCategoryInfoRequest;
import com.yiling.goods.standard.dto.request.UpdateCategoryNameRequest;
import com.yiling.goods.standard.entity.StandardGoodsCategoryDO;

/**
 * <p>
 * 标准库商品分类表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardGoodsCategoryService extends BaseService<StandardGoodsCategoryDO> {

    /**
     * 根据id批量获取药品类型
     *
     * @param ids
     * @return
     */
    List<StandardGoodsCategoryDO> getStandardGoodsCateList(Set<Long> ids);

    /**
     * 获取一级分类信息
     *
     * @return
     */
    List<StandardGoodsCategoryDO> getFirstCateInfo();

    /**
     * 获取所有二级分类信息
     *
     * @return
     */
    List<StandardGoodsCategoryDO> getSecondCateInfo();

    /**
     * 根据父id获取分类
     * @param parentId
     * @return
     */
    List<StandardGoodsCategoryDO> getSecondCateOne(Long parentId);


    /**
     * 获取所有分类信息
     *
     * @return
     */
    List<StandardGoodsCategoryInfoAllDTO> getAllCateInfo();

    /**
     * 根据id修改药品名称
     *
     * @param request
     * @return
     */
    Boolean updateCateName(UpdateCategoryNameRequest request);

    /**
     * 修改分类id
     * @param parentId
     * @param id
     * @return
     */
    Boolean updateCateParentId(Long parentId,Long id,Long opUserId );

    /**
     * 新增分类
     *
     * @param request
     * @return
     */
    Boolean saveCateInfo(SaveCategoryInfoRequest request);

    /**
     * 根据类型获取
     * @param type 1-一级分类 2-二级分类
     * @param name 分类名称
     * @return
     */
    StandardGoodsCategoryDO getCategoryByName(Integer type , String name);

}
