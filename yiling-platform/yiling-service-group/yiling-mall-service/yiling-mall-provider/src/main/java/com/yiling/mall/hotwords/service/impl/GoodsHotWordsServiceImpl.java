package com.yiling.mall.hotwords.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.hotwords.dao.GoodsHotWordsMapper;
import com.yiling.mall.hotwords.dto.GoodsHotWordsAvailableDTO;
import com.yiling.mall.hotwords.dto.GoodsHotWordsDTO;
import com.yiling.mall.hotwords.dto.request.QueryGoodsHotWordsPageRequest;
import com.yiling.mall.hotwords.dto.request.SaveGoodsHotWordsRequest;
import com.yiling.mall.hotwords.dto.request.UpdateGoodsHotWordsRequest;
import com.yiling.mall.hotwords.entity.GoodsHotWordsDO;
import com.yiling.mall.hotwords.service.GoodsHotWordsService;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 商品热词表 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-06-11
 */
@Service
public class GoodsHotWordsServiceImpl extends BaseServiceImpl<GoodsHotWordsMapper, GoodsHotWordsDO> implements GoodsHotWordsService {

    /**
     * 获取热词分页信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<GoodsHotWordsDTO> getGoodsHotWordsPage(QueryGoodsHotWordsPageRequest request) {
        QueryWrapper<GoodsHotWordsDO> wrapper = new QueryWrapper<>();
        if(request.getState() != null && request.getState() != 0 ){
            wrapper.lambda().eq(GoodsHotWordsDO :: getState,request.getState());
        }
        if(request.getStartCreateTime() != null){
            wrapper.lambda().ge(GoodsHotWordsDO :: getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if(request.getEndCreateTime() != null){
            wrapper.lambda().le(GoodsHotWordsDO :: getCreateTime,DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(StringUtils.isNotBlank(request.getName())){
            wrapper.lambda().like(GoodsHotWordsDO :: getName,request.getName());
        }
        if(request.getStartTime() != null){
            wrapper.lambda().ge(GoodsHotWordsDO :: getStartTime,DateUtil.beginOfDay(request.getStartTime()));
        }
        if(request.getEndTime() != null){
            wrapper.lambda().le(GoodsHotWordsDO :: getEndTime,DateUtil.endOfDay(request.getEndTime()));
        }
        if(request.getEid() != null && request.getEid() != 0){
            wrapper.lambda().eq(GoodsHotWordsDO :: getEid,request.getEid());
        }
        wrapper.lambda().orderByDesc(GoodsHotWordsDO::getSort)
                .orderByAsc(GoodsHotWordsDO :: getEndTime,GoodsHotWordsDO :: getStartTime,GoodsHotWordsDO :: getCreateTime);
        Page<GoodsHotWordsDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<GoodsHotWordsDTO> dtoPage = PojoUtils.map(page, GoodsHotWordsDTO.class);
        return dtoPage;
    }

    /**
     * 根据id查询热词详情
     *
     * @param id
     * @return
     */
    @Override
    public GoodsHotWordsDO getGoodsHotWordsDetails(Long id) {
        GoodsHotWordsDO one = this.getById(id);
        return one;
    }

    /**
     * 修改商品信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateGoodsHotWordsById(UpdateGoodsHotWordsRequest request) {
        if(request.getStartTime() != null){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(request.getEndTime() != null){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        GoodsHotWordsDO words = PojoUtils.map(request, GoodsHotWordsDO.class);
        return updateById(words);
    }

    /**
     * 保存热词信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveGoodsHotWords(SaveGoodsHotWordsRequest request) {
        if(request.getEid() == null){
            request.setEid(Constants.YILING_EID);
        }
        if(request.getStartTime() != null){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(request.getEndTime() != null){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        GoodsHotWordsDO words = PojoUtils.map(request, GoodsHotWordsDO.class);
        return save(words);
    }

    /**
     * 获取可用热词
     *
     * @param number
     * @return
     */
    @Override
    public List<GoodsHotWordsAvailableDTO> getAvailableGoodsHotWords(Integer number) {
        QueryWrapper<GoodsHotWordsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsHotWordsDO :: getState,1)
                .eq(GoodsHotWordsDO :: getEid,Constants.YILING_EID)
                .le(GoodsHotWordsDO :: getStartTime,new Date())
                .ge(GoodsHotWordsDO :: getEndTime,new Date())
                .orderByDesc(GoodsHotWordsDO::getSort)
                .orderByAsc(GoodsHotWordsDO :: getEndTime,GoodsHotWordsDO :: getStartTime,GoodsHotWordsDO :: getCreateTime);
        if(-1 != number){
            String limit = " limit "+number;
            wrapper.last(limit);
        }
        List<GoodsHotWordsDO> list = this.list(wrapper);
        return PojoUtils.map(list,GoodsHotWordsAvailableDTO.class);
    }


}
