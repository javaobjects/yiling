package com.yiling.sales.assistant.commissions.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.dao.CommissionsMapper;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.CommissionsPayRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.RemoveCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.UpdateCommissionsEffectiveRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDO;
import com.yiling.sales.assistant.commissions.entity.CommissionsDetailDO;
import com.yiling.sales.assistant.commissions.entity.CommissionsUserDO;
import com.yiling.sales.assistant.commissions.enums.CommissionsDetailStatusEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsErrorCode;
import com.yiling.sales.assistant.commissions.enums.CommissionsSourcesEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsStatusEnum;
import com.yiling.sales.assistant.commissions.enums.CommissionsTypeEnum;
import com.yiling.sales.assistant.commissions.enums.EffectStatusEnum;
import com.yiling.sales.assistant.commissions.service.CommissionsDetailService;
import com.yiling.sales.assistant.commissions.service.CommissionsService;
import com.yiling.sales.assistant.commissions.service.CommissionsUserService;
import com.yiling.sales.assistant.task.dto.UserTypeDTO;
import com.yiling.sales.assistant.task.enums.AssistantErrorCode;
import com.yiling.sales.assistant.task.service.UserTaskService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 销售助手佣金记录表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Service
@Slf4j
public class CommissionsServiceImpl extends BaseServiceImpl<CommissionsMapper, CommissionsDO> implements CommissionsService {

    @Autowired
    CommissionsDetailService commissionsDetailService;
    @Autowired
    CommissionsUserService commissionsUserService;
    @Autowired
    UserTaskService userTaskService;
    @Autowired
    protected RedisDistributedLock redisDistributedLock;
    @Autowired
    @Lazy
    CommissionsServiceImpl _this;

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;


    @Override
    public Boolean addCommissionsToUser(AddCommissionsToUserRequest request) {
        if (CollUtil.isEmpty(request.getDetailList())) {
            throw new BusinessException(CommissionsErrorCode.ADD_COMMISSIONS_LIST_NOT_NULL);
        }


        _this.sendMq(Constants.TOPIC_SA_COMMISSIONS_SEND, Constants.TAG_TOPIC_SA_COMMISSIONS_SEND, JSON.toJSONString(request));
        return Boolean.TRUE;
    }

