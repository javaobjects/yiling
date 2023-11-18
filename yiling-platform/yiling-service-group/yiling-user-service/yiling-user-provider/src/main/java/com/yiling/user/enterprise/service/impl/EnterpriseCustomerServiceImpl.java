package com.yiling.user.enterprise.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerEasDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerPaymentMethodDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.entity.EnterprisePurchaseApplyDO;
import com.yiling.user.enterprise.enums.EnterpriseCustomerSourceEnum;
import com.yiling.user.enterprise.service.EnterpriseCustomerEasService;
import com.yiling.user.enterprise.service.EnterpriseCustomerLineService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.bo.ChannelCustomerBO;
import com.yiling.user.enterprise.bo.GroupCustomerNumBO;
import com.yiling.user.enterprise.dao.EnterpriseCustomerMapper;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.MoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListByContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SaveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SavePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerContactDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseCustomerContactService;
import com.yiling.user.enterprise.service.EnterpriseCustomerPaymentMethodService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.enterprise.service.EnterprisePurchaseApplyService;
import com.yiling.user.enterprise.service.EnterprisePurchaseRelationService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.system.bo.BaseUser;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业客户信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
@Slf4j
@Service
public class EnterpriseCustomerServiceImpl extends BaseServiceImpl<EnterpriseCustomerMapper, EnterpriseCustomerDO> implements EnterpriseCustomerService {

