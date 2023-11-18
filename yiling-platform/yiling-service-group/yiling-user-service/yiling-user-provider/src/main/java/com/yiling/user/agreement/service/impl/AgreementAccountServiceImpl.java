//package com.yiling.user.agreement.service.impl;
//
//import java.util.List;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yiling.framework.common.base.BaseServiceImpl;
//import com.yiling.framework.common.util.PojoUtils;
//import com.yiling.user.agreement.dao.AgreementAccountMapper;
//import com.yiling.user.agreement.dto.AgreementAccountDTO;
//import com.yiling.user.agreement.dto.request.QueryAgreementAccountPageRequest;
//import com.yiling.user.agreement.dto.request.QueryAgreementAccountRequest;
//import com.yiling.user.agreement.entity.AgreementAccountDO;
//import com.yiling.user.agreement.service.AgreementAccountService;
//import org.springframework.stereotype.Service;
//
///**
// * <p>
// * 协议账号表 服务实现类
// * </p>
// *
// * @author shuang.zhang
// * @date 2021-07-14
// */
//@Service
//public class AgreementAccountServiceImpl extends BaseServiceImpl<AgreementAccountMapper, AgreementAccountDO> implements AgreementAccountService {
//
//    @Override
//    public AgreementAccountDTO getAgreementAccount(QueryAgreementAccountRequest request) {
//        QueryWrapper<AgreementAccountDO> agreementAccountWrapper = new QueryWrapper<>();
//        agreementAccountWrapper.lambda().eq(AgreementAccountDO::getAgreementId, request.getAgreementId());
//        agreementAccountWrapper.lambda().eq(AgreementAccountDO::getEasAccount,request.getEasAccount());
//        return PojoUtils.map(this.getOne(agreementAccountWrapper),AgreementAccountDTO.class);
//    }
//
//    @Override
//    public Page<AgreementAccountDTO> pageList(QueryAgreementAccountPageRequest request) {
//        QueryWrapper<AgreementAccountDO> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(AgreementAccountDO::getEid, request.getEid());
//        wrapper.lambda().eq(AgreementAccountDO::getEasAccount, request.getEasAccount());
//        Page<AgreementAccountDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
//        return PojoUtils.map(page,AgreementAccountDTO.class);
//    }
//}
