package com.yiling.user.member.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.member.bo.MemberBuyRecordBO;
import com.yiling.user.member.dao.MemberBuyRecordMapper;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.AddMemberReturnRequest;
import com.yiling.user.member.dto.request.CancelBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyRecordRequest;
import com.yiling.user.member.dto.request.RefundOrderRequest;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.member.dto.request.UpdatePromoterSendRequest;
import com.yiling.user.member.dto.request.UpdateReturnEnterpriseMemberRequest;
import com.yiling.user.member.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.EnterpriseMemberLogDO;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.entity.MemberOrderDO;
import com.yiling.user.member.enums.EnterpriseMemberLogOpTypeEnum;
import com.yiling.user.member.enums.MemberOpenTypeEnum;
import com.yiling.user.member.enums.MemberReturnAuthStatusEnum;
import com.yiling.user.member.enums.MemberReturnStatusEnum;
import com.yiling.user.member.enums.MemberSourceEnum;
import com.yiling.user.member.service.EnterpriseMemberLogService;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberOrderService;
import com.yiling.user.member.service.MemberReturnService;
import com.yiling.user.member.service.MemberService;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.AdminService;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员购买记录 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Slf4j
@Service
public class MemberBuyRecordServiceImpl extends BaseServiceImpl<MemberBuyRecordMapper, MemberBuyRecordDO> implements MemberBuyRecordService {

    @Autowired
    MemberService memberService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;
    @Autowired
    StaffService staffService;
    @Autowired
    MemberReturnService memberReturnService;
    @Autowired
    MemberOrderService memberOrderService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    EnterpriseMemberLogService enterpriseMemberLogService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    MemberBuyRecordServiceImpl _this;

    @Override
    public Page<MemberBuyRecordDTO> queryBuyRecordListPage(QueryBuyRecordRequest request) {
        // 获取查询条件和分页查询
        QueryWrapper<MemberBuyRecordDO> queryWrapper = WrapperUtils.getWrapper(request);

        // 是否退款、是否过期处理虚拟字段处理
        if (Objects.nonNull(request.getReturnFlag()) && request.getReturnFlag() != 0 ) {
            // 未退款
            if (request.getReturnFlag() == 1) {
                queryWrapper.lambda().eq(MemberBuyRecordDO::getReturnAmount, 0);
            } else if (request.getReturnFlag() == 2) {
                // 已退款
                queryWrapper.lambda().ne(MemberBuyRecordDO::getReturnAmount, 0);
            }
        }
        // 通过会员结束时间如果小于当前查询时间展示已过期，为大于当前时间未过期
        if (Objects.nonNull(request.getExpireFlag()) && request.getExpireFlag() != 0 ) {
            // 未过期
            if (request.getExpireFlag() == 1) {
                queryWrapper.lambda().gt(MemberBuyRecordDO::getEndTime, new Date());
            } else if (request.getExpireFlag() == 2) {
                // 已过期
                queryWrapper.lambda().lt(MemberBuyRecordDO::getEndTime, new Date());
            }
        }
        // 更新人
        if (StrUtil.isNotEmpty(request.getUpdateUserName())) {
            Admin admin = adminService.getByName(request.getUpdateUserName());
            if (Objects.nonNull(admin)) {
                queryWrapper.lambda().eq(MemberBuyRecordDO::getUpdateUser, admin.getId());
            }
        }
        // 推广方所属省市区编码
        if (StrUtil.isNotEmpty(request.getPromoterProvinceCode()) || StrUtil.isNotEmpty(request.getPromoterCityCode()) || StrUtil.isNotEmpty(request.getPromoterRegionCode())) {
            List<Long> allPromoterIdList = this.getAllPromoterId();

            QueryEnterpriseListRequest listRequest = new QueryEnterpriseListRequest();
            if (CollUtil.isNotEmpty(allPromoterIdList)) {
                listRequest.setIdList(allPromoterIdList);
            }
            listRequest.setProvinceCode(request.getPromoterProvinceCode());
            listRequest.setCityCode(request.getPromoterCityCode());
            listRequest.setRegionCode(request.getPromoterRegionCode());
            List<Long> eidList = enterpriseService.queryListByArea(listRequest).stream().map(BaseDTO::getId).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(eidList)) {
                queryWrapper.lambda().in(MemberBuyRecordDO::getPromoterId, eidList);
            } else {
                return new Page<>(request.getPage().getCurrent(), request.getPage().getSize());
            }
        }

