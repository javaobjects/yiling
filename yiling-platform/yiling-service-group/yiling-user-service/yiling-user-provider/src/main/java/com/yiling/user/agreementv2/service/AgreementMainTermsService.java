package com.yiling.user.agreementv2.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.bo.AgreementAuthListItemBO;
import com.yiling.user.agreementv2.bo.AgreementDetailBO;
import com.yiling.user.agreementv2.bo.AgreementGoodsListItemBO;
import com.yiling.user.agreementv2.bo.AgreementImportListItemBO;
import com.yiling.user.agreementv2.bo.AgreementListItemBO;
import com.yiling.user.agreementv2.dto.AgreementMainTermsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.CreateAgreementRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementAuthPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementExistRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreementv2.dto.request.QueryImportAgreementListRequest;
import com.yiling.user.agreementv2.dto.request.UpdateArchiveAgreementRequest;
import com.yiling.user.agreementv2.dto.request.UpdateAuthAgreementRequest;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议主条款表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-23
 */
public interface AgreementMainTermsService extends BaseService<AgreementMainTermsDO> {

    /**
     * 创建协议
     *
     * @param request
     * @return
     */
    Boolean createAgreement(CreateAgreementRequest request);

    /**
     * 添加协议主条款
     *
     * @param request
     * @return
     */
    AgreementMainTermsDO addAgreementMainTerms(AddAgreementMainTermsRequest request);

    /**
     * 查询协议是否存在，存在则返回协议编号
     *
     * @param request
     * @return
     */
    String checkAgreementExist(QueryAgreementExistRequest request);

    /**
     * 查询协议主条款
     *
     * @param request
     * @return
     */
    List<AgreementMainTermsDO> getAgreementMainTerms(QueryAgreementMainTermsRequest request);

    /**
     * 生成协议流水号
     *
     * @param agreementType
     * @return
     */
    Integer generateSerialNo(Integer agreementType);

    /**
     * 分页查询协议列表
     *
     * @param request
     * @return
     */
    Page<AgreementListItemBO> queryAgreementPage(QueryAgreementPageListRequest request);

    /**
     * 协议商品分页列表
     *
     * @param request
     * @return
     */
    Page<AgreementGoodsListItemBO> queryAgreementGoodsPage(QueryAgreementGoodsPageRequest request);

    /**
     * 获取协议详情
     *
     * @param id
     * @return
     */
    AgreementDetailBO getDetail(Long id);

    /**
     * 查询协议审核列表
     *
     * @param request
     * @return
     */
    Page<AgreementAuthListItemBO> queryAgreementAuthPage(QueryAgreementAuthPageRequest request);

    /**
     * 审核协议
     *
     * @param request
     * @return
     */
    Boolean updateAuthAgreement(UpdateAuthAgreementRequest request);

    /**
     * 查询协议导入选择协议列表
     *
     * @param request
     * @return
     */
    List<AgreementImportListItemBO> queryImportAgreementList(QueryImportAgreementListRequest request);

    /**
     * 归档协议
     *
     * @param request
     * @return
     */
    Boolean updateArchiveAgreement(UpdateArchiveAgreementRequest request);

}
