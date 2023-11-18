package com.yiling.user.agreementv2.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.bo.AgreementImportListItemBO;
import com.yiling.user.agreementv2.bo.AgreementListItemBO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreementv2.dto.request.QueryImportAgreementListRequest;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 协议主条款表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-23
 */
@Repository
public interface AgreementMainTermsMapper extends BaseMapper<AgreementMainTermsDO> {

    /**
     * 协议分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<AgreementListItemBO> queryAgreementPage(Page page, @Param("request") QueryAgreementPageListRequest request);

    /**
     * 导入协议选择列表
     *
     * @param request
     * @return
     */
    List<AgreementImportListItemBO> queryImportAgreementList(@Param("request") QueryImportAgreementListRequest request);
}
