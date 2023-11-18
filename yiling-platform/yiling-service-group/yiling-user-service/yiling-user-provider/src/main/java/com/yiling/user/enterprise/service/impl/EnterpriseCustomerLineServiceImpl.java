package com.yiling.user.enterprise.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.dao.EnterpriseCustomerLineMapper;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerLineListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCustomerLineRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerLineDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerLineService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 企业客户使用产品线 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/29
 */
@Service
public class EnterpriseCustomerLineServiceImpl extends BaseServiceImpl<EnterpriseCustomerLineMapper, EnterpriseCustomerLineDO> implements EnterpriseCustomerLineService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(List<AddCustomerLineRequest> request) {
        List<EnterpriseCustomerLineDO> list = PojoUtils.map(request, EnterpriseCustomerLineDO.class);

        List<EnterpriseCustomerLineDO> lineDoList = list.stream().map(enterpriseCustomerLineDO -> {
            LambdaQueryWrapper<EnterpriseCustomerLineDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(EnterpriseCustomerLineDO::getCustomerId, enterpriseCustomerLineDO.getCustomerId());
            wrapper.eq(EnterpriseCustomerLineDO::getUseLine, enterpriseCustomerLineDO.getUseLine());
            int count = this.count(wrapper);
            if (count > 0) {
                return null;
            }
            return enterpriseCustomerLineDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return this.saveBatch(lineDoList);
    }

    @Override
    public boolean delete(List<Long> idList, Long opUserId) {
        EnterpriseCustomerLineDO customerLineDO = new EnterpriseCustomerLineDO();
        customerLineDO.setOpUserId(opUserId);
        customerLineDO.setOpTime(new Date());
        LambdaQueryWrapper<EnterpriseCustomerLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseCustomerLineDO::getId, idList);
        return this.batchDeleteWithFill(customerLineDO, wrapper) > 0;
    }

    @Override
    public boolean deleteByCustomerId(Long customerId, Long opUserId) {
        EnterpriseCustomerLineDO customerLineDO = new EnterpriseCustomerLineDO();
        customerLineDO.setOpUserId(opUserId);
        LambdaQueryWrapper<EnterpriseCustomerLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseCustomerLineDO::getCustomerId, customerId);
        return this.batchDeleteWithFill(customerLineDO, wrapper) > 0;
    }

    @Override
    public List<EnterpriseCustomerLineDO> queryList(QueryCustomerLineListRequest request) {
        LambdaQueryWrapper<EnterpriseCustomerLineDO> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getCustomerId()) && request.getCustomerId() != 0) {
            queryWrapper.eq(EnterpriseCustomerLineDO::getCustomerId, request.getCustomerId());
        }
        if (Objects.nonNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(EnterpriseCustomerLineDO::getEid, request.getEid());
        }
        if (Objects.nonNull(request.getCustomerEid()) && request.getCustomerEid() != 0) {
            queryWrapper.eq(EnterpriseCustomerLineDO::getCustomerEid, request.getCustomerEid());
        }

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLine(UpdateEnterpriseCustomerLineRequest request) {
        if (CollUtil.isEmpty(request.getUseLineList())) {
            return true;
        }

        //查询当前存在的企业客户产品线
        LambdaQueryWrapper<EnterpriseCustomerLineDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseCustomerLineDO::getCustomerId, request.getCustomerId());
        List<EnterpriseCustomerLineDO> list = this.list(queryWrapper);

        List<Integer> existLineList = list.stream().map(EnterpriseCustomerLineDO::getUseLine).collect(Collectors.toList());

        //需要新增
        List<Integer> addList = request.getUseLineList().stream().filter(line -> !existLineList.contains(line)).collect(Collectors.toList());
        this.saveLine(addList, request);
        //需要删除
        List<Integer> removeList = existLineList.stream().filter(line -> !request.getUseLineList().contains(line)).collect(Collectors.toList());
        this.removeLine(removeList, request);

        return true;
    }

    @Override
    public boolean getCustomerLineFlag(Long eid, Long customerEid, EnterpriseCustomerLineEnum lineEnum) {
        LambdaQueryWrapper<EnterpriseCustomerLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseCustomerLineDO::getEid, eid);
        wrapper.eq(EnterpriseCustomerLineDO::getCustomerEid, customerEid);
        wrapper.eq(EnterpriseCustomerLineDO::getUseLine, lineEnum.getCode());
        wrapper.last("limit 1");
        EnterpriseCustomerLineDO customerLineDO = this.getOne(wrapper);

        return Objects.nonNull(customerLineDO);
    }

    @Override
    public Map<Long, Boolean> getCustomerLineListFlag(List<Long> eidList, Long customerEid, EnterpriseCustomerLineEnum lineEnum) {
        Map<Long, Boolean> map = MapUtil.newHashMap();
        if (CollUtil.isEmpty(eidList) || Objects.isNull(customerEid)) {
            return map;
        }
        LambdaQueryWrapper<EnterpriseCustomerLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EnterpriseCustomerLineDO::getEid, eidList);
        wrapper.eq(EnterpriseCustomerLineDO::getCustomerEid, customerEid);
        wrapper.eq(EnterpriseCustomerLineDO::getUseLine, lineEnum.getCode());
        List<EnterpriseCustomerLineDO> customerLineDOList = this.list(wrapper);
        List<Long> list = customerLineDOList.stream().map(EnterpriseCustomerLineDO::getEid).collect(Collectors.toList());

        eidList.forEach(eid -> {
            if (list.contains(eid)) {
                map.put(eid, true);
            } else {
                map.put(eid, false);
            }
        });

        return map;
    }


    private void removeLine(List<Integer> removeList, UpdateEnterpriseCustomerLineRequest request) {
        if (CollUtil.isEmpty(removeList)) {
            return;
        }

        EnterpriseCustomerLineDO customerLineDO = new EnterpriseCustomerLineDO();
        customerLineDO.setOpUserId(request.getOpUserId());
        customerLineDO.setOpTime(new Date());
        LambdaQueryWrapper<EnterpriseCustomerLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseCustomerLineDO::getCustomerId, request.getCustomerId());
        wrapper.in(EnterpriseCustomerLineDO::getUseLine, removeList);
        this.batchDeleteWithFill(customerLineDO, wrapper);
    }

    private void saveLine(List<Integer> addList, UpdateEnterpriseCustomerLineRequest request) {
        if (CollUtil.isEmpty(addList)) {
            return;
        }

        List<EnterpriseCustomerLineDO> list = addList.stream().map(line -> {
            EnterpriseCustomerLineDO lineDO = PojoUtils.map(request, EnterpriseCustomerLineDO.class);
            lineDO.setOpUserId(request.getOpUserId());
            lineDO.setUseLine(line);
            return lineDO;
        }).collect(Collectors.toList());

        this.saveBatch(list);
    }
}
