package com.yiling.user.member.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.member.dao.MemberBuyStageMapper;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStageRequest;
import com.yiling.user.member.entity.MemberBuyStageDO;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberOrderService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.yiling.user.member.service.MemberOrderService;
import com.yiling.user.member.service.MemberService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员购买条件 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Slf4j
@Service
public class MemberBuyStageServiceImpl extends BaseServiceImpl<MemberBuyStageMapper, MemberBuyStageDO> implements MemberBuyStageService {

    @Autowired
    MemberOrderService memberOrderService;
    @Autowired
    MemberBuyRecordService memberBuyRecordService;
    @Autowired
    private MemberService memberService;

    @Override
    public Map<Long, List<Long>> getEidListByBuyStageId(List<Long> buyStageIdList) {
        if (CollUtil.isEmpty(buyStageIdList)) {
            return MapUtil.newHashMap();
        }

        Map<Long, List<Long>> map = MapUtil.newHashMap();
        buyStageIdList.forEach(buyStageId -> {
            List<MemberOrderDTO> memberOrderDTOList = memberOrderService.getMemberOrderByStageId(buyStageId);
            List<String> orderNoList = memberOrderDTOList.stream().map(MemberOrderDTO::getOrderNo).collect(Collectors.toList());

            List<Long> eidList = memberBuyRecordService.getEidByOrderNoList(orderNoList);
            map.put(buyStageId, eidList);
        });

        return map;
    }

    @Override
    public MemberBuyStageDTO getBuyStageByCond(QueryMemberBuyStageRequest request) {
        QueryWrapper<MemberBuyStageDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), MemberBuyStageDTO.class);
    }

    @Override
    public Map<String, String> getStageNameByOrderNo(List<String> orderNoList) {
        Map<String, Long> stageByOrderMap = memberOrderService.getStageByOrderList(orderNoList);
        if (CollUtil.isEmpty(stageByOrderMap)) {
            return MapUtil.newHashMap();
        }

        // 获取购买条件名称
        List<MemberBuyStageDO> memberBuyStageDOList = this.listByIds(stageByOrderMap.values());
        Map<Long, String> stageMap = memberBuyStageDOList.stream().collect(Collectors.toMap(BaseDO::getId, MemberBuyStageDO::getName, (k1, k2) -> k2));

        Map<String, String> orderStageMap = MapUtil.newConcurrentHashMap();
        stageByOrderMap.forEach((orderNo, stageId) -> orderStageMap.put(orderNo, stageMap.getOrDefault(stageId, "")));

        return orderStageMap;
    }

    @Override
    public Page<MemberBuyStageDTO> queryMemberBuyStagePage(QueryMemberBuyStagePageRequest request) {
        LambdaQueryWrapper<MemberBuyStageDO> wrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(request.getMemberIdList())) {
            wrapper.in(MemberBuyStageDO::getMemberId, request.getMemberIdList());
        }
        if (CollUtil.isNotEmpty(request.getStageIdList())) {
            wrapper.in(MemberBuyStageDO::getId, request.getStageIdList());
        }
        if (StrUtil.isNotEmpty(request.getMemberName())) {
            List<Long> idList = this.getMemberIdListByName(request.getMemberName());
            if (CollUtil.isNotEmpty(idList)) {
                wrapper.in(MemberBuyStageDO::getMemberId, idList);
            }
        }
        Page<MemberBuyStageDO> stageDOPage = this.page(request.getPage(), wrapper);

        List<Long> memberIdList = stageDOPage.getRecords().stream().map(MemberBuyStageDO::getMemberId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(memberIdList)) {
            Map<Long, String> nameMap = memberService.listByIds(memberIdList).stream().collect(Collectors.toMap(BaseDO::getId, MemberDO::getName, (k1, k2) -> k2));
            stageDOPage.getRecords().forEach(memberBuyStageDO -> memberBuyStageDO.setMemberName(nameMap.getOrDefault(memberBuyStageDO.getMemberId(), memberBuyStageDO.getMemberName())));
        }

        return PojoUtils.map(stageDOPage, MemberBuyStageDTO.class);
    }


    private List<Long> getMemberIdListByName(String memberName){
        LambdaQueryWrapper<MemberDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(memberName)) {
            wrapper.like(MemberDO::getName, memberName);
        }
        // 根据会员名称查询会员信息
        List<MemberDO> memberDOList = memberService.list(wrapper);
        return memberDOList.stream().map(BaseDO::getId).collect(Collectors.toList());
    }


    @Override
    public List<MemberBuyStageDTO> getStageByMemberName(String memberName) {
        LambdaQueryWrapper<MemberDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(memberName)) {
            wrapper.like(MemberDO::getName, memberName);
        }
        // 根据会员名称查询会员信息
        List<MemberDO> memberDOList = memberService.list(wrapper);
        List<Long> idList = memberDOList.stream().map(BaseDO::getId).collect(Collectors.toList());
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.toList();
        }
        Map<Long, String> nameMap = memberDOList.stream().collect(Collectors.toMap(MemberDO::getId, MemberDO::getName, (k1, k2) -> k2));

        // 根据会员ID查询出购买条件
        LambdaQueryWrapper<MemberBuyStageDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MemberBuyStageDO::getMemberId, idList);
        List<MemberBuyStageDO> stageDOList = this.list(queryWrapper);
        stageDOList.forEach(memberBuyStageDO -> memberBuyStageDO.setMemberName(nameMap.getOrDefault(memberBuyStageDO.getMemberId(), memberBuyStageDO.getMemberName())));

        return PojoUtils.map(stageDOList, MemberBuyStageDTO.class);
    }

}
