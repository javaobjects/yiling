package com.yiling.user.usercustomer.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.UserCustomerStatusEnum;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDetailDO;
import com.yiling.user.enterprise.enums.EnterpriseAuthSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseAuthInfoService;
import com.yiling.user.enterprise.service.EnterpriseCertificateAuthInfoService;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaDetailService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.service.LockCustomerService;
import com.yiling.user.system.entity.UserSalesAreaDO;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.system.service.UserSalesAreaService;
import com.yiling.user.usercustomer.dao.UserCustomerMapper;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.AddUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.UpdateCustomerStatusRequest;
import com.yiling.user.usercustomer.dto.request.UpdateUserCustomerRequest;
import com.yiling.user.usercustomer.entity.UserCustomerDO;
import com.yiling.user.usercustomer.service.UserCustomerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 用户客户Service实现
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
@Service
public class UserCustomerServiceImpl extends BaseServiceImpl<UserCustomerMapper, UserCustomerDO> implements UserCustomerService {

    @Autowired
    UserCustomerMapper userCustomerMapper;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    EnterpriseCertificateAuthInfoService certificateAuthInfoService;
    @Autowired
    EnterpriseSalesAreaDetailService enterpriseSalesAreaDetailService;
    @Autowired
    UserSalesAreaService userSalesAreaService;
    @Autowired
    EnterpriseAuthInfoService enterpriseAuthInfoService;
    @Autowired
    LockCustomerService lockCustomerService;

