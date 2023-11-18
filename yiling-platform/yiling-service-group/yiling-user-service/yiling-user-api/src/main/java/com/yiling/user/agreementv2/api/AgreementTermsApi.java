package com.yiling.user.agreementv2.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

/**
 * 协议条款 API
 *
 * @author: lun.yu
 * @date: 2022/2/23
 */
public interface AgreementTermsApi {

    /**
     * 新建协议
     *
     * @param request
     * @return
     */
    Boolean createAgreement(CreateAgreementRequest request);

    /**
     * 查询协议是否存在，存在则返回协议编码
     *
     * @param request
     * @return
     */
    String checkAgreementExist(QueryAgreementExistRequest request);

    /**
     * 创建/修改乙方签订人
     *
     * @param request
     * @return
     */
    Boolean saveAgreementSecondUser(SaveAgreementSecondUserRequest request);

    /**
     * 删除乙方签订人
     *
     * @param id
     * @param opUserId
     * @return
     */
    Boolean deleteAgreementSecondUser(Long id, Long opUserId);

    /**
     * 分页查询乙方签订人
     *
     * @param request
     * @return
     */
    Page<AgreementSecondUserDTO> querySecondUserListPage(QueryAgreementSecondUserPageRequest request);

    /**
     * 返利条款页面选择商品分页列表
     *
     * @param request
     * @return
     */
    Page<AgreementSupplySalesGoodsDTO> querySupplyGoodsPage(QueryAgreementGoodsPageRequest request);

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
     */
    Boolean updateArchiveAgreement(UpdateArchiveAgreementRequest request);

    /**
     * 获取供销商品数量
     *
     * @param agreementId
     * @return
     */
    Integer getSupplyGoodsNumber(Long agreementId);

}
