package com.yiling.user.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.integral.bo.IntegralExchangeGoodsItemBO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeGoodsPageRequest;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeOrderRequest;
import com.yiling.user.integral.dto.request.SaveIntegralExchangeGoodsRequest;
import com.yiling.user.integral.dto.request.UpdateShelfStatusRequest;
import com.yiling.user.integral.entity.IntegralExchangeGoodsDO;
import com.yiling.user.integral.dao.IntegralExchangeGoodsMapper;
import com.yiling.user.integral.entity.IntegralExchangeGoodsMemberDO;
import com.yiling.user.integral.entity.IntegralExchangeOrderDO;
import com.yiling.user.integral.enums.IntegralGoodsTypeEnum;
import com.yiling.user.integral.enums.IntegralShelfStatusEnum;
import com.yiling.user.integral.enums.IntegralUserFlagEnum;
import com.yiling.user.integral.service.IntegralExchangeGoodsMemberService;
import com.yiling.user.integral.service.IntegralExchangeGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralExchangeOrderService;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.AdminService;
import com.yiling.user.system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换商品表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Slf4j
@Service
public class IntegralExchangeGoodsServiceImpl extends BaseServiceImpl<IntegralExchangeGoodsMapper, IntegralExchangeGoodsDO> implements IntegralExchangeGoodsService {

    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    IntegralExchangeGoodsMemberService integralExchangeGoodsMemberService;
    @Autowired
    IntegralExchangeOrderService integralExchangeOrderService;

