package com.yiling.user.enterprise.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.common.enums.EnterprisePurchaseApplyDataTypeEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dao.EnterprisePurchaseApplyMapper;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.AddPurchaseApplyRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePurchaseApplyPageRequest;
import com.yiling.user.enterprise.dto.request.UpdatePurchaseApplyStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterprisePurchaseApplyDO;
import com.yiling.user.enterprise.enums.EnterprisePurchaseApplyStatusEnum;
import com.yiling.user.enterprise.service.EnterpriseCustomerLineService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterprisePurchaseApplyService;
import com.yiling.user.enterprise.service.EnterpriseService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * <p>
 * 企业采购申请 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
@Service
public class EnterprisePurchaseApplyServiceImpl extends BaseServiceImpl<EnterprisePurchaseApplyMapper, EnterprisePurchaseApplyDO> implements EnterprisePurchaseApplyService {

    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    EnterpriseCustomerLineService enterpriseCustomerLineService;

    @Autowired
    private EnterprisePurchaseApplyMapper enterprisePurchaseApplyMapper;

    @Override
    public EnterprisePurchaseApplyBO getByCustomerEid(Long customerEid, Long eid) {
        return enterprisePurchaseApplyMapper.getByCustomerEid(customerEid,eid);
    }

    @Override
    public EnterprisePurchaseApplyBO getByEid(Long customerEid, Long eid) {
        return enterprisePurchaseApplyMapper.getByEid(customerEid,eid);
    }

    @Override
    public Page<EnterprisePurchaseApplyBO> pageList(QueryEnterprisePurchaseApplyPageRequest request) {
        request.setDataType(Objects.isNull(request.getDataType()) || request.getDataType() == 0 ? EnterprisePurchaseApplyDataTypeEnum.BUY.getCode() : request.getDataType());
        return enterprisePurchaseApplyMapper.pageList(request.getPage(), request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPurchaseApply(AddPurchaseApplyRequest request) {
        LambdaQueryWrapper<EnterprisePurchaseApplyDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterprisePurchaseApplyDO::getCustomerEid,request.getCustomerEid());
        queryWrapper.eq(EnterprisePurchaseApplyDO::getEid,request.getEid());
        EnterprisePurchaseApplyDO enterprisePurchaseApplyDO = this.getOne(queryWrapper);
        if (Objects.nonNull(enterprisePurchaseApplyDO)) {
            // 重新提交申请
            if (EnterprisePurchaseApplyStatusEnum.REJECTED != EnterprisePurchaseApplyStatusEnum.getByCode(enterprisePurchaseApplyDO.getAuthStatus())) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_HAD_ESTABLISHED_PURCHASE);
            }
            enterprisePurchaseApplyDO.setAuthStatus(EnterprisePurchaseApplyStatusEnum.WAITING.getCode());
            enterprisePurchaseApplyDO.setAuthRejectReason("");
            String date = "1970-01-01 00:00:00";
            enterprisePurchaseApplyDO.setAuthTime(DateUtil.parseDate(date));
            enterprisePurchaseApplyDO.setAuthUser(0L);
            enterprisePurchaseApplyDO.setOpUserId(request.getOpUserId());
            this.updateById(enterprisePurchaseApplyDO);

        } else {
            EnterprisePurchaseApplyDO purchaseApplyDO = PojoUtils.map(request, EnterprisePurchaseApplyDO.class);
            purchaseApplyDO.setAuthStatus(EnterprisePurchaseApplyStatusEnum.WAITING.getCode());
            purchaseApplyDO.setOpUserId(request.getOpUserId());
            this.save(purchaseApplyDO);
        }