    @Override
    public Boolean addCommissionsToUserConsumer(AddCommissionsToUserRequest request) {

        String lockName = StrUtil.format("addCommissions_{}_{}", request.getUserId(), request.getUserTaskId());
        String lockId = "";

        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);
            executeCommAdd(request);
        } catch (BusinessException e) {
            log.warn("佣金新增失败，原因：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("佣金新增失败，原因：{}", e.getMessage());
            throw e;
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean executeCommAdd(AddCommissionsToUserRequest request) {
        CommissionsDO commissionsDO;
        List<CommissionsDetailDO> detailList;

        //自所有任务都是统一生效后 逻辑改为无论任务收益还是下线分成每个任务在佣金记录中都只有一条
        if (ObjectUtil.equal(request.getSources(), CommissionsSourcesEnum.TASK.getCode()) || ObjectUtil.equal(request.getSources(), CommissionsSourcesEnum.SUBORDINATE.getCode())) {
            commissionsDO = getCommissionsDOByTask(request);
        } else {
            log.error("该佣金类型暂不支持，类型{}", request.getFinishType());
            return Boolean.FALSE;
        }
        //更新佣金记录的金额
        BigDecimal amount = BigDecimal.ZERO;
        for (AddCommissionsToUserRequest.AddCommToUserDetailRequest item : request.getDetailList()) {
            BigDecimal subAmount = item.getSubAmount();
            subAmount = subAmount == null ? BigDecimal.ZERO : subAmount;
            amount = amount.add(subAmount);
        }
        //如果佣金金额小于等于0
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            log.error(CommissionsErrorCode.SAVE_COMMISSIONS_CASH.getMessage());
            throw new BusinessException(CommissionsErrorCode.SAVE_COMMISSIONS_CASH);
        }
        commissionsDO.setAmount(amount.add(commissionsDO.getAmount()));
        commissionsDO.setSurplusAmount(amount.add(commissionsDO.getSurplusAmount()));
        commissionsDO.setStatus(CommissionsStatusEnum.UN_SETTLEMENT.getCode());
        commissionsDO.setOpUserId(request.getOpUserId());
        commissionsDO.setSources(request.getSources());
        //查询任务相关人员所属企业等信息
        UserTypeDTO userType = userTaskService.getUserTypeByUserTaskId(request.getUserTaskId());
        if (ObjectUtil.isNotNull(userType)) {
            commissionsDO.setTaskOwnershipEid(userType.getCurrentEid());
            commissionsDO.setTaskUserType(userType.getUserTypeEnum().getCode());
            //如果是下线分成则设置下线userId
            if (ObjectUtil.equal(commissionsDO.getSources(), CommissionsSourcesEnum.SUBORDINATE.getCode())) {
                commissionsDO.setSubordinateUserId(userType.getUserId());
            }
        }
        //查询获佣人相关的所属企业信息
        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByUserId(request.getUserId(), EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(enterpriseDTOS)) {
            commissionsDO.setOwnershipEid(0L);
            commissionsDO.setUserType(UserTypeEnum.ZIRANREN.getCode());
        } else {
            EnterpriseDTO enterpriseDTO = enterpriseDTOS.stream().findAny().get();
            commissionsDO.setOwnershipEid(enterpriseDTO.getId());
            commissionsDO.setUserType(Constants.YILING_EID.equals(enterpriseDTO.getId()) ? UserTypeEnum.YILING.getCode() : UserTypeEnum.XIAOSANYUAN.getCode());
        }

        //保存佣金记录
        boolean saveOrUpdate;
        if (ObjectUtil.equal(commissionsDO.getId(), 0L) || ObjectUtil.isNull(commissionsDO.getId())) {
            saveOrUpdate = save(commissionsDO);
        } else {
            saveOrUpdate = updateById(commissionsDO);
        }
        if (!saveOrUpdate) {
            log.error("保存佣金记录失败，参数：{}", commissionsDO);
            return Boolean.FALSE;
        }
        //如果佣金生效则更新用户佣金余额
        if (ObjectUtil.equal(EffectStatusEnum.VALID.getCode(), commissionsDO.getEffectStatus())) {
            Boolean isUpdate = commissionsUserService.addUserCommissions(commissionsDO);
            if (!isUpdate) {
                log.error("更新用户佣金余额失败,参数：{}", commissionsDO);
                throw new BusinessException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS);
            }
        }
        //保存佣金明细

        detailList = PojoUtils.map(request.getDetailList(), CommissionsDetailDO.class);
        detailList.forEach(e -> {
            e.setCommissionsId(commissionsDO.getId());
            e.setStatus(CommissionsDetailStatusEnum.UN_SETTLEMENT.getCode());
            e.setUserId(request.getUserId());
            e.setTaskId(request.getTaskId());
            e.setUserTaskId(request.getUserTaskId());
            e.setOpUserId(request.getOpUserId());
        });
        boolean saveBatch = commissionsDetailService.saveBatch(detailList);
        if (!saveBatch) {
            log.error("新增用户佣金明细失败,参数：{}", detailList);
            throw new BusinessException(CommissionsErrorCode.SAVE_COMMISSIONS_DETAIL);
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeCommissionsToUser(RemoveCommissionsToUserRequest request) {
        //根据userTaskId查询佣金记录
        LambdaQueryWrapper<CommissionsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDO::getUserTaskId, request.getUserTaskId());
        List<CommissionsDO> commissionsDOS = list(wrapper);
        if (CollUtil.isEmpty(commissionsDOS)) {
            log.error("删除佣金记录时，根据userTaskId未查询到佣金记录，userTaskId={}", request.getUserTaskId());
            throw new BusinessException(CommissionsErrorCode.COMMISSIONS_NOTFOUND);
        }
        Map<Integer, List<CommissionsDO>> sourceMap = commissionsDOS.stream().collect(Collectors.groupingBy(CommissionsDO::getSources));
        //删除本人佣金明细
        List<CommissionsDO> currentUserCom = sourceMap.get(CommissionsSourcesEnum.TASK.getCode());
        if (CollUtil.isEmpty(currentUserCom)) {
            log.error("删除佣金记录时，当前人没有佣金记录，userTaskId={}", request.getUserTaskId());
            throw new BusinessException(CommissionsErrorCode.COMMISSIONS_NOTFOUND);
        }
        CommissionsDO currentCom = currentUserCom.stream().findAny().orElse(null);
        if (ObjectUtil.equal(currentCom.getEffectStatus(), EffectStatusEnum.VALID.getCode())) {
            log.error("佣金已生效，佣金id={}，userTaskId={}", currentCom.getId(), request.getUserTaskId());
            throw new BusinessException(CommissionsErrorCode.COMMISSIONS_EFFECT_INVALID);
        }
        //查询本人佣金明细
        CommissionsDetailDTO commissionsDetailDTO = commissionsDetailService.queryCommDetail(currentCom.getId(), request.getOrderCode());
        BigDecimal amount = commissionsDetailDTO.getSubAmount();
        //删除佣金明细
        int row = commissionsDetailService.deleteByIdWithFill(PojoUtils.map(commissionsDetailDTO, CommissionsDetailDO.class));
        if (row == 0) {
            log.error("删除佣金明细失败，id={}", commissionsDetailDTO.getId());
            throw new ServiceException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS.getMessage());
        }
        currentCom.setAmount(currentCom.getAmount().subtract(amount));
        currentCom.setSurplusAmount(currentCom.getSurplusAmount().subtract(amount));

        if (currentCom.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            row = deleteByIdWithFill(currentCom);
            if (row == 0) {
                log.error("删除佣金失败，id={}", currentCom.getId());
                throw new ServiceException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS.getMessage());
            }
        } else {
            Boolean isSucceed = updateById(currentCom);
            if (!isSucceed) {
                log.error("更新佣金失败，id={}", currentCom.getId());
                throw new ServiceException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS.getMessage());
            }
        }

        //删除上线佣金明细
        List<CommissionsDO> leaderUserCom = sourceMap.get(CommissionsSourcesEnum.SUBORDINATE.getCode());
        //如果有上线分成
        if (CollUtil.isNotEmpty(leaderUserCom)) {
            CommissionsDO leaderComm = leaderUserCom.stream().findAny().orElse(null);
            //删除佣金明细
            CommissionsDetailDTO leaderComDetailDTO = commissionsDetailService.queryCommDetail(leaderComm.getId(), request.getOrderCode());
            row = commissionsDetailService.deleteByIdWithFill(PojoUtils.map(leaderComDetailDTO, CommissionsDetailDO.class));
            if (row == 0) {
                log.error("删除上线的佣金明细失败，id={}", leaderComDetailDTO.getId());
                throw new ServiceException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS.getMessage());
            }
            leaderComm.setAmount(leaderComm.getAmount().subtract(leaderComDetailDTO.getSubAmount()));

            //如果佣金为0，删除佣金记录
            if (leaderComm.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                //删除佣金记录
                row = deleteByIdWithFill(leaderComm);
                if (row == 0) {
                    log.error("删除上线的佣金记录失败，id={}", leaderComm.getId());
                    throw new ServiceException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS.getMessage());
                }
            } else {
                Boolean isSucceed = updateById(leaderComm);
                if (!isSucceed) {
                    log.error("更新上线的佣金记录失败，id={}", leaderComm.getId());
                    throw new ServiceException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS.getMessage());
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCommissionsEffective(UpdateCommissionsEffectiveRequest request) {
        LambdaQueryWrapper<CommissionsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDO::getUserId, request.getUserId());
        wrapper.eq(CommissionsDO::getUserTaskId, request.getUserTaskId());
        List<CommissionsDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            log.error("佣金记录不存在，userTaskId：{}", request.getUserTaskId());
            return Boolean.FALSE;
        }
        if (list.size() > 1) {
            log.error("交易类佣金记录存在多条，不能修改生效状态，userTaskId：{}", request.getUserTaskId());
            return Boolean.FALSE;
        }
        list.forEach(commissionsDO -> {
            commissionsDO.setOpUserId(request.getOpUserId());
            commissionsDO.setEffectStatus(EffectStatusEnum.VALID.getCode());
            commissionsDO.setEffectTime(new Date());
            //更新佣金记录为生效
            boolean saveOrUpdate = saveOrUpdate(commissionsDO);
            if (!saveOrUpdate) {
                log.error("更新佣金记录为生效时失败，参数：{}", commissionsDO);
                throw new BusinessException(CommissionsErrorCode.UPDATE_COMMISSIONS_EFFECT_FAIL);
            }
            saveOrUpdate = commissionsUserService.addUserCommissions(commissionsDO);
            if (!saveOrUpdate) {
                log.error("更新用户佣金余额失败,参数：{}", commissionsDO);
                throw new BusinessException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS);
            }
        });

        //更新上线佣金的佣金为生效
        updateInviterCommission(request);
        return Boolean.TRUE;
    }

    /**
     * 更新邀请人的佣金
     *
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInviterCommission(UpdateCommissionsEffectiveRequest request) {
        LambdaQueryWrapper<CommissionsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDO::getSources, CommissionsSourcesEnum.SUBORDINATE.getCode());
        wrapper.eq(CommissionsDO::getUserTaskId, request.getUserTaskId());
        List<CommissionsDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            log.info("没有邀请人佣金记录，userTaskId：{}", request.getUserTaskId());
            return;
        }
        list.forEach(commissionsDO -> {
            commissionsDO.setOpUserId(request.getOpUserId());
            commissionsDO.setEffectStatus(EffectStatusEnum.VALID.getCode());
            commissionsDO.setEffectTime(new Date());
            //更新佣金记录为生效
            boolean saveOrUpdate = saveOrUpdate(commissionsDO);
            if (!saveOrUpdate) {
                log.error("为上线更新佣金记录为生效时失败，参数：{}", commissionsDO);
                throw new BusinessException(CommissionsErrorCode.UPDATE_COMMISSIONS_EFFECT_FAIL);
            }
            saveOrUpdate = commissionsUserService.addUserCommissions(commissionsDO);
            if (!saveOrUpdate) {
                log.error("更新用户佣金余额失败,参数：{}", commissionsDO);
                throw new BusinessException(CommissionsErrorCode.UPDATE_USER_COMMISSIONS);
            }
        });

    }

    @Override
    public List<CommissionsDTO> queryCommissionsByUserTaskId(Long userId, Long userTaskId) {
        if (ObjectUtil.isNull(userId) || ObjectUtil.isNull(userTaskId)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<CommissionsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDO::getUserId, userId);
        wrapper.eq(CommissionsDO::getUserTaskId, userTaskId);
        List<CommissionsDO> list = list(wrapper);
        return PojoUtils.map(list, CommissionsDTO.class);
    }

    @Override
    public List<CommissionsDTO> queryCommissionsList(Long userId, CommissionsTypeEnum typeEnum, Integer day) {
        if (ObjectUtil.isNull(userId)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<CommissionsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDO::getUserId, userId);
        //如果传了收支类型
        wrapper.eq(ObjectUtil.isNotNull(typeEnum), CommissionsDO::getType, typeEnum.getCode());
        //如果传了天数
        if (ObjectUtil.isNotNull(day)) {
            //结束时间
            DateTime endTime = DateUtil.beginOfDay(new Date());
            //开始时间
            DateTime startTime = DateUtil.offsetDay(endTime, Math.negateExact(day));
            wrapper.ge(CommissionsDO::getEffectTime, startTime);
            wrapper.le(CommissionsDO::getEffectTime, endTime);
        }
        List<CommissionsDO> list = list(wrapper);
        return PojoUtils.map(list, CommissionsDTO.class);
    }

    @Override
    public Page<CommissionsDTO> queryCommissionsPageList(QueryCommissionsPageListRequest request) {
        if (CollUtil.isEmpty(request.getUserIdList())) {
            return new Page<>();
        }

        LambdaQueryWrapper<CommissionsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CommissionsDO::getUserId, request.getUserIdList());
        //如果传了收支类型及生效状态
        if (request.getTypeEnum() != null) {
            wrapper.eq(CommissionsDO::getType, request.getTypeEnum().getCode());
        }
        //如果传了结算状态
        if (request.getStatusEnum() != null) {
            wrapper.eq(CommissionsDO::getStatus, request.getStatusEnum().getCode());
        }
        //如果传了生效类型
        if (request.getEffectStatusEnum() != null) {
            wrapper.eq(CommissionsDO::getEffectStatus, request.getEffectStatusEnum().getCode());
            wrapper.orderByDesc(CommissionsDO::getEffectTime);
        } else {
            wrapper.orderByDesc(CommissionsDO::getCreateTime);
        }
        Page<CommissionsDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, CommissionsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean commissionsPay(List<CommissionsPayRequest> request) {
        if (CollUtil.isEmpty(request)) {
            return Boolean.TRUE;
        }
        //		//需要更新结清的佣金记录
        //		List<CommissionsDO> updateCommissionsList = ListUtil.toList();
        //每个用户兑付的金额
        Map<Long, AtomicReference<BigDecimal>> userPayAmount = MapUtil.newHashMap();

        List<Long> commissionsDetailIdList = request.stream().map(CommissionsPayRequest::getId).distinct().collect(Collectors.toList());
        //查询佣金明细
        List<CommissionsDetailDO> detailList = commissionsDetailService.listByIds(commissionsDetailIdList);
        //佣金记录map
        Map<Long, List<CommissionsDetailDO>> commissionsMap = detailList.stream().collect(Collectors.groupingBy(CommissionsDetailDO::getCommissionsId));
        //查询佣金记录
        List<CommissionsDO> commissionsList = listByIds(commissionsMap.keySet());
        Map<Long, CommissionsDO> commissionsDOMap = commissionsList.stream().collect(Collectors.toMap(CommissionsDO::getId, e -> e));

        //准备兑付相应数据
        commissionsMap.forEach((commissionsId, detailDOList) -> {
            //本条佣金记录的兑付金额
            BigDecimal commissionsPayAmount = BigDecimal.ZERO;
            for (CommissionsDetailDO detail : detailDOList) {
                //累加该条佣金兑付金额
                commissionsPayAmount = commissionsPayAmount.add(detail.getSubAmount());
            }
            CommissionsDO commissionsDO = commissionsDOMap.get(commissionsId);
            //如果待结算金额等于本次兑付金额则更新佣金记录状态为结清
            commissionsDO.setPaidAmount(commissionsDO.getPaidAmount().add(commissionsPayAmount));
            if (commissionsDO.getSurplusAmount().setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(commissionsPayAmount) == 0) {
                commissionsDO.setStatus(CommissionsStatusEnum.SETTLEMENT.getCode());
                commissionsDO.setSurplusAmount(BigDecimal.ZERO);
            } else {
                commissionsDO.setSurplusAmount(commissionsDO.getSurplusAmount().subtract(commissionsPayAmount));
            }
            //累加用户对应的兑付金额
            AtomicReference<BigDecimal> amount = userPayAmount.get(commissionsDO.getUserId());
            if (ObjectUtil.isNull(amount)) {
                AtomicReference<BigDecimal> reference = new AtomicReference<>();
                reference.set(commissionsPayAmount);
                userPayAmount.put(commissionsDO.getUserId(), reference);
            } else {
                amount.set(amount.get().add(commissionsPayAmount));
            }
        });
        //更新兑付数据
        savePayCommissions(commissionsList, detailList, userPayAmount, request.get(0).getOpUserId());
        return Boolean.TRUE;
    }

    /**
     * 保存佣金兑付记录
     *
     * @param commissionsList 需要更新为结清的佣金记录
     * @param detailList 兑付的佣金明细
     * @param userPayAmount 本次涉及用户的兑付金额
     * @param opUserId 操作人id
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePayCommissions(List<CommissionsDO> commissionsList, List<CommissionsDetailDO> detailList, Map<Long, AtomicReference<BigDecimal>> userPayAmount, Long opUserId) {
        //保存兑付佣金记录
        List<CommissionsDO> payCommList = ListUtil.toList();
        //每个用户的佣金兑付记录Map key=userId,value=佣金兑付记录id
        Map<Long, Long> tempPayRecordId = MapUtil.newHashMap();

        //准备佣金兑付记录
        userPayAmount.forEach((userId, amount) -> {
            CommissionsDO payCommissions = new CommissionsDO();
            payCommissions.setTaskName("发放奖励");
            payCommissions.setEffectStatus(EffectStatusEnum.VALID.getCode());
            payCommissions.setEffectTime(new Date());
            payCommissions.setUserId(userId);
            payCommissions.setAmount(amount.get());
            payCommissions.setType(CommissionsTypeEnum.OUTPUT.getCode());
            payCommissions.setOpUserId(opUserId);
            payCommList.add(payCommissions);
        });

        boolean isSuccess;

        //批量保存每个用户兑付的记录
        isSuccess = saveBatch(payCommList);

        if (!isSuccess) {
            log.error("新增佣金兑付记录异常，数据：{}", JSON.toJSON(payCommList));
            throw new BusinessException(AssistantErrorCode.COMMISSION_SAVE_ERROR);
        }
        //找出每个用户的佣金兑付记录的id
        payCommList.forEach(commissionsDO -> {
            tempPayRecordId.put(commissionsDO.getUserId(), commissionsDO.getId());
        });
        //设置佣金明细的兑付佣金记录id和兑付状态
        detailList.forEach(detailDO -> {
            detailDO.setPaidCommissionsId(tempPayRecordId.get(detailDO.getUserId()));
            detailDO.setStatus(CommissionsDetailStatusEnum.SETTLEMENT.getCode());
            detailDO.setOpUserId(opUserId);
        });

        //更新佣金记录状态
        if (CollUtil.isNotEmpty(commissionsList)) {
            isSuccess = updateBatchById(commissionsList);

            if (!isSuccess) {
                log.error("更新佣金记录状态失败，数据：{}", JSON.toJSON(commissionsList));
                throw new BusinessException(AssistantErrorCode.COMMISSIONS_UPDATE_FAILD);
            }
        }
        //批量更新佣金明细兑付状态
        isSuccess = commissionsDetailService.updateBatchById(detailList);
        if (!isSuccess) {
            log.error("更新佣金明细兑付状态失败，数据：{}", JSON.toJSON(detailList));
            throw new BusinessException(AssistantErrorCode.COMMISSIONS_DETAIL_UPDATE_FAILD);
        }
        //查询设计的用户佣金余额
        List<CommissionsUserDTO> commUserDTOList = commissionsUserService.batchQueryCommissionsUserByUserId(new ArrayList<>(userPayAmount.keySet()));
        List<CommissionsUserDO> commUserList = PojoUtils.map(commUserDTOList, CommissionsUserDO.class);
        commUserList.forEach(e -> {
            BigDecimal amount = userPayAmount.get(e.getUserId()).get();
            e.setSurplusAmount(e.getSurplusAmount().subtract(amount));
            e.setPaidAmount(e.getPaidAmount().add(amount));
            e.setOpUserId(opUserId);
        });
        //批量更新用户佣金表
        isSuccess = commissionsUserService.updateBatchById(commUserList);
        if (!isSuccess) {
            log.error("更新佣金表失败，数据：{}", JSON.toJSON(commUserList));
            throw new BusinessException(AssistantErrorCode.COMMISSIONS_USER_UPDATE_FAILD);
        }
    }


    /**
     * 根据交易类任务生成佣金记录
     *
     * @param request
     * @return
     */
    private CommissionsDO getCommissionsDOByTask(AddCommissionsToUserRequest request) {
        CommissionsDO commissionsDO;
        //如果是交易类（如果是交易类，每个任务在佣金记录表只能有一条记录）
        List<CommissionsDTO> commissionsDTOList = queryCommissionsByUserTaskId(request.getUserId(), request.getUserTaskId());
        //判断佣金是否存在
        if (CollUtil.isEmpty(commissionsDTOList)) {
            commissionsDO = PojoUtils.map(request, CommissionsDO.class);
            commissionsDO.setType(CommissionsTypeEnum.INPUT.getCode());
            commissionsDO.setAmount(BigDecimal.ZERO);
            commissionsDO.setSurplusAmount(BigDecimal.ZERO);
        } else {
            if (commissionsDTOList.size() > 1) {
                log.error("同一个人的同一个任务查询的佣金记录有多条，userId={},userTaskId={}", request.getUserId(), request.getUserTaskId());
                throw new BusinessException(CommissionsErrorCode.COMMISSIONS_TO_MANY);
            }
            CommissionsDTO commissions = commissionsDTOList.stream().findAny().get();
            //如果佣金状态为已生效
            if (ObjectUtil.equal(EffectStatusEnum.VALID.getCode(), commissions.getEffectStatus())) {
                log.error("该交易类任务对应的佣金记录已经是有效状态，不能在向其中添加佣金明细，参数{}", commissions);
                throw new BusinessException(CommissionsErrorCode.COMMISSIONS_EFFECT_INVALID);
            }

            commissionsDO = PojoUtils.map(commissions, CommissionsDO.class);
        }
        //如果任务完成则佣金生效
        if (ObjectUtil.equal(EffectStatusEnum.VALID.getCode(), request.getEffectStatus())) {
            commissionsDO.setEffectTime(new Date());
            commissionsDO.setEffectStatus(EffectStatusEnum.VALID.getCode());
        }
        return commissionsDO;
    }
    //      自所有任务都是统一生效后此方法作废
    //	/**
    //	 * 根据下线收益生成佣金记录
    //	 *
    //	 * @param request
    //	 * @return
    //	 */
    //	private CommissionsDO getCommissionsDOBySubordinate(AddCommissionsToUserRequest request) {
    //		CommissionsDO commissionsDO;
    //
    //		//如果是下线收益（如果是下线收益，每个任务在佣金记录表可以有多条记录）
    //		//下线收益的佣金必须立即生效 后续规则改了下线也不是立即生效而是任务完成后统一发放
    ///*		if (ObjectUtil.equal(request.getEffectStatus(), EffectStatusEnum.INVALID.getCode())) {
    //			throw new BusinessException(CommissionsErrorCode.EFFECT_STATUS_INVALID);
    //		}*/
    //		commissionsDO = PojoUtils.map(request, CommissionsDO.class);
    //		commissionsDO.setType(CommissionsTypeEnum.INPUT.getCode());
    //		//commissionsDO.setEffectTime(new Date());
    //		commissionsDO.setAmount(BigDecimal.ZERO);
    //		commissionsDO.setSurplusAmount(BigDecimal.ZERO);
    //		return commissionsDO;
    //	}


    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);

        mqMessageSendApi.send(mqMessageBO);

        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {

        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

}