    @Autowired
    private EnterpriseCustomerContactService enterpriseCustomerContactService;
    @Autowired
    private EnterpriseCustomerPaymentMethodService customerPaymentMethodService;
    @Autowired
    private EnterprisePurchaseRelationService enterprisePurchaseRelationService;
    @Autowired
    private EnterpriseCustomerEasService enterpriseCustomerEasService;
    @Autowired
    private EnterprisePurchaseApplyService enterprisePurchaseApplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseCustomerLineService enterpriseCustomerLineService;
    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Override
    public Page<EnterpriseCustomerDO> pageList(QueryCustomerPageListRequest request) {
        setUnionFlag(request);
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public List<EnterpriseCustomerDO> queryList(QueryCustomerPageListRequest request) {
        setUnionFlag(request);
        return this.baseMapper.pageList(request);
    }

    private void setUnionFlag(QueryCustomerPageListRequest request) {
        if (StrUtil.isNotEmpty(request.getName()) || StrUtil.isNotEmpty(request.getContactorPhone()) || StrUtil.isNotEmpty(request.getName())
                || CollUtil.isNotEmpty(request.getChannelIds()) || StrUtil.isNotEmpty(request.getProvinceCode()) || StrUtil.isNotEmpty(request.getCityCode())
                || StrUtil.isNotEmpty(request.getRegionCode()) || StrUtil.isNotEmpty(request.getLicenseNumber()) || (Objects.nonNull(request.getType()) && request.getType() != 0) ) {
            request.setUnionFlag(1);
        }
    }

    @Override
    public EnterpriseCustomerDO get(Long eid, Long customerEid) {
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerDO::getEid, eid)
                .eq(EnterpriseCustomerDO::getCustomerEid, customerEid)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EnterpriseCustomerDO> listByEidAndCustomerEids(Long eid, List<Long> customerEids) {
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerDO::getEid, eid)
                .in(EnterpriseCustomerDO::getCustomerEid, customerEids);

        List<EnterpriseCustomerDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public List<EnterpriseCustomerDO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid) {
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(EnterpriseCustomerDO::getEid, eids)
                .eq(EnterpriseCustomerDO::getCustomerEid, customerEid);

        List<EnterpriseCustomerDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public boolean add(AddCustomerRequest request) {
        this.addCustomer(request);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addCustomer(AddCustomerRequest request) {
        EnterpriseCustomerDO entity = this.get(request.getEid(), request.getCustomerEid());
        if (entity == null) {
            entity = PojoUtils.map(request, EnterpriseCustomerDO.class);
            this.save(entity);
        }

        if (request.isAddPurchaseRelationFlag()) {
            SavePurchaseRelationFormRequest request1 = new SavePurchaseRelationFormRequest();
            request1.setBuyerId(request.getCustomerEid());
            request1.setSellerIds(ListUtil.toList(request.getEid()));
            request1.setSource(1);
            request1.setOpUserId(request.getOpUserId());
            enterprisePurchaseRelationService.addPurchaseRelation(request1);

            //二级商向一级商采购或KA向一二级采购时，默认添加支付方式：线下支付
            EnterpriseChannelEnum eidChannelEnum = EnterpriseChannelEnum.getByCode(enterpriseService.getById(request.getEid()).getChannelId());
            if(eidChannelEnum == EnterpriseChannelEnum.LEVEL_1 || eidChannelEnum == EnterpriseChannelEnum.LEVEL_2){
                customerPaymentMethodService.saveCustomerPaymentMethods(request.getEid(), request.getCustomerEid(), ListUtil.toList(1L), PlatformEnum.POP, request.getOpUserId());
            }
        }

        //根据来源创建企业客户使用产品线
        saveEnterpriseCustomerLineBySource(request, entity);

        return entity.getId();
    }

    /**
     * 根据企业客户来源创建企业客户使用产品线
     * @param request
     * @param entity
     */
    private void saveEnterpriseCustomerLineBySource(AddCustomerRequest request, EnterpriseCustomerDO entity) {
        if (Objects.nonNull(request.getSource()) && request.getSource() != 0) {

            AddCustomerLineRequest lineRequest = new AddCustomerLineRequest();
            lineRequest.setCustomerId(entity.getId());
            lineRequest.setEid(request.getEid());
            lineRequest.setEname(enterpriseService.getById(request.getEid()).getName());
            lineRequest.setCustomerEid(request.getCustomerEid());
            lineRequest.setCustomerName(request.getCustomerName());

            if (EnterpriseCustomerSourceEnum.getByCode(request.getSource()) == EnterpriseCustomerSourceEnum.AGREEMENT_CREATE ||
                    EnterpriseCustomerSourceEnum.getByCode(request.getSource()) == EnterpriseCustomerSourceEnum.OPEN_POP ||
                    EnterpriseCustomerSourceEnum.getByCode(request.getSource()) == EnterpriseCustomerSourceEnum.IMPORT) {

                lineRequest.setUseLine(EnterpriseCustomerLineEnum.POP.getCode());
                enterpriseCustomerLineService.add(ListUtil.toList(lineRequest));

            } else if (EnterpriseCustomerSourceEnum.getByCode(request.getSource()) == EnterpriseCustomerSourceEnum.ERP_SYNC ||
                    EnterpriseCustomerSourceEnum.getByCode(request.getSource()) == EnterpriseCustomerSourceEnum.ONLINE_PURCHASE) {

                lineRequest.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
                enterpriseCustomerLineService.add(ListUtil.toList(lineRequest));

            }
        }
    }

    @Override
    public Map<Long, Long> countGroupCustomers(List<Long> groupIds) {
        if (CollUtil.isEmpty(groupIds)) {
            return MapUtil.empty();
        }

        List<GroupCustomerNumBO> list = this.baseMapper.countGroupCustomers(groupIds);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        return list.stream().collect(Collectors.toMap(GroupCustomerNumBO::getGroupId, GroupCustomerNumBO::getCustomerNum,(k1,k2) -> k2));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addGroupCustomers(SaveGroupCustomersRequest request) {
        if (this.hasCustomerJoinedGroup(request.getEid(), request.getCustomerEids())) {
            throw new BusinessException(UserErrorCode.CUSTOMER_HAS_JOINED_GROUP);
        }

        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerDO::getEid, request.getEid())
                .in(EnterpriseCustomerDO::getCustomerEid, request.getCustomerEids());

        EnterpriseCustomerDO entity = new EnterpriseCustomerDO();
        entity.setCustomerGroupId(request.getGroupId());
        entity.setOpUserId(request.getOpUserId());
        log.info("客户分组管理，企业ID：{}，分组ID：{}，向分组中添加查询结果数量：{}", request.getEid(), request.getGroupId(), request.getCustomerEids().size());

        return this.update(entity, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGroupCustomers(RemoveGroupCustomersRequest request) {
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerDO::getEid, request.getEid())
                .in(EnterpriseCustomerDO::getCustomerEid, request.getCustomerEids());

        EnterpriseCustomerDO entity = new EnterpriseCustomerDO();
        entity.setCustomerGroupId(0L);
        entity.setOpUserId(request.getOpUserId());

        return this.update(entity, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveGroupCustomers(MoveGroupCustomersRequest request) {
        //校验原分组中是否存在数据
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseCustomerDO::getCustomerGroupId, request.getOriginalGroupId());
        int count = this.count(queryWrapper);
        if(count <= 0){
            throw new BusinessException(UserErrorCode.CUSTOMER_GROUP_NOT_CUSTOMER);
        }

        EnterpriseCustomerDO entity = new EnterpriseCustomerDO();
        entity.setCustomerGroupId(request.getTargetGroupId());
        entity.setOpUserId(request.getOpUserId());

        return this.update(entity, queryWrapper);
    }

    @Override
    public Page<ChannelCustomerBO> pageChannelCustomerList(QueryChannelCustomerPageListRequest request) {
        //根据商务联系人名称取到ID去做精准查询条件
        if(StrUtil.isNotEmpty(request.getContactUserName())){
            QueryStaffListRequest listRequest = new QueryStaffListRequest();
            listRequest.setNameEq(request.getContactUserName());
            List<Staff> staffList = staffService.list(listRequest);
            if (CollUtil.isNotEmpty(staffList)) {
                List<EnterpriseEmployeeDO> employeeDOList = enterpriseEmployeeService.listByEidUserIds(request.getEid(), staffList.stream().map(BaseUser::getId).collect(Collectors.toList()));
                if (CollUtil.isNotEmpty(employeeDOList)) {
                    request.setContactUserId(employeeDOList.get(0).getUserId());
                }
            }
        }

        Page<ChannelCustomerBO> channelCustomerBoPage = this.baseMapper.pageChannelCustomerList(request.getPage(), request);
        // 设置特殊字段值
        setValue(channelCustomerBoPage.getRecords());

        return channelCustomerBoPage;
    }

    @Override
    public List<ChannelCustomerBO> queryChannelCustomerList(QueryChannelCustomerPageListRequest request) {
        //根据商务联系人名称取到ID去做精准查询条件
        if(StrUtil.isNotEmpty(request.getContactUserName())){
            UserDO userDO = Optional.ofNullable(userService.getOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getName, request.getContactUserName()))).orElse(new UserDO());
            request.setContactUserId(userDO.getId());
        }

        List<ChannelCustomerBO> channelCustomerBOList = this.baseMapper.pageChannelCustomerList(request);
        // 设置特殊字段值
        setValue(channelCustomerBOList);

        return channelCustomerBOList;
    }

    /**
     * 设置特殊字段值
     *
     * @param channelCustomerBOList 结果集
     */
    private void setValue(List<ChannelCustomerBO> channelCustomerBOList) {
        channelCustomerBOList.forEach(e -> {
            if (Objects.nonNull(e.getChannelId()) && e.getChannelId() != 0) {
                e.setChannelName(Objects.requireNonNull(EnterpriseChannelEnum.getByCode(e.getChannelId())).getName());
            }
            if (Objects.nonNull(e.getType()) && e.getType() != 0) {
                e.setCustomerType(Objects.requireNonNull(EnterpriseTypeEnum.getByCode(e.getType())).getName());
            }
        });
    }

    @Override
    public ChannelCustomerBO getChannelCustomer(Long eid, Long customerEid) {
        Assert.notNull(eid, "获取渠道商详情：用户企业ID参数为空号！");
        Assert.notNull(customerEid, "获取渠道商详情：渠道商企业ID参数为空号！");

        QueryChannelCustomerPageListRequest request = new QueryChannelCustomerPageListRequest().setCustomerEid(customerEid).setEids(Collections.singletonList(eid));
        ChannelCustomerBO channelCustomerBO = this.baseMapper.getChannelCustomer(request);
        if (Objects.nonNull(channelCustomerBO.getChannelId())) {
            channelCustomerBO.setChannelName(Objects.requireNonNull(EnterpriseChannelEnum.getByCode(channelCustomerBO.getChannelId())).getName());
        }
        return channelCustomerBO;
    }

    @Override
    public Map<Long, Long> countCustomersByEids(List<Long> eids) {
        if (CollUtil.isEmpty(eids)) {
            return MapUtil.empty();
        }
        List<EnterpriseCustomerDO> list = this.baseMapper.selectList(new LambdaQueryWrapper<EnterpriseCustomerDO>().in(EnterpriseCustomerDO::getEid, eids));
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }
        return list.stream().collect(Collectors.groupingBy(EnterpriseCustomerDO::getEid, Collectors.counting()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveChannelCustomer(UpdateCustomerInfoRequest request) {
        Assert.notNull(request.getEid(), "保存渠道商信息：企业ID为空！");
        Assert.notNull(request.getCustomerEid(), "保存渠道商信息：客户企业ID为空！");

        // 保存企业客户联系人
        enterpriseCustomerContactService.saveEnterpriseCustomerContactUserIds(request.getEid(), request.getCustomerEid(), request.getContactUserIds(), request.getOpUserId());

        // 保存支付方式设置
        customerPaymentMethodService.saveCustomerPaymentMethods(request.getEid(), request.getCustomerEid(), request.getPaymentMethodIds(), request.getPlatformEnum(), request.getOpUserId());

        if (request.getCustomerGroupId() != null) {

            EnterpriseCustomerDO enterpriseCustomerDO = Optional.ofNullable(this.get(request.getEid(), request.getCustomerEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_CHANNEL_CUSTOMER_NOT_EXIST));

            EnterpriseCustomerDO customerDO = new EnterpriseCustomerDO();
            customerDO.setId(enterpriseCustomerDO.getId());
            customerDO.setCustomerGroupId(request.getCustomerGroupId());
            customerDO.setOpUserId(request.getOpUserId());
            this.baseMapper.updateById(customerDO);
        }
        return true;
    }

    @Override
    public List<EnterpriseCustomerDO> listByEid(Long eid) {
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerDO::getEid, eid);

        List<EnterpriseCustomerDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public EnterpriseCustomerDTO listByEidAndCustomerErpCode(Long eid, String customerErpCode) {
        if (ObjectUtil.isNull(eid) || StrUtil.isBlank(customerErpCode)) {
            return null;
        }
        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseCustomerDO::getEid, eid).eq(EnterpriseCustomerDO::getCustomerErpCode, customerErpCode);
        List<EnterpriseCustomerDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }

        return PojoUtils.map(list.get(0), EnterpriseCustomerDTO.class);
    }

    @Override
    public Boolean syncUpdateById(UpdateCustomerRequest request) {
        if (ObjectUtil.isNull(request)) {
            return false;
        }
        EnterpriseCustomerDO enterpriseCustomerDO = new EnterpriseCustomerDO();
        enterpriseCustomerDO.setId(request.getId());
        if (StringUtils.isNotBlank(request.getCustomerName())) {
            enterpriseCustomerDO.setCustomerName(request.getCustomerName());
        }
        if (StringUtils.isNotBlank(request.getCustomerCode())) {
            enterpriseCustomerDO.setCustomerCode(request.getCustomerCode());
        }
        if (StringUtils.isNotBlank(request.getCustomerErpCode())) {
            enterpriseCustomerDO.setCustomerErpCode(request.getCustomerErpCode());
        }
        if (ObjectUtil.isNotNull(request.getCustomerGroupId())) {
            enterpriseCustomerDO.setCustomerGroupId(request.getCustomerGroupId());
        }
        if (ObjectUtil.isNotNull(request.getLastPurchaseTime())) {
            enterpriseCustomerDO.setLastPurchaseTime(request.getLastPurchaseTime());
        }
        return this.updateById(enterpriseCustomerDO);
    }

    @Override
    public Page<EnterpriseDTO> queryCustomerPageListByContact(QueryCustomerPageListByContactRequest request) {

        return PojoUtils.map(this.baseMapper.queryCustomerPageListByContact(request.getPage(),request),EnterpriseDTO.class);
    }

    @Override
    public List<EnterpriseDTO> getEidListByCustomerId(Long customerId) {
        LambdaQueryWrapper<EnterpriseCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseCustomerDO::getCustomerEid,customerId);

        List<EnterpriseCustomerDO> list = this.list(queryWrapper);
        List<Long> eidList = list.stream().map(EnterpriseCustomerDO::getEid).collect(Collectors.toList());

        List<EnterpriseDO> enterpriseDOList = enterpriseService.listByIds(eidList);
        return PojoUtils.map(enterpriseDOList,EnterpriseDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addB2bCustomer(AddCustomerRequest request) {
        String lockName = this.getLockName(request.getEid(), request.getCustomerEid());
        String lock = null;
        try {
            lock = redisDistributedLock.lock(lockName, 5, 10, TimeUnit.SECONDS);
            if (StrUtil.isEmpty(lock)) {
                log.error("添加B2B企业客户获取锁超时，客户详情：{}", JSONObject.toJSONString(request));
                throw new BusinessException(UserErrorCode.ADD_CUSTOMER_GET_LOCK_OUT_TIME);
            }

            EnterpriseCustomerDO entity = this.get(request.getEid(), request.getCustomerEid());
            if (entity == null) {
                entity = PojoUtils.map(request, EnterpriseCustomerDO.class);
                entity.setSource(EnterpriseCustomerSourceEnum.ONLINE_PURCHASE.getCode());
                this.save(entity);
                log.info("添加B2B企业客户成功，请求数据：{}", JSONObject.toJSONString(request));

                //添加企业客户产品线
                AddCustomerLineRequest lineRequest = new AddCustomerLineRequest();
                lineRequest.setCustomerId(entity.getId());
                lineRequest.setEid(request.getEid());
                lineRequest.setEname(enterpriseService.getById(request.getEid()).getName());
                lineRequest.setCustomerEid(request.getCustomerEid());
                lineRequest.setCustomerName(request.getCustomerName());
                lineRequest.setOpUserId(request.getOpUserId());
                lineRequest.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
                enterpriseCustomerLineService.add(ListUtil.toList(lineRequest));
            }
        } catch (InterruptedException e) {
            log.error("添加B2B客户获取锁失败：{}", e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lock);
        }

        return true;
    }

    @Override
    public List<Long> getEidListByCustomerEid(QueryCanBuyEidRequest request) {
        return this.baseMapper.getEidListByCustomerEid(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEnterpriseCustomer(Long customerId, Long opUserId) {
        // 获取企业客户信息
        EnterpriseCustomerDO enterpriseCustomerDO = this.getById(customerId);
        if (Objects.isNull(enterpriseCustomerDO)) {
            return false;
        }

        // 删除企业客户信息
        EnterpriseCustomerDO customerDO = new EnterpriseCustomerDO();
        customerDO.setId(customerId);
        customerDO.setOpUserId(opUserId);
        this.deleteByIdWithFill(customerDO);

        // 删除企业客户产品线信息
        enterpriseCustomerLineService.deleteByCustomerId(customerId, opUserId);

        // 删除企业客户EAS信息
        LambdaQueryWrapper<EnterpriseCustomerEasDO> easWrapper = new LambdaQueryWrapper<>();
        easWrapper.eq(EnterpriseCustomerEasDO::getEid, enterpriseCustomerDO.getEid());
        easWrapper.eq(EnterpriseCustomerEasDO::getCustomerEid, enterpriseCustomerDO.getCustomerEid());
        EnterpriseCustomerEasDO enterpriseCustomerEasDO = new EnterpriseCustomerEasDO();
        enterpriseCustomerEasDO.setOpUserId(opUserId);
        enterpriseCustomerEasService.batchDeleteWithFill(enterpriseCustomerEasDO, easWrapper);

        // 删除企业客户联系人
        LambdaQueryWrapper<EnterpriseCustomerContactDO> contactWrapper = new LambdaQueryWrapper<>();
        contactWrapper.eq(EnterpriseCustomerContactDO::getEid, enterpriseCustomerDO.getEid());
        contactWrapper.eq(EnterpriseCustomerContactDO::getCustomerEid, enterpriseCustomerDO.getCustomerEid());
        EnterpriseCustomerContactDO customerContactDO = new EnterpriseCustomerContactDO();
        customerContactDO.setOpUserId(opUserId);
        enterpriseCustomerContactService.batchDeleteWithFill(customerContactDO, contactWrapper);

        // 删除企业客户支付方式
        LambdaQueryWrapper<EnterpriseCustomerPaymentMethodDO> paymentMethodWrapper = new LambdaQueryWrapper<>();
        paymentMethodWrapper.eq(EnterpriseCustomerPaymentMethodDO::getEid, enterpriseCustomerDO.getEid());
        paymentMethodWrapper.eq(EnterpriseCustomerPaymentMethodDO::getCustomerEid, enterpriseCustomerDO.getCustomerEid());
        EnterpriseCustomerPaymentMethodDO paymentMethodDO = new EnterpriseCustomerPaymentMethodDO();
        paymentMethodDO.setOpUserId(opUserId);
        customerPaymentMethodService.batchDeleteWithFill(paymentMethodDO, paymentMethodWrapper);

        // 删除企业客户申请
        LambdaQueryWrapper<EnterprisePurchaseApplyDO> purchaseApplyWrapper = new LambdaQueryWrapper<>();
        purchaseApplyWrapper.eq(EnterprisePurchaseApplyDO::getEid, enterpriseCustomerDO.getEid());
        purchaseApplyWrapper.eq(EnterprisePurchaseApplyDO::getCustomerEid, enterpriseCustomerDO.getCustomerEid());
        EnterprisePurchaseApplyDO purchaseApplyDO = new EnterprisePurchaseApplyDO();
        purchaseApplyDO.setOpUserId(opUserId);
        enterprisePurchaseApplyService.batchDeleteWithFill(purchaseApplyDO, purchaseApplyWrapper);

        return true;
    }

    /**
     * 添加客户获取分布式锁名称
     *
     * @param eid
     * @param customerEid
     * @return
     */
    private String getLockName(Long eid, Long customerEid){
        return "enterprise_customer:add_customer"+":"+eid + "_" + customerEid;
    }

    /**
     * 是否客户已加入分组
     *
     * @param eid 企业ID
     * @param customerEids 客户ID
     * @return
     */
    private boolean hasCustomerJoinedGroup(Long eid, List<Long> customerEids) {
        Assert.notNull(eid, "参数eid不能为空");
        Assert.notEmpty(customerEids, "参数customerEids不能为空");

        QueryWrapper<EnterpriseCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerDO::getEid, eid)
                .in(EnterpriseCustomerDO::getCustomerEid, customerEids)
                .ne(EnterpriseCustomerDO::getCustomerGroupId, 0L);

        return this.count(queryWrapper) > 0;
    }
}
