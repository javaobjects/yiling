package com.yiling.goods.standard.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsCategoryApi;
import com.yiling.goods.standard.dto.StandardGoodsCategoryDTO;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.dto.request.SaveCategoryInfoRequest;
import com.yiling.goods.standard.dto.request.UpdateCategoryNameRequest;
import com.yiling.goods.standard.entity.StandardGoodsCategoryDO;
import com.yiling.goods.standard.service.StandardGoodsCategoryService;

/**
 * 库存管理类型
 *
 * @author: wei.wang
 * @date: 2021/5/20
 */
@DubboService
public class StandardGoodsCategoryApiImpl implements StandardGoodsCategoryApi {

    @Autowired
    private StandardGoodsCategoryService standardGoodsCateService;

    /**
     * 获取一级类别
     *
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDTO> getFirstCateInfo() {
        List<StandardGoodsCategoryDO> cateInfo = standardGoodsCateService.getFirstCateInfo();
        return PojoUtils.map(cateInfo, StandardGoodsCategoryDTO.class);
    }

    /**
     * 获取二级类别
     *
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDTO> getSecondCateInfo() {
        List<StandardGoodsCategoryDO> cateInfo = standardGoodsCateService.getSecondCateInfo();
        return PojoUtils.map(cateInfo, StandardGoodsCategoryDTO.class);
    }

    /**
     * 根据父id获取子分类
     *
     * @param parentId
     * @return
     */
    @Override
    public List<StandardGoodsCategoryDTO> getSecondCateOne(Long parentId) {
        List<StandardGoodsCategoryDO> cateOne = standardGoodsCateService.getSecondCateOne(parentId);
        return PojoUtils.map(cateOne, StandardGoodsCategoryDTO.class);
    }

    /**
     * 获取所有分类的信息
     *
     * @return
     */
    @Override
    public List<StandardGoodsCategoryInfoAllDTO> getAllCateInfo() {
        List<StandardGoodsCategoryInfoAllDTO> allCateInfo = standardGoodsCateService.getAllCateInfo();
        return allCateInfo;
    }

    /**
     * 根据id编辑分类名称
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateCateName(UpdateCategoryNameRequest request) {
        Boolean result = standardGoodsCateService.updateCateName(request);
        return result;
    }

    /**
     * 修改分类id
     * @param parentId 父类
     * @param id
     * @param opUserId 操作人
     * @return
     */
    @Override
    public Boolean updateCateParentId(Long parentId, Long id,Long opUserId) {
        Boolean result = standardGoodsCateService.updateCateParentId(parentId,id,opUserId);
        return result;
    }

    /**
     * 新建分类
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveCateInfo(SaveCategoryInfoRequest request) {
        Boolean result = standardGoodsCateService.saveCateInfo(request);
        return result;
    }

    /**
     * 根据类型获取
     *
     * @param type 1-一级分类 2-二级分类
     * @param name 分类名称
     * @return
     */
    @Override
    public StandardGoodsCategoryDTO getCategoryByName(Integer type, String name) {
        StandardGoodsCategoryDO result = standardGoodsCateService.getCategoryByName(type, name);
        return PojoUtils.map(result,StandardGoodsCategoryDTO.class);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public StandardGoodsCategoryDTO getOneById(Long id) {
        StandardGoodsCategoryDO result = standardGoodsCateService.getById(id);
        return PojoUtils.map(result,StandardGoodsCategoryDTO.class);
    }


}
