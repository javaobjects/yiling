package com.yiling.user.enterprise.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.UserCustomerStatusEnum;
import com.yiling.user.enterprise.dao.EnterpriseAuthInfoMapper;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseAuthPageRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.entity.EnterpriseAuthInfoDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseAuthTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseAuthInfoService;
import com.yiling.user.enterprise.service.EnterpriseCertificateAuthInfoService;
import com.yiling.user.enterprise.service.EnterpriseCertificateService;
import com.yiling.user.enterprise.service.EnterprisePlatformService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.service.LockCustomerService;
import com.yiling.user.member.service.impl.MemberBuyRecordServiceImpl;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.UpdateCustomerStatusRequest;
import com.yiling.user.usercustomer.service.UserCustomerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业副本 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Service
@Slf4j
public class EnterpriseAuthInfoServiceImpl extends BaseServiceImpl<EnterpriseAuthInfoMapper, EnterpriseAuthInfoDO> implements EnterpriseAuthInfoService {

    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    EnterpriseCertificateAuthInfoService certificateAuthInfoService;
    @Autowired
    EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    UserCustomerService userCustomerService;
    @Autowired
    EnterprisePlatformService enterprisePlatformService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    EnterpriseAuthInfoServiceImpl _this;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addEnterpriseAuth(EnterpriseAuthInfoRequest request) {
        //校验是否存在待审核的企业副本信息，存在不可以创建副本
        LambdaQueryWrapper<EnterpriseAuthInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseAuthInfoDO::getEid,request.getEid());
        queryWrapper.eq(EnterpriseAuthInfoDO::getAuthStatus,EnterpriseAuthStatusEnum.UNAUTH.getCode());
        EnterpriseAuthInfoDO infoDO = this.getOne(queryWrapper);
        if(Objects.nonNull(infoDO)){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_GOING);
        }

        EnterpriseAuthInfoDO enterpriseAuthInfoDO = PojoUtils.map(request,EnterpriseAuthInfoDO.class);
        enterpriseAuthInfoDO.setAuthStatus(EnterpriseAuthStatusEnum.UNAUTH.getCode());
        EnterpriseDO enterprise = Optional.ofNullable(enterpriseService.getById(request.getEid())).orElse(new EnterpriseDO());
        enterpriseAuthInfoDO.setType(enterprise.getType());

        //设置审核类型：1-首次认证 2-资质更新 3-驳回后再次认证
        EnterpriseDO enterpriseDO = enterpriseService.getById(request.getEid());

        if(EnterpriseAuthStatusEnum.UNAUTH == EnterpriseAuthStatusEnum.getByCode(enterpriseDO.getAuthStatus())){
            enterpriseAuthInfoDO.setAuthType(EnterpriseAuthTypeEnum.FIRST.getCode());
        }else {
            queryWrapper.clear();
            queryWrapper.eq(EnterpriseAuthInfoDO::getEid,request.getEid());
            queryWrapper.orderByDesc(EnterpriseAuthInfoDO::getCreateTime);
            List<EnterpriseAuthInfoDO> authInfoDOList = this.list(queryWrapper);

            if(CollUtil.isEmpty(authInfoDOList)){
                enterpriseAuthInfoDO.setAuthType(EnterpriseAuthTypeEnum.UPDATE.getCode());
            }else if (EnterpriseAuthStatusEnum.getByCode(authInfoDOList.get(0).getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS) {
                enterpriseAuthInfoDO.setAuthType(EnterpriseAuthTypeEnum.UPDATE.getCode());
            } else if (EnterpriseAuthStatusEnum.getByCode(authInfoDOList.get(0).getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_FAIL) {
                enterpriseAuthInfoDO.setAuthType(EnterpriseAuthTypeEnum.REJECT_AND_UPDATE.getCode());
            }
        }

        this.save(enterpriseAuthInfoDO);

        return enterpriseAuthInfoDO.getId();
    }

    @Override
    public EnterpriseAuthInfoDTO getByEid(Long eid) {
        LambdaQueryWrapper<EnterpriseAuthInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseAuthInfoDO::getEid,eid);
        queryWrapper.orderByDesc(EnterpriseAuthInfoDO::getCreateTime);

        List<EnterpriseAuthInfoDO> list = this.list(queryWrapper);

        return PojoUtils.map(list.stream().findFirst().orElse(null),EnterpriseAuthInfoDTO.class);
    }

    @Override
    public Page<EnterpriseAuthInfoDTO> pageList(QueryEnterpriseAuthPageRequest request) {
        LambdaQueryWrapper<EnterpriseAuthInfoDO> queryWrapper = getEnterpriseAuthInfoLambdaQueryWrapper(request);

        return PojoUtils.map(this.page(request.getPage(),queryWrapper),EnterpriseAuthInfoDTO.class);
    }

    @Override
    public Boolean updateAuthStatus(UpdateEnterpriseAuthRequest request) {
        EnterpriseAuthInfoDTO authInfoDTO = this.getByEid(request.getId());

        if(EnterpriseAuthStatusEnum.UNAUTH != EnterpriseAuthStatusEnum.getByCode(authInfoDTO.getAuthStatus())){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_STATUS_ERROR);
        }

        EnterpriseAuthInfoDO authInfoDO = new EnterpriseAuthInfoDO();
        authInfoDO.setId(authInfoDTO.getId());
        authInfoDO.setOpUserId(request.getOpUserId());
        authInfoDO.setAuthUser(request.getOpUserId());
        authInfoDO.setAuthTime(new Date());

        if(EnterpriseAuthStatusEnum.AUTH_SUCCESS == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus())){
            authInfoDO.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        }else{
            authInfoDO.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_FAIL.getCode());
            authInfoDO.setAuthRejectReason(request.getAuthRejectReason());
        }

        return this.updateById(authInfoDO);
    }

    @Override
    public Boolean updateAuth(UpdateEnterpriseAuthRequest request) {
        List<MqMessageBO> mqMessageBOList = _this.updateAuthTx(request);

        if (CollUtil.isNotEmpty(mqMessageBOList)) {
            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }
        return true;
    }

    @GlobalTransactional
    public List<MqMessageBO> updateAuthTx(UpdateEnterpriseAuthRequest request) {
        //先判断是首次认证还是资质更新审核
        EnterpriseAuthInfoDTO authInfoDTO = Optional.ofNullable(this.getByEid(request.getId()))
                .orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_AUTH_INFO_NOT_FIND));
        EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(request.getId())).orElse(new EnterpriseDO());

        List<MqMessageBO> mqMessageBOList = ListUtil.toList();
        boolean salesSendMqFlag = false;
        if (Objects.nonNull(enterpriseDO.getSource()) && EnterpriseSourceEnum.getByCode(enterpriseDO.getSource()) == EnterpriseSourceEnum.SALE_ASSISTANT_ADD) {
            // 销售助手添加的未入住的客户审核通过后发送MQ通知
            if (EnterpriseAuthStatusEnum.AUTH_SUCCESS == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus()))  {
                List<EnterpriseAuthInfoDTO> authInfoDTOList = this.queryListByEid(request.getId());
                long count = authInfoDTOList.stream().filter(infoDTO -> infoDTO.getAuthStatus().equals(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode())).count();
                // 首次认证校验
                if (count == 0 && EnterpriseAuthTypeEnum.getByCode(authInfoDTO.getAuthType()) == EnterpriseAuthTypeEnum.FIRST) {
                    salesSendMqFlag = true;
                }
            }
        }

        log.info("企业审核接口入参：{}，查询到企业副本信息：{}", JSONObject.toJSONString(request),JSONObject.toJSONString(authInfoDTO));
        this.updateAuthStatus(request);

        //如果是审核销售助手添加进来的企业，还需要更新销售助手对应的我的客户表审核状态（即只要更新审核状态，对应客户状态就要变更）
        this.updateCustomerStatus(request, enterpriseDO.getSource());

        if (EnterpriseAuthTypeEnum.getByCode(authInfoDTO.getAuthType()) == EnterpriseAuthTypeEnum.FIRST) {
            //如果首次认证：审核成功或失败都需要修改企业表审核状态；
            enterpriseService.updateAuthStatus(request);

        } else {
            //如果不是首次认证：如果是驳回，只需要更新副本；如果是审核成功，还需要更新企业信息
            if (EnterpriseAuthStatusEnum.AUTH_SUCCESS == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus())) {

                //场景：如果首次认证时审核不通过，驳回后修改再次提交，此时审核通过，也需要更新企业状态为审核通过
                if(EnterpriseAuthStatusEnum.AUTH_FAIL == EnterpriseAuthStatusEnum.getByCode(enterpriseDO.getAuthStatus())){
                    enterpriseService.updateAuthStatus(request);
                }
            }

        }
        //只要是审核成功，就需要修改企业信息
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus())) {
            UpdateEnterpriseRequest enterpriseRequest = PojoUtils.map(authInfoDTO, UpdateEnterpriseRequest.class);
            enterpriseRequest.setOpUserId(request.getOpUserId());
            enterpriseRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            enterpriseRequest.setId(authInfoDTO.getEid());
            enterpriseService.update(enterpriseRequest);

            //修改企业资质信息
            List<EnterpriseCertificateAuthInfoDTO> certificateAuthInfoDTOList = certificateAuthInfoService.getCertificateAuthInfoListByAuthId(authInfoDTO.getId());
            if (CollUtil.isNotEmpty(certificateAuthInfoDTOList)) {
                enterpriseCertificateService.deleteByEid(authInfoDTO.getEid());

                List<CreateEnterpriseCertificateRequest> certificateList = PojoUtils.map(certificateAuthInfoDTOList,CreateEnterpriseCertificateRequest.class);
                enterpriseCertificateService.saveCertificateList(certificateList);
            }
        }

        // 销售助手添加的未入住的客户审核通过后发送MQ通知
        if (salesSendMqFlag) {
            QueryUserCustomerRequest customerRequest = new QueryUserCustomerRequest();
            customerRequest.setCustomerEid(request.getId());
            List<UserCustomerDTO> userCustomerDTOList = userCustomerService.queryCustomerList(customerRequest);
            log.info("销售助手添加的企业客户首次认证且审核通过准备发送MQ，当前用户客户数量：{}",userCustomerDTOList.size());
            if (CollUtil.isNotEmpty(userCustomerDTOList)) {
                UserCustomerDTO userCustomerDTO = userCustomerDTOList.get(0);
                if (userCustomerDTO.getLockStatus() != 2) {
                    MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_SALES_ADD_CUSTOMER_PASS_NOTIFY, "", JSONObject.toJSONString(userCustomerDTO));
                    mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                    mqMessageBOList.add(mqMessageBO);
                    log.info("销售助手添加的企业客户首次认证且审核通过发送MQ通知，企业ID：{}",request.getId());
                }
            }
        }

        // 终端类型的企业审核通过后自动开通B2B
        if (EnterpriseTypeEnum.getByCode(enterpriseDO.getType()).isTerminal()) {
            enterprisePlatformService.openPlatform(enterpriseDO.getId(), ListUtil.toList(PlatformEnum.B2B), null, null, request.getOpUserId());
        }

        return mqMessageBOList;
    }

    @Override
    public List<EnterpriseAuthInfoDTO> queryListByEid(Long eid) {
        LambdaQueryWrapper<EnterpriseAuthInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseAuthInfoDO::getEid,eid);
        queryWrapper.orderByDesc(EnterpriseAuthInfoDO::getCreateTime);

        return PojoUtils.map(this.list(queryWrapper),EnterpriseAuthInfoDTO.class);
    }

    /**
     * 如果是审核销售助手添加进来的企业，还需要根据场景是否更新销售助手对应的我的客户表审核状态
     * @param request 审核信息
     * @param source 企业来源
     */
    private void updateCustomerStatus(UpdateEnterpriseAuthRequest request, Integer source){
        if (Objects.nonNull(source) && EnterpriseSourceEnum.getByCode(source) == EnterpriseSourceEnum.SALE_ASSISTANT_ADD) {
            UpdateCustomerStatusRequest statusRequest = new UpdateCustomerStatusRequest();
            statusRequest.setCustomerEid(request.getId());
            statusRequest.setStatus(request.getAuthStatus());
            statusRequest.setOpUserId(request.getOpUserId());
            statusRequest.setRejectReason(UserCustomerStatusEnum.REJECT == UserCustomerStatusEnum.getByCode(request.getAuthStatus()) ? request.getAuthRejectReason() : "");
            statusRequest.setAuditTime(new Date());
            userCustomerService.updateCustomerStatus(statusRequest);
        }
    }

    private LambdaQueryWrapper<EnterpriseAuthInfoDO> getEnterpriseAuthInfoLambdaQueryWrapper(QueryEnterpriseAuthPageRequest request) {
        LambdaQueryWrapper<EnterpriseAuthInfoDO> queryWrapper = new LambdaQueryWrapper<>();

        if(Objects.nonNull(request.getAuthStatus()) && request.getAuthStatus() != 0){
            queryWrapper.eq(EnterpriseAuthInfoDO::getAuthStatus, request.getAuthStatus());
        }

        if(Objects.nonNull(request.getAuthType()) && request.getAuthType() != 0){
            queryWrapper.eq(EnterpriseAuthInfoDO::getAuthType, request.getAuthType());
        }

        if(Objects.nonNull(request.getSource()) && request.getSource() != 0){
            queryWrapper.eq(EnterpriseAuthInfoDO::getSource, request.getSource());
        }

        if(Objects.nonNull(request.getType()) && request.getType() != 0){
            queryWrapper.eq(EnterpriseAuthInfoDO::getType, request.getType());
        }

        if(StrUtil.isNotEmpty(request.getName())){
            queryWrapper.like(EnterpriseAuthInfoDO::getName, request.getName());
        }

        if(StrUtil.isNotEmpty(request.getLicenseNumber())){
            queryWrapper.like(EnterpriseAuthInfoDO::getLicenseNumber, request.getLicenseNumber());
        }

        if(StrUtil.isNotEmpty(request.getProvinceCode())){
            queryWrapper.eq(EnterpriseAuthInfoDO::getProvinceCode, request.getProvinceCode());
        }

        if(StrUtil.isNotEmpty(request.getCityCode())){
            queryWrapper.eq(EnterpriseAuthInfoDO::getCityCode, request.getCityCode());
        }

        if(StrUtil.isNotEmpty(request.getRegionCode())){
            queryWrapper.eq(EnterpriseAuthInfoDO::getRegionCode, request.getRegionCode());
        }

        if(Objects.nonNull(request.getStartCreateTime())){
            queryWrapper.ge(EnterpriseAuthInfoDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }

        if(Objects.nonNull(request.getEndCreateTime())){
            queryWrapper.le(EnterpriseAuthInfoDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        return queryWrapper;
    }

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
