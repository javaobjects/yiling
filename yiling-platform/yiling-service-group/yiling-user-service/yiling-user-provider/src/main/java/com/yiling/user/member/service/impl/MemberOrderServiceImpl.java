package com.yiling.user.member.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.enums.MemberOrderStatusEnum;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.member.dao.MemberOrderMapper;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.entity.MemberOrderDO;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberOrderCouponService;
import com.yiling.user.member.service.MemberOrderService;
import com.yiling.user.member.service.MemberService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员订单表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-20
 */
@Slf4j
@Service
public class MemberOrderServiceImpl extends BaseServiceImpl<MemberOrderMapper, MemberOrderDO> implements MemberOrderService {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    MemberOrderCouponService memberOrderCouponService;

    @Override
    public Page<MemberOrderDTO> queryMemberOrderPage(QueryMemberOrderPageRequest request) {
        QueryWrapper<MemberOrderDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().eq(MemberOrderDO::getCancelFlag, 0);
        return PojoUtils.map(this.page(request.getPage(), wrapper), MemberOrderDTO.class);
    }

    @Override
    public MemberOrderDTO getMemberOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<MemberOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberOrderDO::getOrderNo, orderNo);
        queryWrapper.eq(MemberOrderDO::getCancelFlag, 0);
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), MemberOrderDTO.class);
    }

    @Override
    public Map<String, Long> getStageByOrderList(List<String> orderNoList) {
        if (CollUtil.isEmpty(orderNoList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<MemberOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MemberOrderDO::getOrderNo, orderNoList);

        return list(queryWrapper).stream().collect(Collectors.toMap(MemberOrderDO::getOrderNo, MemberOrderDO::getBuyStageId, (k1, k2) -> k2));
    }

    @Override
    public List<MemberOrderDTO> getMemberOrderByOrderNoList(List<String> orderNoList) {
        if (CollUtil.isEmpty(orderNoList)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<MemberOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MemberOrderDO::getOrderNo, orderNoList);
        wrapper.eq(MemberOrderDO::getCancelFlag, 0);
        return PojoUtils.map(this.list(wrapper), MemberOrderDTO.class);
    }

    @Override
    public List<MemberOrderDTO> getMemberOrderByStageId(Long buyStageId) {
        LambdaQueryWrapper<MemberOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberOrderDO::getBuyStageId, buyStageId);
        queryWrapper.eq(MemberOrderDO::getStatus, MemberOrderStatusEnum.PAY_SUCCESS.getCode());
        queryWrapper.eq(MemberOrderDO::getCancelFlag, 0);
        List<MemberOrderDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, MemberOrderDTO.class);
    }

}