    @Override
    public Page<IntegralExchangeGoodsItemBO> queryListPage(QueryIntegralExchangeGoodsPageRequest request) {
        if (StrUtil.isNotEmpty(request.getCreateUserName())) {
            Admin admin = adminService.getByName(request.getCreateUserName());
            if (Objects.nonNull(admin)) {
                request.setCreateUser(admin.getId());
            }
        }
        if (StrUtil.isNotEmpty(request.getMobile())) {
            Admin admin = adminService.getByMobile(request.getMobile());
            if (Objects.nonNull(admin)) {
                request.setCreateUser(admin.getId());
            }
        }

        QueryWrapper<IntegralExchangeGoodsDO> wrapper = WrapperUtils.getWrapper(request);
        if (Objects.nonNull(request.getValidGoods()) && request.getValidGoods() == 1) {
            wrapper.lambda().gt(IntegralExchangeGoodsDO::getValidEndTime, new Date());
        }
        if (request.getOrderCond() == 1) {
            wrapper.lambda().orderByDesc(IntegralExchangeGoodsDO::getCreateTime);
        } else if (request.getOrderCond() == 2) {
            wrapper.lambda().orderByDesc(IntegralExchangeGoodsDO::getSort, IntegralExchangeGoodsDO::getUpdateTime);
        }

        Page<IntegralExchangeGoodsDO> exchangeGoodsDOPage = this.page(request.getPage(), wrapper);

        Page<IntegralExchangeGoodsItemBO> goodsItemBOPage = PojoUtils.map(exchangeGoodsDOPage, IntegralExchangeGoodsItemBO.class);
        // 用户信息
        List<Long> userIdList = goodsItemBOPage.getRecords().stream().map(IntegralExchangeGoodsItemBO::getCreateUser).distinct().collect(Collectors.toList());
        Map<Long, UserDO> userMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userIdList)) {
            userMap = userService.listByIds(userIdList).stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));
        }
        // 积分兑换订单信息
        List<Long> exchangeGoodsIdList = goodsItemBOPage.getRecords().stream().map(IntegralExchangeGoodsItemBO::getId).collect(Collectors.toList());
        Map<Long, List<IntegralExchangeOrderDO>> allExchangeOrderMap = MapUtil.newHashMap();
        Map<Long, List<IntegralExchangeOrderDO>> recentExchangeOrderMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(exchangeGoodsIdList)) {
            // 兑换商品ID对应的所有订单数据
            QueryIntegralExchangeOrderRequest exchangeOrderRequest = new QueryIntegralExchangeOrderRequest();
            exchangeOrderRequest.setExchangeGoodsId(exchangeGoodsIdList);
            allExchangeOrderMap = integralExchangeOrderService.getByExchangeGoodsIdList(exchangeOrderRequest);
            // 近30天的订单数据
            exchangeOrderRequest.setStartSubmitTime(DateUtil.offsetDay(new Date(), -30));
            exchangeOrderRequest.setEndSubmitTime(new Date());
            recentExchangeOrderMap = integralExchangeOrderService.getByExchangeGoodsIdList(exchangeOrderRequest);
        }

        Map<Long, UserDO> finalUserMap = userMap;
        Map<Long, List<IntegralExchangeOrderDO>> finalRecentExchangeOrderMap = recentExchangeOrderMap;
        Map<Long, List<IntegralExchangeOrderDO>> finalAllExchangeOrderMap = allExchangeOrderMap;
        goodsItemBOPage.getRecords().forEach(goodsItemBO -> {
            UserDO userDO = finalUserMap.getOrDefault(goodsItemBO.getCreateUser(), new UserDO());
            goodsItemBO.setCreateUserName(userDO.getName());
            goodsItemBO.setMobile(userDO.getMobile());
            // 近30天兑换数量
            Integer exchangeNum = finalRecentExchangeOrderMap.getOrDefault(goodsItemBO.getId(), ListUtil.toList()).stream().mapToInt(IntegralExchangeOrderDO::getExchangeNum).sum();
            goodsItemBO.setRecentExchangeNum(exchangeNum);
            // 剩余可兑换数量
            Integer allExchangeNum = finalAllExchangeOrderMap.getOrDefault(goodsItemBO.getId(), ListUtil.toList()).stream().mapToInt(IntegralExchangeOrderDO::getExchangeNum).sum();
            goodsItemBO.setCanExchangeNum(goodsItemBO.getCanExchangeNum() - allExchangeNum);
        });

        return goodsItemBOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateShelfStatusRequest request) {
        IntegralExchangeGoodsDO exchangeGoodsDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_NOT_EXIST));
        // 上下架有效期校验
        if (IntegralShelfStatusEnum.getByCode(request.getStatus()) == IntegralShelfStatusEnum.SHELF) {
            if (!DateUtil.isIn(new Date(), exchangeGoodsDO.getValidStartTime(), exchangeGoodsDO.getValidEndTime())) {
                throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_VALID_TIME_ERROR);
            }
        }

        IntegralExchangeGoodsDO integralExchangeGoodsDO = new IntegralExchangeGoodsDO();
        integralExchangeGoodsDO.setId(exchangeGoodsDO.getId());
        integralExchangeGoodsDO.setShelfStatus(request.getStatus());
        integralExchangeGoodsDO.setOpUserId(request.getOpUserId());
        return this.updateById(integralExchangeGoodsDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveExchangeGoods(SaveIntegralExchangeGoodsRequest request) {
        // 根据商品类型和ID获取兑换商品
        IntegralExchangeGoodsDO integralExchangeGoodsDO = this.getByGoodsTypeAndId(request);

        IntegralExchangeGoodsDO exchangeGoodsDO = PojoUtils.map(request, IntegralExchangeGoodsDO.class);
        if (Objects.isNull(request.getId()) || request.getId() == 0) {
            // 积分兑换商品重复校验
            if (Objects.nonNull(integralExchangeGoodsDO)) {
                throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_EXIST);
            }
            exchangeGoodsDO.setShelfStatus(IntegralShelfStatusEnum.SHELF.getCode());
            this.save(exchangeGoodsDO);

        } else {
            if (Objects.nonNull(integralExchangeGoodsDO) && exchangeGoodsDO.getId().compareTo(integralExchangeGoodsDO.getId()) != 0) {
                throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_EXIST);
            }
            this.updateById(exchangeGoodsDO);
        }

        LambdaQueryWrapper<IntegralExchangeGoodsMemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralExchangeGoodsMemberDO::getExchangeGoodsId, exchangeGoodsDO.getId());
        IntegralExchangeGoodsMemberDO exchangeGoodsMemberDO = new IntegralExchangeGoodsMemberDO();
        exchangeGoodsMemberDO.setOpUserId(request.getOpUserId());
        integralExchangeGoodsMemberService.batchDeleteWithFill(exchangeGoodsMemberDO, wrapper);

        if (IntegralUserFlagEnum.getByCode(request.getUserFlag()) == IntegralUserFlagEnum.ASSIGN) {
            List<Long> memberIdList = request.getMemberIdList();
            if (CollUtil.isNotEmpty(memberIdList)) {
                List<IntegralExchangeGoodsMemberDO> goodsMemberDOList = memberIdList.stream().map(memberId -> {
                    IntegralExchangeGoodsMemberDO goodsMemberDO = new IntegralExchangeGoodsMemberDO();
                    goodsMemberDO.setExchangeGoodsId(exchangeGoodsDO.getId());
                    goodsMemberDO.setMemberId(memberId);
                    goodsMemberDO.setOpUserId(request.getOpUserId());
                    return goodsMemberDO;
                }).collect(Collectors.toList());

                integralExchangeGoodsMemberService.saveBatch(goodsMemberDOList);
            }

        }

        return true;
    }

    /**
     * 根据商品类型和ID获取兑换商品
     *
     * @param request
     * @return
     */
    private IntegralExchangeGoodsDO getByGoodsTypeAndId(SaveIntegralExchangeGoodsRequest request) {
        LambdaQueryWrapper<IntegralExchangeGoodsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IntegralExchangeGoodsDO::getGoodsType, request.getGoodsType());
        queryWrapper.eq(IntegralExchangeGoodsDO::getGoodsId, request.getGoodsId());
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

}