        queryWrapper.lambda().orderByDesc(MemberBuyRecordDO::getCreateTime, MemberBuyRecordDO::getId);


        Page<MemberBuyRecordDO> recordDoPage = this.page(request.getPage(), queryWrapper);

        // 对返回数据进行处理
        if(CollUtil.isNotEmpty(recordDoPage.getRecords())){
            List<Long> memberIdList = recordDoPage.getRecords().stream().map(MemberBuyRecordDO::getMemberId).distinct().collect(Collectors.toList());
            Map<Long, String> memberNameMap = memberService.listByIds(memberIdList).stream().collect(Collectors.toMap(BaseDO::getId, MemberDO::getName));

            List<Long> eidList = recordDoPage.getRecords().stream().map(MemberBuyRecordDO::getEid).distinct().collect(Collectors.toList());
            Map<Long, String> nameMap = enterpriseService.listByIds(eidList).stream().collect(Collectors.toMap(BaseDO::getId, EnterpriseDO::getName));

            recordDoPage.getRecords().forEach(memberBuyRecordDO -> {
                memberBuyRecordDO.setMemberName(memberNameMap.get(memberBuyRecordDO.getMemberId()));
                memberBuyRecordDO.setEname(nameMap.get(memberBuyRecordDO.getEid()));
            });
        }