        return true;
    }

    @Override
    @GlobalTransactional
    public boolean updateAuthStatus(UpdatePurchaseApplyStatusRequest request) {
        EnterprisePurchaseApplyDO purchaseApplyDO = Optional.ofNullable(this.getById(request.getId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXIST_PURCHASE));
        // 状态流转校验：必须是待审核状态才能审核通过or审核驳回
        if (EnterprisePurchaseApplyStatusEnum.getByCode(purchaseApplyDO.getAuthStatus()) != EnterprisePurchaseApplyStatusEnum.WAITING) {
            throw new BusinessException(UserErrorCode.PURCHASE_AUTH_STATUS_ERROR);
        }
        if (EnterprisePurchaseApplyStatusEnum.getByCode(request.getAuthStatus()) == EnterprisePurchaseApplyStatusEnum.REJECTED) {
            if (StrUtil.isEmpty(request.getAuthRejectReason())) {
                Assert.notNull(request.getAuthRejectReason(), "驳回原因不能为空");
            }
        }

        EnterprisePurchaseApplyDO applyDO = PojoUtils.map(request,EnterprisePurchaseApplyDO.class);
        applyDO.setOpUserId(request.getOpUserId());
        applyDO.setOpTime(new Date());
        applyDO.setAuthTime(new Date());
        applyDO.setAuthUser(request.getOpUserId());

        this.updateById(applyDO);

        // 审核通过后加入企业客户表：如果存在企业客户关系，则只需入产品线，不存在则都需要入
        if (EnterprisePurchaseApplyStatusEnum.getByCode(request.getAuthStatus()) == EnterprisePurchaseApplyStatusEnum.ESTABLISHED) {

            EnterpriseCustomerDO enterpriseCustomerDO = enterpriseCustomerService.get(purchaseApplyDO.getEid(), purchaseApplyDO.getCustomerEid());
            EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(purchaseApplyDO.getCustomerEid())).orElse(new EnterpriseDO());
            if (Objects.isNull(enterpriseCustomerDO)) {
                AddCustomerRequest customerRequest = new AddCustomerRequest();
                customerRequest.setEid(purchaseApplyDO.getEid());
                customerRequest.setCustomerEid(purchaseApplyDO.getCustomerEid());
                customerRequest.setCustomerName(enterpriseDO.getName());
                customerRequest.setAddPurchaseRelationFlag(false);
                customerRequest.setOpUserId(request.getOpUserId());
                enterpriseCustomerService.addB2bCustomer(customerRequest);

            } else {
                AddCustomerLineRequest lineRequest = new AddCustomerLineRequest();
                lineRequest.setEid(purchaseApplyDO.getEid());
                lineRequest.setCustomerEid(purchaseApplyDO.getCustomerEid());
                lineRequest.setCustomerName(enterpriseDO.getName());
                lineRequest.setCustomerId(enterpriseCustomerDO.getId());
                EnterpriseDO enterprise = Optional.ofNullable(enterpriseService.getById(purchaseApplyDO.getEid())).orElse(new EnterpriseDO());
                lineRequest.setEname(enterprise.getName());
                lineRequest.setOpUserId(request.getOpUserId());
                lineRequest.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
                enterpriseCustomerLineService.add(ListUtil.toList(lineRequest));

            }
        }

        return true;
    }

    /**
     * 根据采购商和供应商获取采购状态
     * @param eidList 供应商企业ID集合
     * @param customerEid 采购商企业ID
     * @return 状态标识：1.去建采 2.审核中 3.已建采
     */
    @Override
    public Map<Long,Integer> getPurchaseApplyStatus(List<Long> eidList, Long customerEid) {
        Map<Long,Integer> map = MapUtil.newHashMap();
        eidList.forEach(eid -> {
            //获取采购关系
            boolean openFlag = enterpriseCustomerLineService.getCustomerLineFlag(eid, customerEid, EnterpriseCustomerLineEnum.B2B);
            if (openFlag) {
                map.put(eid,3);

            } else {

                EnterprisePurchaseApplyBO purchaseApplyBO = this.getByCustomerEid(customerEid, eid);
                if (Objects.isNull(purchaseApplyBO)) {
                    map.put(eid,1);
                } else if (EnterprisePurchaseApplyStatusEnum.getByCode(purchaseApplyBO.getAuthStatus()) == EnterprisePurchaseApplyStatusEnum.ESTABLISHED) {
                    map.put(eid,3);
                } else if (EnterprisePurchaseApplyStatusEnum.getByCode(purchaseApplyBO.getAuthStatus()) == EnterprisePurchaseApplyStatusEnum.REJECTED) {
                    map.put(eid,1);
                } else {
                    map.put(eid,2);
                }
            }
        });

        return map;
    }
}
