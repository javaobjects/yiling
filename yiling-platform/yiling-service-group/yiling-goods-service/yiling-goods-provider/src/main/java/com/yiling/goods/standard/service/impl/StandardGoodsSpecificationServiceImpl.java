package com.yiling.goods.standard.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dao.StandardGoodsSpecificationMapper;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationPicDTO;
import com.yiling.goods.standard.dto.request.IndexStandardGoodsSpecificationPageRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.service.StandardGoodsPicService;
import com.yiling.goods.standard.service.StandardGoodsService;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <p>
 * 商品规格标准表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardGoodsSpecificationServiceImpl extends BaseServiceImpl<StandardGoodsSpecificationMapper, StandardGoodsSpecificationDO> implements StandardGoodsSpecificationService {

    @Autowired
    private StandardGoodsSpecificationMapper standardGoodsSpecificationMapper;
    @Autowired
    private StandardGoodsPicService          standardGoodsPicService;
    @Autowired
    private GoodsService                     goodsService;
    @Autowired
    private StandardGoodsService standardGoodsService;

    @Override
    public Page<StandardGoodsSpecificationPicDTO> getIndexStandardGoodsSpecificationInfoPage(IndexStandardGoodsSpecificationPageRequest request) {
        Page<StandardGoodsSpecificationDO> standardGoodsPage = standardGoodsSpecificationMapper.getIndexStandardGoodsSpecificationInfoPage(new Page<>(request.getCurrent(), request.getSize()), request);
        Page<StandardGoodsSpecificationPicDTO> page = PojoUtils.map(standardGoodsPage, StandardGoodsSpecificationPicDTO.class);
        List<Long> standardIds = standardGoodsPage.getRecords().stream().map(e -> e.getStandardId()).distinct().collect(Collectors.toList());

        //批量获取图片信息
        Map<Long, List<StandardGoodsPicDO>> finalStandardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);

        page.getRecords().forEach(e -> {
            e.setPic(goodsService.getDefaultUrlByStandardGoodsPicList(finalStandardGoodsPicMap.get(e.getStandardId()), e.getId()));
        });
        return page;
    }

    @Override
    public Page<StandardGoodsSpecificationPicDTO> querySpecificationByB2b(IndexStandardGoodsSpecificationPageRequest request) {
        Page<StandardGoodsSpecificationDO> standardSpecificationPage = this.baseMapper.querySpecificationByB2b(request.getPage(), request);
        Page<StandardGoodsSpecificationPicDTO> page = PojoUtils.map(standardSpecificationPage, StandardGoodsSpecificationPicDTO.class);
        List<Long> standardIds = standardSpecificationPage.getRecords().stream().map(e -> e.getStandardId()).distinct().collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(standardIds)){
            List<StandardGoodsDO> standardGoodsDOList = standardGoodsService.listByIds(standardIds);
            Map<Long, StandardGoodsDO> standardMap = standardGoodsDOList.stream().collect(Collectors.toMap(StandardGoodsDO::getId, Function.identity()));
            //批量获取图片信息
            Map<Long, List<StandardGoodsPicDO>> finalStandardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
            page.getRecords().forEach(e -> {
                StandardGoodsDO standardGoods = standardMap.get(e.getStandardId());
                if(null!=standardGoods){
                    e.setGdfName(standardGoods.getGdfName());
                }
                e.setPic(goodsService.getDefaultUrlByStandardGoodsPicList(finalStandardGoodsPicMap.get(e.getStandardId()), e.getId()));
            });
        }

        return page;
    }

    @Override
    public StandardGoodsSpecificationDTO getStandardGoodsSpecification(Long specificationId) {
        StandardGoodsSpecificationDO standardGoodsSpecificationDO = this.getById(specificationId);
        return PojoUtils.map(standardGoodsSpecificationDO, StandardGoodsSpecificationDTO.class);
    }

    /**
     * 获取标准商品管理数量
     *
     * @param ids
     * @return
     */
    @Override
    public List<StandardGoodsSpecificationDO> getListStandardGoodsSpecification(List<Long> ids) {
        QueryWrapper<StandardGoodsSpecificationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardGoodsSpecificationDO::getStandardId, ids);
        return this.list(wrapper);
    }

    @Override
    public List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecificationByIds(List<Long> specificationIds) {
        QueryWrapper<StandardGoodsSpecificationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardGoodsSpecificationDO::getId, specificationIds);
        return PojoUtils.map(this.list(wrapper), StandardGoodsSpecificationDTO.class);
    }

    /**
     * 保存规格型号
     *
     * @param specificationDO
     * @return
     */
    @Override
    public Long saveStandardGoodsSpecificationOne(StandardGoodsSpecificationDO specificationDO) {
        if (this.saveOrUpdate(specificationDO)) {
            return specificationDO.getId();
        }
        return null;
    }

    /**
     * 根据标准库id返回
     *
     * @param request
     * @return
     */
    @Override
    public Page<StandardGoodsSpecificationDO> getSpecificationPage(StandardSpecificationPageRequest request) {
        QueryWrapper<StandardGoodsSpecificationDO> wrapper = new QueryWrapper<>();
        if (request.getStandardId() != null) {
            wrapper.lambda().eq(StandardGoodsSpecificationDO::getStandardId, request.getStandardId());
        }
        Page<StandardGoodsSpecificationDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return page;
    }

    @Override
    public Page<StandardGoodsSpecificationDTO> getSpecificationPageByGoods(StandardSpecificationPageRequest request) {
        QueryWrapper<StandardGoodsSpecificationDO> wrapper = new QueryWrapper<>();
        if (null != request.getStandardId()) {
            wrapper.lambda().eq(StandardGoodsSpecificationDO::getStandardId, request.getStandardId());
        }
        if (StringUtils.isNotBlank(request.getStandardGoodsName())) {
            wrapper.lambda().like(StandardGoodsSpecificationDO::getName, request.getStandardGoodsName());
        }
        Page<StandardGoodsSpecificationDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, StandardGoodsSpecificationDTO.class);
    }

    @Override
    public List<StandardGoodsSpecificationDO> getStandardGoodsSpecificationBySpecificationOrBarcode(Long standardId, String specificationName, String barcode) {
        QueryWrapper<StandardGoodsSpecificationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().and(query -> {
            query.eq(StandardGoodsSpecificationDO::getStandardId, standardId);
            query.eq(StandardGoodsSpecificationDO::getSellSpecifications, specificationName);
        });
        if (StringUtils.isNotBlank(barcode)) {
            wrapper.lambda().or().eq(StandardGoodsSpecificationDO::getBarcode, barcode);
        }
        return this.list(wrapper);
    }

    @Override
    public Page<StandardGoodsSpecificationDTO> querySpecificationPage(StandardSpecificationPageRequest request) {
        QueryWrapper<StandardGoodsSpecificationDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getName())) {
            wrapper.lambda().eq(StandardGoodsSpecificationDO::getName, request.getName());
        }
        if (StringUtils.isNotBlank(request.getLicenseNo())) {
            wrapper.lambda().eq(StandardGoodsSpecificationDO::getLicenseNo, request.getLicenseNo());
        }
        Page<StandardGoodsSpecificationDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, StandardGoodsSpecificationDTO.class);
    }

    @Override
    public Page<StandardSpecificationGoodsInfoBO> getSpecificationGoodsInfoPage(StandardSpecificationPageRequest request) {
        return standardGoodsSpecificationMapper.getSpecificationGoodsInfoPage(new Page<>(request.getCurrent(), request.getSize()), request);
    }
}
