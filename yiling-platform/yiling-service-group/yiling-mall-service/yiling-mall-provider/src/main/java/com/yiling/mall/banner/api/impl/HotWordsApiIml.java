package com.yiling.mall.banner.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.HotWordsApi;
import com.yiling.mall.banner.dto.B2bAppHotWordsDTO;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsSaveRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsWeightRequest;
import com.yiling.mall.banner.entity.B2bAppHotWordsDO;
import com.yiling.mall.banner.service.B2bAppHotWordsService;

/**
 * @author: yong.zhang
 * @date: 2021/10/25
 */
@DubboService
public class HotWordsApiIml implements HotWordsApi {
    @Autowired
    private B2bAppHotWordsService hotWordsService;

    @Override
    public B2bAppHotWordsDTO queryById(Long id) {
        B2bAppHotWordsDO b2bAppHotWordsDO = hotWordsService.getById(id);
        return PojoUtils.map(b2bAppHotWordsDO, B2bAppHotWordsDTO.class);
    }

    @Override
    public boolean saveB2bAppHotWords(B2bAppHotWordsSaveRequest request) {
        return hotWordsService.saveB2bAppHotWords(request);
    }

    @Override
    public boolean editB2bAppHotWordsWeight(B2bAppHotWordsWeightRequest request) {
        return hotWordsService.editWeight(request.getId(), request.getSort(), request.getOpUserId());
    }

    @Override
    public boolean editB2bAppHotWordsStatus(B2bAppHotWordsStatusRequest request) {
        return hotWordsService.editStatus(request.getId(), request.getUseStatus(), request.getOpUserId());
    }

    @Override
    public Page<B2bAppHotWordsDTO> pageList(B2bAppHotWordsPageRequest request) {
        return hotWordsService.pageList(request);
    }

    @Override
    public List<B2bAppHotWordsDTO> listAll(int source) {
        return hotWordsService.listByStatus(1, source);
    }
}
