package com.yiling.user.agreementv2.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.api.AgreementTermsApi;
import com.yiling.user.agreementv2.bo.AgreementAuthListItemBO;
import com.yiling.user.agreementv2.bo.AgreementDetailBO;
import com.yiling.user.agreementv2.bo.AgreementGoodsListItemBO;
import com.yiling.user.agreementv2.bo.AgreementImportListItemBO;
import com.yiling.user.agreementv2.bo.AgreementListItemBO;
import com.yiling.user.agreementv2.dto.AgreementSecondUserDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementAuthPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementSecondUserPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryImportAgreementListRequest;
import com.yiling.user.agreementv2.dto.request.SaveAgreementSecondUserRequest;
import com.yiling.user.agreementv2.dto.request.CreateAgreementRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementExistRequest;
import com.yiling.user.agreementv2.dto.request.UpdateArchiveAgreementRequest;
import com.yiling.user.agreementv2.dto.request.UpdateAuthAgreementRequest;
import com.yiling.user.agreementv2.entity.AgreementSecondUserDO;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.user.agreementv2.service.AgreementSecondUserService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * 厂家管理 API 实现
 *
 * @author: lun.yu
 * @date: 2022/2/22
 */
@Slf4j
@DubboService
public class AgreementTermsApiImpl implements AgreementTermsApi {

    @Autowired
    private AgreementMainTermsService agreementMainTermsService;
    @Autowired
    private AgreementSecondUserService agreementSecondUserService;
    @Autowired
    private AgreementSupplySalesGoodsService agreementSupplySalesGoodsService;

    @Override
    public Boolean createAgreement(CreateAgreementRequest request) {
        return agreementMainTermsService.createAgreement(request);
    }

    @Override
    public String checkAgreementExist(QueryAgreementExistRequest request) {
        return agreementMainTermsService.checkAgreementExist(request);
    }

    @Override
    public Boolean saveAgreementSecondUser(SaveAgreementSecondUserRequest request) {
        return agreementSecondUserService.saveAgreementSecondUser(request);
    }

    @Override
    public Boolean deleteAgreementSecondUser(Long id, Long opUserId) {
        AgreementSecondUserDO secondUserDO = new AgreementSecondUserDO();
        secondUserDO.setOpUserId(opUserId);
        secondUserDO.setId(id);
        agreementSecondUserService.deleteByIdWithFill(secondUserDO);
        return true;
    }

    @Override
    public Page<AgreementSecondUserDTO> querySecondUserListPage(QueryAgreementSecondUserPageRequest request) {
        return agreementSecondUserService.querySecondUserListPage(request);
    }

    @Override
    public Page<AgreementSupplySalesGoodsDTO> querySupplyGoodsPage(QueryAgreementGoodsPageRequest request) {
        return agreementSupplySalesGoodsService.getSupplyGoodsByAgreementId(request);
    }

    @Override
    public Page<AgreementListItemBO> queryAgreementPage(QueryAgreementPageListRequest request) {
        return agreementMainTermsService.queryAgreementPage(request);
    }

    @Override
    public Page<AgreementGoodsListItemBO> queryAgreementGoodsPage(QueryAgreementGoodsPageRequest request) {
        return agreementMainTermsService.queryAgreementGoodsPage(request);
    }

    @Override
    public AgreementDetailBO getDetail(Long id) {
        return agreementMainTermsService.getDetail(id);
    }

    @Override
    public Page<AgreementAuthListItemBO> queryAgreementAuthPage(QueryAgreementAuthPageRequest request) {
        return agreementMainTermsService.queryAgreementAuthPage(request);
    }

    @Override
    public Boolean updateAuthAgreement(UpdateAuthAgreementRequest request) {
        return agreementMainTermsService.updateAuthAgreement(request);
    }

    @Override
    public List<AgreementImportListItemBO> queryImportAgreementList(QueryImportAgreementListRequest request) {
        return agreementMainTermsService.queryImportAgreementList(request);
    }

    @Override
    public Boolean updateArchiveAgreement(UpdateArchiveAgreementRequest request) {
        return agreementMainTermsService.updateArchiveAgreement(request);
    }

    @Override
    public Integer getSupplyGoodsNumber(Long agreementId){
        return agreementSupplySalesGoodsService.getSupplyGoodsNumber(agreementId);
    }

}
