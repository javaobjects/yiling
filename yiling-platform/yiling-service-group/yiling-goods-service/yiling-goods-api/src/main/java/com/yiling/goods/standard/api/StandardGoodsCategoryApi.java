package com.yiling.goods.standard.api;


import java.util.List;

import com.yiling.goods.standard.dto.StandardGoodsCategoryDTO;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.dto.request.SaveCategoryInfoRequest;
import com.yiling.goods.standard.dto.request.UpdateCategoryNameRequest;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
public interface StandardGoodsCategoryApi {

    /**
     * 获取一级类别
     *
     * @return
     */
    List<StandardGoodsCategoryDTO> getFirstCateInfo();

    /**
     * 获取所以二级类别
     *
     * @return
     */
    List<StandardGoodsCategoryDTO> getSecondCateInfo();

    /**
     * 根据父id获取子分类
     * @param parentId
     * @return
     */
    List<StandardGoodsCategoryDTO> getSecondCateOne(Long parentId);

    /**
     * 获取所有分类的信息
     *
     * @return
     */
    List<StandardGoodsCategoryInfoAllDTO> getAllCateInfo();

    /**
     * 根据id编辑分类名称
     *
     * @param request
     * @return
     */
    Boolean updateCateName(UpdateCategoryNameRequest request);

    /**
     * 修改分类id
     * @param parentId 父类
     * @param id
     * @param opUserId 操作人
     * @return
     */
    Boolean updateCateParentId(Long parentId,Long id,Long opUserId );



    /**
     * 新建分类
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
    StandardGoodsCategoryDTO getCategoryByName(Integer type , String name);

    /**
     *获取分类信息
     * @param id id
     * @return
     */
    StandardGoodsCategoryDTO getOneById(Long id);
}
