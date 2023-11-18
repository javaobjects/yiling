package com.yiling.user.member.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStageRequest;
import com.yiling.user.member.service.MemberBuyStageService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员购买条件 API 实现
 *
 * @author: lun.yu
 * @date: 2022-08-11
 */
@Slf4j
@DubboService
public class MemberBuyStageApiImpl implements MemberBuyStageApi {

    @Autowired
    MemberBuyStageService memberBuyStageService;

    @Override
    public Map<String, String> getStageNameByOrderNo(List<String> orderNoList) {
        return memberBuyStageService.getStageNameByOrderNo(orderNoList);
    }

    @Override
    public Page<MemberBuyStageDTO> queryMemberBuyStagePage(QueryMemberBuyStagePageRequest request) {
        return memberBuyStageService.queryMemberBuyStagePage(request);
    }

    @Override
    public List<MemberBuyStageDTO> getStageByMemberName(String memberName) {
        return memberBuyStageService.getStageByMemberName(memberName);
    }

    @Override
    public List<MemberBuyStageDTO> listByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.toList();
        }
        return PojoUtils.map(memberBuyStageService.listByIds(ids), MemberBuyStageDTO.class);
    }

    @Override
    public MemberBuyStageDTO getById(Long id) {
        return PojoUtils.map(memberBuyStageService.getById(id), MemberBuyStageDTO.class);
    }

    @Override
    public MemberBuyStageDTO getBuyStageByCond(QueryMemberBuyStageRequest request) {
        return memberBuyStageService.getBuyStageByCond(request);
    }

}
