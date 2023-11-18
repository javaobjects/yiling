package com.yiling.mall.navigation.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.navigation.dao.NavigationInfoMapper;
import com.yiling.mall.navigation.dto.NavigationInfoDTO;
import com.yiling.mall.navigation.dto.NavigationInfoFrontDTO;
import com.yiling.mall.navigation.dto.request.QueryNavigationInfoPageRequest;
import com.yiling.mall.navigation.dto.request.SaveNavigationInfoRequest;
import com.yiling.mall.navigation.dto.request.UpdateNavigationInfoRequest;
import com.yiling.mall.navigation.entity.NavigationInfoDO;
import com.yiling.mall.navigation.service.NavigationInfoService;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 导航信息表 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-06-15
 */
@Service
public class NavigationInfoServiceImpl extends BaseServiceImpl<NavigationInfoMapper, NavigationInfoDO> implements NavigationInfoService {

    /**
     * 获取分页信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<NavigationInfoDTO> getNavigationInfo(QueryNavigationInfoPageRequest request) {
        QueryWrapper<NavigationInfoDO> wrapper = new QueryWrapper<>();
        if(request.getState() != null){
            wrapper.lambda().eq(NavigationInfoDO :: getState,request.getState());
        }
        if(request.getStartCreateTime() != null){

            wrapper.lambda().ge(NavigationInfoDO :: getCreateTime,DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if(request.getEndCreateTime() != null){
            wrapper.lambda().le(NavigationInfoDO :: getCreateTime,DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(StringUtils.isNotBlank(request.getName())){
            wrapper.lambda().like(NavigationInfoDO :: getName,request.getName());
        }

        if(request.getEid() != null && request.getEid()!=0){
            wrapper.lambda().eq(NavigationInfoDO :: getEid,request.getEid());
        }
        wrapper.lambda().orderByDesc(NavigationInfoDO::getSort).orderByAsc(NavigationInfoDO :: getCreateTime);
        Page<NavigationInfoDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<NavigationInfoDTO> dtoPage = PojoUtils.map(page, NavigationInfoDTO.class);
        return dtoPage;
    }

    /**
     * 保存导航信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveNavigationInfo(SaveNavigationInfoRequest request) {
        if(request.getEid() == null){
            request.setEid(Constants.YILING_EID);
        }
        NavigationInfoDO navigationInfo = PojoUtils.map(request, NavigationInfoDO.class);
        return save(navigationInfo);
    }

    /**
     * 修改导航信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateNavigationInfo(UpdateNavigationInfoRequest request) {
        NavigationInfoDO navigationInfo = PojoUtils.map(request, NavigationInfoDO.class);
        return updateById(navigationInfo);
    }

    /**
     * POP首页获取导航信息
     *
     * @param number
     * @return
     */
    @Override
    public List<NavigationInfoFrontDTO> getNavigationInfoFront(Integer number) {
        QueryWrapper<NavigationInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NavigationInfoDO :: getState,1)
                .eq(NavigationInfoDO :: getEid,Constants.YILING_EID)
                .orderByDesc(NavigationInfoDO::getSort).orderByAsc(NavigationInfoDO :: getCreateTime);
        if(-1 != number){
            String limit = "limit "+number;
            wrapper.last(limit);
        }
        List<NavigationInfoDO> list = list(wrapper);
        return PojoUtils.map(list,NavigationInfoFrontDTO.class) ;
    }
}