    @Override
    @GlobalTransactional
    public boolean add(AddUserCustomerRequest request) {
        //添加企业
        EnterpriseDO enterpriseDTO = Optional.ofNullable(enterpriseService.getByLicenseNumber(request.getLicenseNumber()))
                .orElse(new EnterpriseDO());

        // 校验不同机构类型，企业资质必填项
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS != EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus())){
            checkEnterpriseCertificate(request.getType() , request.getCertificateList());
        }
        // 校验：审核中，审核失败状态其他人无法添加
        if (EnterpriseAuthStatusEnum.UNAUTH == EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus()) ||
                EnterpriseAuthStatusEnum.AUTH_FAIL == EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus())) {
            throw new BusinessException(UserErrorCode.ENTERPRISE_GOING_AUTH);
        }
        // 校验：销售区域是否在可售范围。已入驻的企业已经在上一步校验，故此处只校验未入驻的企业即可
        if (Objects.isNull(enterpriseDTO.getId())) {
            boolean checkUserSaleArea = this.checkUserSaleArea(request.getUserId(), request.getEid(), request.getUserType(), request.getRegionCode());
            if (!checkUserSaleArea) {
                throw new BusinessException(UserErrorCode.CUSTOMER_NOT_IN_SALES_AREA);
            }
        }

        LockCustomerDTO lockCustomerDTO = Optional.ofNullable(lockCustomerService.getByLicenseNumber(request.getLicenseNumber())).orElse(new LockCustomerDTO());

        Long eid = enterpriseDTO.getId();
        if (Objects.isNull(eid)){
            CreateEnterpriseRequest enterpriseRequest = PojoUtils.map(request,CreateEnterpriseRequest.class);
            enterpriseRequest.setSource(EnterpriseSourceEnum.SALE_ASSISTANT_ADD.getCode());
            eid = enterpriseService.create(enterpriseRequest);
            // 添加企业审核信息
            this.addEnterpriseAuth(request, eid);
        }
        request.setCustomerEid(eid);

        UserCustomerDO userCustomerDO = PojoUtils.map(request,UserCustomerDO.class);
        userCustomerDO.setStatus(Objects.isNull(enterpriseDTO.getId()) ? UserCustomerStatusEnum.WAITING.getCode() : UserCustomerStatusEnum.PASS.getCode());
        userCustomerDO.setLockStatus(lockCustomerDTO.getStatus());

        return this.save(userCustomerDO);
    }

    private void addEnterpriseAuth(AddUserCustomerRequest request, Long eid) {
        //添加企业信息副本
        EnterpriseAuthInfoRequest authInfoRequest = new EnterpriseAuthInfoRequest();
        authInfoRequest.setEid(eid);
        authInfoRequest.setName(request.getName());
        authInfoRequest.setProvinceCode(request.getProvinceCode());
        authInfoRequest.setProvinceName(request.getProvinceName());
        authInfoRequest.setCityCode(request.getCityCode());
        authInfoRequest.setCityName(request.getCityName());
        authInfoRequest.setRegionCode(request.getRegionCode());
        authInfoRequest.setRegionName(request.getRegionName());
        authInfoRequest.setAddress(request.getAddress());
        authInfoRequest.setContactor(request.getContactor());
        authInfoRequest.setContactorPhone(request.getContactorPhone());
        authInfoRequest.setLicenseNumber(request.getLicenseNumber());
        authInfoRequest.setSource(EnterpriseAuthSourceEnum.SA.getCode());
        authInfoRequest.setOpUserId(request.getOpUserId());
        Long enterpriseAuthId = enterpriseAuthInfoService.addEnterpriseAuth(authInfoRequest);

        //添加企业资质信息副本
        List<EnterpriseCertificateAuthInfoRequest> authInfoRequests = PojoUtils.map(request.getCertificateList(),EnterpriseCertificateAuthInfoRequest.class);
        authInfoRequests.forEach(enterpriseCertificateAuthInfoRequest ->{
            enterpriseCertificateAuthInfoRequest.setEnterpriseAuthId(enterpriseAuthId);
            enterpriseCertificateAuthInfoRequest.setEid(eid);
            enterpriseCertificateAuthInfoRequest.setOpUserId(request.getOpUserId());
        });
        certificateAuthInfoService.addEnterpriseCertificateAuth(authInfoRequests);
    }

    @Override
    public UserCustomerDTO getCustomerById(Long id) {
        UserCustomerDO userCustomerDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.CUSTOMER_NOT_EXIST));
        UserCustomerDTO userCustomerDTO = PojoUtils.map(userCustomerDO,UserCustomerDTO.class);

        EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(userCustomerDO.getCustomerEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        PojoUtils.map(enterpriseDO,userCustomerDTO);
        userCustomerDTO.setStatus(userCustomerDO.getStatus());
        userCustomerDTO.setAuditTime(userCustomerDO.getAuditTime());

        return userCustomerDTO;
    }

    /**
     * 校验企业资质信息
     * @param enterpriseType 企业类型
     * @param certificateList 企业资质list
     */
    private void checkEnterpriseCertificate(Integer enterpriseType , List<CreateEnterpriseCertificateRequest> certificateList) {
        if(CollUtil.isNotEmpty(certificateList)){

            Map<Integer, String> map = certificateList.stream().collect(
                    Collectors.toMap(CreateEnterpriseCertificateRequest::getType, CreateEnterpriseCertificateRequest::getFileKey, (k1, k2) -> k2));
            Map<Integer, CreateEnterpriseCertificateRequest> typeMap = certificateList.stream().collect(
                    Collectors.toMap(CreateEnterpriseCertificateRequest::getType, createEnterpriseCertificateRequest -> createEnterpriseCertificateRequest));

            EnterpriseTypeEnum typeCertificateEnum = Optional.ofNullable(EnterpriseTypeEnum.getByCode(enterpriseType))
                    .orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_FIND_CERTIFICATE_TYPE));

            //对必填的和需要证照日期的进行校验
            List<EnterpriseCertificateTypeEnum> must = typeCertificateEnum.getCertificateTypeEnumList();
            for (EnterpriseCertificateTypeEnum enums : must) {
                //必填
                if(enums.getMustExist()){
                    //证照日期必须存在
                    if(enums.getCollectDate()){
                        CreateEnterpriseCertificateRequest request = typeMap.get(enums.getCode());
                        if(Objects.isNull(request) || Objects.isNull(request.getPeriodBegin())){
                            throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "证照日期必填");
                        }

                        if((Objects.isNull(request.getPeriodEnd()) && request.getLongEffective() == 0 )){
                            throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "证照日期必填");
                        }
                    }

                    if (Objects.isNull(map.get(enums.getCode()))) {
                        throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "必填");
                    }
                }
            }
        }

    }

    @Override
    public Page<UserCustomerDTO> pageList(QueryUserCustomerRequest request) {
        return userCustomerMapper.queryPageList(request.getPage(),request);
    }

    @Override
    @GlobalTransactional
    public boolean update(UpdateUserCustomerRequest request) {
        //只有审核失败的客户才可以修改
        LambdaQueryWrapper<UserCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCustomerDO::getUserId,request.getUserId());
        wrapper.eq(UserCustomerDO::getCustomerEid,request.getCustomerEid());
        UserCustomerDO userCustomerDO = this.getOne(wrapper);
        if (Objects.nonNull(userCustomerDO) && UserCustomerStatusEnum.getByCode(userCustomerDO.getStatus()) != UserCustomerStatusEnum.REJECT ) {
            throw new BusinessException(UserErrorCode.CUSTOMER_STATUS_ERROR);
        }

        EnterpriseDO enterpriseDTO = enterpriseService.getByLicenseNumber(request.getLicenseNumber());
        // 校验：不同机构类型，企业资质必填项，只有已经存在于系统且审核通过的企业才不需要校验
        if (Objects.isNull(enterpriseDTO) || EnterpriseAuthStatusEnum.AUTH_SUCCESS != EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus())){
            checkEnterpriseCertificate(request.getType() , PojoUtils.map(request.getCertificateList(),CreateEnterpriseCertificateRequest.class));
        }

        //修改企业信息
        Long customerEid;
        if (Objects.nonNull(enterpriseDTO)){
            customerEid = enterpriseDTO.getId();
            // 如果更新后的企业信息依然存在于系统：审核成功的企业会自动带入，审核失败或待审核的企业才需要更新企业信息
            if (EnterpriseAuthStatusEnum.AUTH_SUCCESS != EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus())){
                this.updateAuthEnterprise(request, customerEid);
            }

        } else {
            customerEid = request.getCustomerEid();
            this.updateAuthEnterprise(request, customerEid);
        }

        LambdaQueryWrapper<UserCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCustomerDO::getUserId,request.getUserId());
        queryWrapper.eq(UserCustomerDO::getCustomerEid,request.getCustomerEid());
        UserCustomerDO userCustomerDo = this.getOne(queryWrapper);

        UserCustomerDO customerDO = new UserCustomerDO();
        customerDO.setId(userCustomerDo.getId());
        customerDO.setCustomerEid(customerEid);
        if (Objects.nonNull(enterpriseDTO) && EnterpriseAuthStatusEnum.AUTH_SUCCESS == EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus())) {
            customerDO.setStatus(UserCustomerStatusEnum.PASS.getCode());
        } else {
            customerDO.setStatus(UserCustomerStatusEnum.WAITING.getCode());
        }
        String date = "1970-01-01 00:00:00";
        customerDO.setAuditTime(DateUtil.parseDate(date));
        customerDO.setRejectReason("");
        this.updateById(customerDO);

        return true;
    }

    private void updateAuthEnterprise(UpdateUserCustomerRequest request, Long customerEid) {
        UpdateEnterpriseRequest enterpriseRequest = PojoUtils.map(request, UpdateEnterpriseRequest.class);
        enterpriseRequest.setId(customerEid);
        enterpriseService.update(enterpriseRequest);
        // 添加企业审核信息
        this.addEnterpriseAuth(PojoUtils.map(request, AddUserCustomerRequest.class), customerEid);
    }

    @Override
    public List<UserCustomerDTO> queryCustomerList(QueryUserCustomerRequest request) {
        return this.baseMapper.queryPageList(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomerStatus(UpdateCustomerStatusRequest request) {
        if (Objects.isNull(request.getCustomerEid()) || Objects.isNull(request.getStatus()) || request.getStatus() == 0) {
            return false;
        }

        LambdaQueryWrapper<UserCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCustomerDO::getCustomerEid,request.getCustomerEid());

        UserCustomerDO userCustomerDO = new UserCustomerDO();
        userCustomerDO.setStatus(request.getStatus());
        userCustomerDO.setOpUserId(request.getOpUserId());
        userCustomerDO.setRejectReason(request.getRejectReason());
        userCustomerDO.setAuditTime(request.getAuditTime());
        return update(userCustomerDO,queryWrapper);
    }

    @Override
    public UserCustomerDTO getByCustomerEid(Long customerEid) {
        LambdaQueryWrapper<UserCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCustomerDO::getCustomerEid,customerEid);
        queryWrapper.eq(UserCustomerDO::getStatus,UserCustomerStatusEnum.PASS.getCode());
        queryWrapper.orderByAsc(UserCustomerDO::getCreateTime);
        queryWrapper.last("limit 1");

        UserCustomerDO userCustomerDO = this.getOne(queryWrapper);
        if (Objects.nonNull(userCustomerDO)) {
            UserCustomerDTO userCustomerDTO = new UserCustomerDTO();
            PojoUtils.map(userCustomerDO,userCustomerDTO);
            return userCustomerDTO;
        }
        return null;
    }

    @Override
    public boolean checkUserCustomer(Long userId, Long customerEid) {
        //校验是否已经是当前用户的客户
        LambdaQueryWrapper<UserCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCustomerDO::getUserId, userId);
        queryWrapper.eq(UserCustomerDO::getCustomerEid, customerEid);
        int count = this.count(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean checkUserSaleArea(Long userId, Long eid, UserTypeEnum userType, String regionCode){
        // 如果为小三元则校验企业销售区域，自然人才校验用户销售区域
        if (userType == UserTypeEnum.XIAOSANYUAN || userType == UserTypeEnum.YILING) {
            List<String> saleAreaCodeList = enterpriseSalesAreaDetailService.getEnterpriseSaleAreaDetail(eid).stream().map(EnterpriseSalesAreaDetailDO::getAreaCode).collect(Collectors.toList());
            return saleAreaCodeList.contains(regionCode);

        } else {
            UserSalesAreaDO userSalesAreaDTO = userSalesAreaService.getSaleAreaByUserId(userId);
            if (Objects.isNull(userSalesAreaDTO)) {
                return false;
            }
            if (userSalesAreaDTO.getSalesAreaAllFlag() == 0) {
                List<String> saleAreaDetailList = userSalesAreaService.getSaleAreaDetailByUserId(userId, 3);
                return saleAreaDetailList.contains(regionCode);
            }
        }

        return true;
    }

    @Override
    public UserCustomerDTO getByUserAndCustomerEid(Long userId, Long customerEid) {
        LambdaQueryWrapper<UserCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCustomerDO::getUserId, userId);
        queryWrapper.eq(UserCustomerDO::getCustomerEid, customerEid);
        UserCustomerDO userCustomerDO = this.getOne(queryWrapper);

        return PojoUtils.map(userCustomerDO,UserCustomerDTO.class);
    }

}