        return PojoUtils.map(recordDoPage,MemberBuyRecordDTO.class);
    }

    @Override
    public Boolean updateBuyMemberPromoter(UpdateMemberPromoterRequest request) {
        List<MqMessageBO> mqMessageBOList = _this.updateMemberPromoter(request);

        if (CollUtil.isNotEmpty(mqMessageBOList)) {
            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<MqMessageBO> updateMemberPromoter(UpdateMemberPromoterRequest request) {
        if (Objects.isNull(request.getPromoterId()) && StrUtil.isEmpty(request.getMobile())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        List<MqMessageBO> mqMessageBOList = ListUtil.toList();

        MemberBuyRecordDO memberBuyRecordDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_BUY_RECORD_NOT_EXIST));
        if (StrUtil.isNotEmpty(request.getMobile())) {
            if (Objects.isNull(memberBuyRecordDO.getPromoterUserId()) || memberBuyRecordDO.getPromoterUserId() == 0) {
                // 更新推广人
                Staff staff = Optional.ofNullable(staffService.getByMobile(request.getMobile())).orElseThrow(() -> new BusinessException(UserErrorCode.SA_LOGIN_ACCOUNT_NOT_EXISTS));
                MemberBuyRecordDO buyRecord = new MemberBuyRecordDO();
                buyRecord.setId(memberBuyRecordDO.getId());
                buyRecord.setPromoterUserId(staff.getId());
                buyRecord.setPromoterUserName(staff.getName());
                buyRecord.setOpUserId(request.getOpUserId());
                // 根据需求：导入的会员不允许修改数据来源字段
                if (MemberSourceEnum.getByCode(memberBuyRecordDO.getSource()) != MemberSourceEnum.IMPORT_GIVE && MemberSourceEnum.getByCode(memberBuyRecordDO.getSource()) != MemberSourceEnum.IMPORT_P2P) {
                    // 重新设置数据来源：B2b-自然流量（无推广方ID和推广人ID）B2b-企业推广（仅有推广方ID），销售助手（有推广人ID有推广方或没有推广方ID有推广人ID）
                    buyRecord.setSource(MemberSourceEnum.ASSISTANT.getCode());
                }
                this.updateById(buyRecord);

                // 发送MQ通知任务佣金模块（存在退款审核通过则不推送）
                List<MemberReturnDTO> memberReturnDtoList = memberReturnService.getReturnListByOrderNo(memberBuyRecordDO.getOrderNo());
                List<MemberReturnDTO> returnDTOList = memberReturnDtoList.stream().filter(memberReturnDTO -> memberReturnDTO.getAuthStatus().equals(MemberReturnAuthStatusEnum.PASS.getCode())).collect(Collectors.toList());
                if (CollUtil.isEmpty(returnDTOList)) {
                    MemberOrderDTO memberOrderDTO = memberOrderService.getMemberOrderByOrderNo(memberBuyRecordDO.getOrderNo());
                    EnterpriseDO enterpriseDO = enterpriseService.getById(memberOrderDTO.getEid());

                    UpdateUserTaskMemberRequest memberRequest = getUpdateUserTaskMemberRequest(request, staff, memberOrderDTO, enterpriseDO);
                    MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_PROMOTER_NOTIFY, "", JSONObject.toJSONString(memberRequest));
                    mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                    mqMessageBOList.add(mqMessageBO);
                    log.info("修改推广人推送MQ通知佣金模块 推广人ID={}，对象信息={}", staff.getId(), memberRequest);
                }
                // 如果此时有推广方，还要再通知返利侧
                if (Objects.nonNull(memberBuyRecordDO.getPromoterId()) && memberBuyRecordDO.getPromoterId() != 0) {
                    UpdatePromoterSendRequest promoterSendRequest = new UpdatePromoterSendRequest(memberBuyRecordDO.getOrderNo(), memberBuyRecordDO.getPromoterId(), MemberSourceEnum.ASSISTANT.getCode());
                    MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_UPDATE_B2B_MEMBER_PROMOTER, "", JSONObject.toJSONString(promoterSendRequest));
                    mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                    mqMessageBOList.add(mqMessageBO);
                    log.info("更新会员推广人发送MQ通知返利 发送数据信息={}", JSONObject.toJSONString(promoterSendRequest));
                }
            }

        } else {
            // 更新推广方
            MemberBuyRecordDO buyRecord = new MemberBuyRecordDO();
            buyRecord.setId(memberBuyRecordDO.getId());
            buyRecord.setOpUserId(request.getOpUserId());
            if (request.getPromoterId() == 0) {
                buyRecord.setPromoterId(request.getPromoterId());
                buyRecord.setPromoterName("");
            } else {
                EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(request.getPromoterId())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
                buyRecord.setPromoterId(enterpriseDO.getId());
                buyRecord.setPromoterName(enterpriseDO.getName());
            }

            // 根据需求：导入的会员不允许修改数据来源字段
            if (MemberSourceEnum.getByCode(memberBuyRecordDO.getSource()) != MemberSourceEnum.IMPORT_GIVE && MemberSourceEnum.getByCode(memberBuyRecordDO.getSource()) != MemberSourceEnum.IMPORT_P2P) {
                // 重新设置数据来源：B2b-自然流量（无推广方ID和推广人ID）B2b-企业推广（仅有推广方ID），销售助手（有推广人ID有推广方或没有推广方ID有推广人ID）
                if (memberBuyRecordDO.getPromoterUserId() == 0) {
                    buyRecord.setSource(MemberSourceEnum.B2B_ENTERPRISE.getCode());
                } else {
                    buyRecord.setSource(MemberSourceEnum.ASSISTANT.getCode());
                }
            }
            this.updateById(buyRecord);

            // 插入企业会员日志记录
            this.insertEnterpriseMemberLog(memberBuyRecordDO, request.getPromoterId(), request.getOpUserId());

            // 发送MQ通知返利模块
            if (memberBuyRecordDO.getReturnAmount().compareTo(BigDecimal.ZERO) <= 0) {
                UpdatePromoterSendRequest promoterSendRequest = new UpdatePromoterSendRequest(memberBuyRecordDO.getOrderNo(), buyRecord.getPromoterId(),
                        Objects.nonNull(buyRecord.getSource()) ? buyRecord.getSource() : memberBuyRecordDO.getSource());
                MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_UPDATE_B2B_MEMBER_PROMOTER, "", JSONObject.toJSONString(promoterSendRequest));
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                mqMessageBOList.add(mqMessageBO);

                log.info("更新会员推广方发送MQ通知返利 发送数据信息：{}", JSONObject.toJSONString(promoterSendRequest));
            }

        }

        return mqMessageBOList;
    }

    /**
     * 插入企业会员日志记录
     *
     * @param buyRecordDO
     * @param promoterId
     */
    private void insertEnterpriseMemberLog(MemberBuyRecordDO buyRecordDO, Long promoterId, Long opUserId) {
        EnterpriseMemberLogDO enterpriseMemberLogDO = new EnterpriseMemberLogDO();
        enterpriseMemberLogDO.setEid(buyRecordDO.getEid());
        enterpriseMemberLogDO.setEname(buyRecordDO.getEname());
        enterpriseMemberLogDO.setMemberId(buyRecordDO.getMemberId());
        enterpriseMemberLogDO.setMemberName(buyRecordDO.getMemberName());
        // 操作类型：1-开通 2-续费 3-退费 4-修改推广方
        enterpriseMemberLogDO.setOpType(EnterpriseMemberLogOpTypeEnum.UPDATE_PROMOTER.getCode());
        enterpriseMemberLogDO.setOrderNo(buyRecordDO.getOrderNo());
        enterpriseMemberLogDO.setOrderBuyRule(buyRecordDO.getBuyRule());
        enterpriseMemberLogDO.setPromoterId(promoterId);
        enterpriseMemberLogDO.setValidDays(buyRecordDO.getValidDays());
        EnterpriseMemberDO enterpriseMemberDO = enterpriseMemberService.getEnterpriseMember(buyRecordDO.getEid(), buyRecordDO.getMemberId());
        enterpriseMemberLogDO.setBeforeStartTime(enterpriseMemberDO.getStartTime());
        enterpriseMemberLogDO.setBeforeEndTime(enterpriseMemberDO.getEndTime());
        enterpriseMemberLogDO.setAfterStartTime(enterpriseMemberDO.getStartTime());
        enterpriseMemberLogDO.setAfterEndTime(enterpriseMemberDO.getEndTime());
        enterpriseMemberLogDO.setOpUserId(opUserId);
        enterpriseMemberLogService.save(enterpriseMemberLogDO);
    }

    private UpdateUserTaskMemberRequest getUpdateUserTaskMemberRequest(UpdateMemberPromoterRequest request, Staff staff, MemberOrderDTO memberOrderDTO, EnterpriseDO enterpriseDO) {
        UpdateUserTaskMemberRequest memberRequest = new UpdateUserTaskMemberRequest();
        memberRequest.setOrderNo(memberOrderDTO.getOrderNo());
        memberRequest.setPushType(2);
        memberRequest.setMemberId(memberOrderDTO.getMemberId());
        memberRequest.setMemberStageId(memberOrderDTO.getBuyStageId());
        memberRequest.setCreateUser(request.getOpUserId());
        memberRequest.setEid(enterpriseDO.getId());
        memberRequest.setEname(enterpriseDO.getName());
        memberRequest.setPromoterId(memberOrderDTO.getPromoterId());
        memberRequest.setPromoterUserId(staff.getId());
        memberRequest.setContactorPhone(enterpriseDO.getContactorPhone());
        memberRequest.setContactorUserId(Optional.ofNullable(staffService.getByMobile(enterpriseDO.getContactorPhone())).orElse(new Staff()).getId());
        memberRequest.setTradeTime(memberOrderDTO.getCreateTime());
        return memberRequest;
    }

    @Override
    public MemberBuyRecordDTO getBuyRecordDetail(Long id) {
        MemberBuyRecordDO buyRecordDO = this.getById(id);
        buyRecordDO.setMemberName(memberService.getById(buyRecordDO.getMemberId()).getName());
        buyRecordDO.setEname(enterpriseService.getById(buyRecordDO.getEid()).getName());

        return PojoUtils.map(buyRecordDO, MemberBuyRecordDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitReturn(UpdateMemberReturnRequest request) {
        MemberBuyRecordDO buyRecordDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_BUY_RECORD_NOT_EXIST));
        // 校验退款金额不能大于支付金额 且退款只能退款一次(退成功算一次)
        List<MemberReturnDTO> memberReturnDTOS = memberReturnService.getReturnListByOrderNo(buyRecordDO.getOrderNo());
        if (CollUtil.isNotEmpty(memberReturnDTOS)) {
            memberReturnDTOS.forEach(memberReturnDTO -> {
                if (MemberReturnStatusEnum.getByCode(memberReturnDTO.getReturnStatus()) == MemberReturnStatusEnum.SUCCESS) {
                    throw new BusinessException(UserErrorCode.MEMBER_HAD_RETURN_SUCCESS);
                }

                if (MemberReturnStatusEnum.getByCode(memberReturnDTO.getReturnStatus()) == MemberReturnStatusEnum.WAITING
                        || MemberReturnStatusEnum.getByCode(memberReturnDTO.getReturnStatus()) == MemberReturnStatusEnum.RETURNING) {
                    throw new BusinessException(UserErrorCode.MEMBER_RETURNING);
                }

            });
        }
        if (request.getSubmitReturnAmount().compareTo(buyRecordDO.getPayAmount()) > 0) {
            throw new BusinessException(UserErrorCode.MEMBER_RETURN_AMOUNT_MORE_THAN_PAY);
        }

        // 更新会员购买记录-会员退款
        MemberBuyRecordDO memberBuyRecordDO = PojoUtils.map(request, MemberBuyRecordDO.class);
        memberBuyRecordDO.setOpUserId(request.getOpUserId());
        this.updateById(memberBuyRecordDO);

        MemberOrderDTO memberOrderDTO = memberOrderService.getMemberOrderByOrderNo(buyRecordDO.getOrderNo());
        // 新增退款审核数据
        AddMemberReturnRequest returnRequest = PojoUtils.map(buyRecordDO, AddMemberReturnRequest.class);
        returnRequest.setReturnAmount(request.getSubmitReturnAmount());
        returnRequest.setReturnReason(request.getReturnReason());
        returnRequest.setReturnRemark(request.getReturnRemark());
        returnRequest.setOrderId(memberOrderDTO.getId());
        returnRequest.setPayTime(memberOrderDTO.getUpdateTime());
        returnRequest.setApplyUser(request.getOpUserId());
        returnRequest.setApplyTime(new Date());
        returnRequest.setOpUserId(request.getOpUserId());
        memberReturnService.addMemberReturn(returnRequest);

        return true;
    }

    @Override
    public MemberBuyRecordDTO getBuyRecordByOrderNo(String orderNo) {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getOrderNo, orderNo);
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        wrapper.last("limit 1");

        return PojoUtils.map(this.getOne(wrapper), MemberBuyRecordDTO.class);
    }

    @Override
    public Integer selectCountRecords(Long eid, Long memberId) {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, eid);
        wrapper.eq(MemberBuyRecordDO::getMemberId, memberId);
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        return this.count(wrapper);
    }

    @Override
    public MemberBuyRecordDTO getLastBuyRecord(Long eid, Long memberId) {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, eid);
        wrapper.eq(MemberBuyRecordDO::getMemberId, memberId);
        wrapper.gt(MemberBuyRecordDO::getEndTime, new Date());
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        wrapper.orderByDesc(MemberBuyRecordDO::getCreateTime);
        List<MemberBuyRecordDO> list = this.list(wrapper);

        for (MemberBuyRecordDO memberBuyRecordDO : list) {
            if (memberBuyRecordDO.getSubmitReturnAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return PojoUtils.map(memberBuyRecordDO, MemberBuyRecordDTO.class);
            }
            // 以审核通过作为退款成功的节点
            List<MemberReturnDTO> returnDTOList = memberReturnService.getReturnListByOrderNo(memberBuyRecordDO.getOrderNo());
            List<Integer> authList = returnDTOList.stream().map(MemberReturnDTO::getAuthStatus).collect(Collectors.toList());
            if (CollUtil.isEmpty(authList) || !authList.contains(MemberReturnAuthStatusEnum.PASS.getCode())) {
                return PojoUtils.map(memberBuyRecordDO, MemberBuyRecordDTO.class);
            }
        }

        return null;
    }

    @Override
    public List<Long> getEidByOrderNoList(List<String> orderNoList) {
        if (CollUtil.isEmpty(orderNoList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MemberBuyRecordDO::getOrderNo, orderNoList);
        wrapper.gt(MemberBuyRecordDO::getEndTime, new Date());
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        return this.list(wrapper).stream().map(MemberBuyRecordDO::getEid).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Long>> getEidByPromoterId(List<Long> promoterIdList) {
        if (CollUtil.isEmpty(promoterIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MemberBuyRecordDO::getPromoterId, promoterIdList);
        wrapper.eq(MemberBuyRecordDO::getReturnAmount, BigDecimal.ZERO);
        wrapper.lt(MemberBuyRecordDO::getStartTime, new Date());
        wrapper.gt(MemberBuyRecordDO::getEndTime, new Date());
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        List<MemberBuyRecordDO> recordDOList = this.list(wrapper);

        Map<Long, List<MemberBuyRecordDO>> map = recordDOList.stream().collect(Collectors.groupingBy(MemberBuyRecordDO::getPromoterId));
        Map<Long, List<Long>> promoterEidMap = MapUtil.newHashMap();
        map.forEach((promoterId, buyRecordDOList) -> promoterEidMap.put(promoterId, buyRecordDOList.stream().map(MemberBuyRecordDO::getEid).distinct().collect(Collectors.toList())));

        return promoterEidMap;
    }

    @Override
    public List<MemberBuyRecordDTO> getCurrentValidMemberRecord(Long eid) {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, eid);
        wrapper.le(MemberBuyRecordDO::getStartTime, new Date());
        wrapper.ge(MemberBuyRecordDO::getEndTime, new Date());
        wrapper.eq(MemberBuyRecordDO::getReturnAmount, BigDecimal.ZERO);
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);

        return PojoUtils.map(this.list(wrapper), MemberBuyRecordDTO.class);
    }

    @Override
    public List<MemberBuyRecordBO> getMemberBuyRecordByDate(QueryMemberBuyRecordRequest request) {
        QueryWrapper<MemberBuyRecordDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().eq(MemberBuyRecordDO::getCancelFlag, 0);
        List<MemberBuyRecordBO> buyRecordBOList = PojoUtils.map(this.list(wrapper), MemberBuyRecordBO.class);

        List<String> orderNoList = buyRecordBOList.stream().map(MemberBuyRecordBO::getOrderNo).collect(Collectors.toList());
        Map<String, Long> stageByOrderMap = memberOrderService.getStageByOrderList(orderNoList);

        buyRecordBOList.forEach(memberBuyRecordBO -> memberBuyRecordBO.setBuyStageId(stageByOrderMap.get(memberBuyRecordBO.getOrderNo())));
        return buyRecordBOList;
    }

    @Override
    public Map<Long, Long> getPromoterByEid(Long eid) {
        // 获取到企业当前生效的会员购买记录
        Date now = new Date();
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, eid);
        wrapper.lt(MemberBuyRecordDO::getStartTime, now);
        wrapper.gt(MemberBuyRecordDO::getEndTime, now);
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        List<MemberBuyRecordDO> buyRecordDOList = this.list(wrapper);

        return buyRecordDOList.stream().collect(Collectors.toMap(MemberBuyRecordDO::getMemberId, MemberBuyRecordDO::getPromoterId, (k1, k2) -> k2));
    }

    @Override
    public List<MemberBuyRecordDTO> getMemberRecordListByEid(Long eid, Long memberId) {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, eid);
        wrapper.eq(MemberBuyRecordDO::getMemberId, memberId);
        wrapper.eq(MemberBuyRecordDO::getReturnAmount, BigDecimal.ZERO);
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        wrapper.orderByAsc(MemberBuyRecordDO::getStartTime);
        return PojoUtils.map(this.list(wrapper), MemberBuyRecordDTO.class);
    }

    @Override
    public List<MemberBuyRecordDTO> getBuyRecordListByCond(QueryMemberListRecordRequest request) {
        if (Objects.isNull(request)) {
            return ListUtil.toList();
        }

        QueryWrapper<MemberBuyRecordDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().eq(MemberBuyRecordDO::getReturnAmount, 0);
        wrapper.lambda().eq(MemberBuyRecordDO::getCancelFlag, 0);
        // 是否只查有效的购买记录
        Date now = new Date();
        if (Objects.nonNull(request.getValidFlag()) && request.getValidFlag()) {
            wrapper.lambda().gt(MemberBuyRecordDO::getEndTime, now);
        }
        // 是否只查当前正在生效的购买记录
        if (Objects.nonNull(request.getCurrentValid()) && request.getCurrentValid()) {
            wrapper.lambda().lt(MemberBuyRecordDO::getStartTime, now);
            wrapper.lambda().gt(MemberBuyRecordDO::getEndTime, now);
        }

        return PojoUtils.map(this.list(wrapper), MemberBuyRecordDTO.class);
    }

    @Override
    public List<Long> getAllPromoterId() {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        wrapper.groupBy(MemberBuyRecordDO::getPromoterId);
        wrapper.select(MemberBuyRecordDO::getPromoterId);
        return this.list(wrapper).stream().map(MemberBuyRecordDO::getPromoterId).collect(Collectors.toList());
    }

    @Override
    public List<MemberBuyRecordDTO> getValidMemberRecordList(Long eid, Long memberId) {
        LambdaQueryWrapper<MemberBuyRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberBuyRecordDO::getEid, eid);
        wrapper.eq(MemberBuyRecordDO::getMemberId, memberId);
        wrapper.gt(MemberBuyRecordDO::getEndTime, new Date());
        wrapper.eq(MemberBuyRecordDO::getReturnAmount, BigDecimal.ZERO);
        wrapper.eq(MemberBuyRecordDO::getCancelFlag, 0);
        wrapper.orderByAsc(MemberBuyRecordDO::getStartTime);
        return PojoUtils.map(this.list(wrapper), MemberBuyRecordDTO.class);
    }

    @Override
    public boolean cancelBuyRecord(CancelBuyRecordRequest request) {
        MemberBuyRecordDO buyRecordDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_BUY_RECORD_NOT_EXIST));
        // 只可取消导入类型的购买记录
        if (MemberSourceEnum.IMPORT_GIVE != MemberSourceEnum.getByCode(buyRecordDO.getSource())
                && MemberSourceEnum.IMPORT_P2P != MemberSourceEnum.getByCode(buyRecordDO.getSource())) {
            throw new BusinessException(UserErrorCode.MEMBER_CANCEL_ERROR);
        }
        Date now = new Date();
        // 如果取消的是失效的记录不做任何操作
        if (buyRecordDO.getEndTime().before(now)) {
            return true;
        }

        List<MqMessageBO> mqMessageBOList = _this.cancelBuyRecordTx(request, buyRecordDO);

        if (CollUtil.isNotEmpty(mqMessageBOList)) {
            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }

        return true;
    }

    @GlobalTransactional
    public List<MqMessageBO> cancelBuyRecordTx(CancelBuyRecordRequest request, MemberBuyRecordDO buyRecordDO) {
        // 根据企业ID和会员ID，获取有效的会员购买记录（即排除过期、退款的等）
        List<MemberBuyRecordDTO> validMemberRecordList = this.getValidMemberRecordList(buyRecordDO.getEid(), buyRecordDO.getMemberId());
        // 获取企业会员信息
        EnterpriseMemberDO enterpriseMember = enterpriseMemberService.getEnterpriseMember(buyRecordDO.getEid(), buyRecordDO.getMemberId());

        // 1.更新企业会员
        UpdateReturnEnterpriseMemberRequest enterpriseMemberRequest = PojoUtils.map(buyRecordDO, UpdateReturnEnterpriseMemberRequest.class);
        enterpriseMemberRequest.setOpUserId(request.getOpUserId());
        enterpriseMemberService.updateReturnEnterpriseMember(enterpriseMemberRequest);

        // 购买记录、会员订单均设置为已取消状态；增加企业会员取消记录的日志
        MemberBuyRecordDO recordTemp = new MemberBuyRecordDO();
        recordTemp.setId(request.getId());
        recordTemp.setCancelFlag(1);
        recordTemp.setOpUserId(request.getOpUserId());
        this.updateById(recordTemp);

        MemberOrderDTO orderDTO = memberOrderService.getMemberOrderByOrderNo(buyRecordDO.getOrderNo());
        MemberOrderDO memberOrderDO = new MemberOrderDO();
        memberOrderDO.setId(orderDTO.getId());
        memberOrderDO.setCancelFlag(1);
        memberOrderDO.setOpUserId(request.getOpUserId());
        memberOrderService.updateById(memberOrderDO);

        EnterpriseMemberLogDO memberLogDO = PojoUtils.map(buyRecordDO, EnterpriseMemberLogDO.class);
        memberLogDO.setOpType(EnterpriseMemberLogOpTypeEnum.CANCEL_IMPORT_RECORD.getCode());
        memberLogDO.setOrderBuyRule(buyRecordDO.getBuyRule());
        memberLogDO.setBeforeStartTime(enterpriseMember.getStartTime());
        memberLogDO.setBeforeEndTime(enterpriseMember.getEndTime());
        memberLogDO.setAfterStartTime(enterpriseMember.getStartTime());
        memberLogDO.setAfterEndTime(DateUtil.offsetDay(enterpriseMember.getEndTime(), -buyRecordDO.getValidDays()));
        memberLogDO.setOpUserId(request.getOpUserId());
        enterpriseMemberLogService.save(memberLogDO);

        // MQ通知：此时有推广人就推到任务佣金模块、有推广方就推到返利模块
        List<MqMessageBO> mqMessageBOList = ListUtil.toList();
        if (Objects.nonNull(buyRecordDO.getPromoterId()) && buyRecordDO.getPromoterId() != 0) {
            RefundOrderRequest refundOrderRequest = new RefundOrderRequest();
            refundOrderRequest.setOrderNo(buyRecordDO.getOrderNo());
            refundOrderRequest.setRefundType(4);
            refundOrderRequest.setRefundAmount(BigDecimal.ZERO);
            refundOrderRequest.setRefundSource(2);
            refundOrderRequest.setSellerEid(Constants.YILING_EID);
            refundOrderRequest.setBuyerEid(buyRecordDO.getEid());
            refundOrderRequest.setPaymentAmount(BigDecimal.ZERO);
            refundOrderRequest.setTotalAmount(BigDecimal.ZERO);
            refundOrderRequest.setOpUserId(request.getOpUserId());

            // 发送MQ通知返利模块
            MqMessageBO rebateMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_REFUND_PUSH_REBATE, "", JSONObject.toJSONString(refundOrderRequest));
            rebateMessageBO = mqMessageSendApi.prepare(rebateMessageBO);
            mqMessageBOList.add(rebateMessageBO);
            log.info("会员取消记录发送MQ通知返利模块 发送数据信息：{}", JSONObject.toJSONString(refundOrderRequest));
        }
        if (Objects.nonNull(buyRecordDO.getPromoterUserId()) && buyRecordDO.getPromoterUserId() != 0) {
            // 发送MQ通知佣金模块
            MqMessageBO taskMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_REFUND_PUSH_TASK, "", buyRecordDO.getOrderNo());
            taskMessageBO = mqMessageSendApi.prepare(taskMessageBO);
            mqMessageBOList.add(taskMessageBO);
            log.info("会员取消记录发送MQ通知佣金模块 发送数据信息：{}", buyRecordDO.getOrderNo());
        }

        // 重新计算当前企业会员的 会员购买记录的开始时间、结束时间、开通类型
        this.updateBuyRecordTime(request.getId(), validMemberRecordList, enterpriseMember);

        return mqMessageBOList;
    }

    @Override
    public void updateBuyRecordTime(Long id, List<MemberBuyRecordDTO> validMemberRecordList, EnterpriseMemberDO enterpriseMember) {
        if (validMemberRecordList.size() > 1) {
            List<Long> recordIdList = validMemberRecordList.stream().map(BaseDTO::getId).collect(Collectors.toList());
            // 不是最后一条数据才处理，最后一条数据则无需处理
            int index = recordIdList.indexOf(id);
            if (validMemberRecordList.size() -1 != index) {
                List<MemberBuyRecordDO> recordDOList = ListUtil.toList();
                // 如果为第一条记录那么直接全部重新更新时间（此时还需要注意开通类型字段）
                if (index == 0) {
                    Date startTime = enterpriseMember.getStartTime();
                    for (int i = 1; i< validMemberRecordList.size(); i++) {
                        MemberBuyRecordDTO buyRecordDTO = validMemberRecordList.get(i);

                        MemberBuyRecordDO recordDO = new MemberBuyRecordDO();
                        if (i == 1) {
                            recordDO.setOpenType(MemberOpenTypeEnum.FIRST.getCode());
                        } else {
                            recordDO.setOpenType(MemberOpenTypeEnum.RENEWAL.getCode());
                        }
                        recordDO.setId(buyRecordDTO.getId());
                        recordDO.setStartTime(startTime);
                        recordDO.setEndTime(DateUtil.offsetDay(startTime, buyRecordDTO.getValidDays()));
                        // 第一次开始，结束时间作为下一次的开始时间
                        startTime = recordDO.getEndTime();
                        recordDOList.add(recordDO);
                    }

                } else {
                    // 如果为中间的记录，那么需要更新当前被删除记录后面的所有记录时间字段
                    int s = index+1;
                    Date startTime = validMemberRecordList.get(index-1).getEndTime();
                    for (int i = s; i < validMemberRecordList.size(); i++) {
                        MemberBuyRecordDTO buyRecordDTO = validMemberRecordList.get(i);

                        MemberBuyRecordDO recordDO = new MemberBuyRecordDO();
                        recordDO.setId(buyRecordDTO.getId());
                        recordDO.setStartTime(startTime);
                        recordDO.setEndTime(DateUtil.offsetDay(startTime, buyRecordDTO.getValidDays()));
                        // 结束时间作为下一次的开始时间
                        startTime = recordDO.getEndTime();
                        recordDOList.add(recordDO);
                    }

                }

                if (recordDOList.size() > 0) {
                    log.info("重新计算当前企业的会员购买记录时间段 待更新的数据={}", JSONObject.toJSONString(recordDOList));
                    this.updateBatchById(recordDOList);
                }

            }
        }
    }

}
