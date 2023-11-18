package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportOpenMemberModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.request.ImportOpenMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStageRequest;
import com.yiling.user.member.enums.MemberSourceEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入开通会员数据验证处理器
 *
 * @author: lun.yu
 * @date: 2022-09-30
 */
@Slf4j
@Component
public class ImportOpenMemberHandler extends AbstractExcelImportHandler<ImportOpenMemberModel> {

    @DubboReference
    MemberApi memberApi;
    @DubboReference
    MemberBuyStageApi memberBuyStageApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;

    @Override
    public ExcelVerifyHandlerResult verify(ImportOpenMemberModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        {
            // 企业ID
            Long eid = model.getEid();
            if (Objects.nonNull(eid) && eid != 0) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
                if (Objects.isNull(enterpriseDTO)) {
                    return this.error("企业不存在");
                }
            }
        }

        {
            // 会员ID
            Long memberId = model.getMemberId();
            if (Objects.nonNull(memberId) && memberId != 0) {
                MemberDTO memberDTO = memberApi.getById(memberId);
                if (Objects.isNull(memberDTO)) {
                    return this.error("会员信息不存在");
                }
            }
        }

        {
            // 购买规则：xxx元购买xxx天xxxx
            String buyRule = model.getBuyRule();
            if (StrUtil.isNotEmpty(buyRule)) {
                QueryMemberBuyStageRequest request = new QueryMemberBuyStageRequest();
                try {
                    int index1 = buyRule.indexOf("元");
                    String price = buyRule.substring(0, index1);
                    int index2 = buyRule.indexOf("买");
                    int index3 = buyRule.indexOf("天");
                    String validTime = buyRule.substring(index2 + 1, index3);
                    String name = buyRule.substring(index3 + 1);

                    request.setMemberId(model.getMemberId());
                    request.setPrice(new BigDecimal(price));
                    request.setValidTime(Integer.valueOf(validTime));
                    request.setName(name);

                } catch (Exception e) {
                    return this.error("购买规则格式错误");
                }

                MemberBuyStageDTO buyStageDTO = memberBuyStageApi.getBuyStageByCond(request);
                if (Objects.isNull(buyStageDTO)) {
                    return this.error("当前会员不存在此购买规则");
                }
            }
        }

        {
            // 导入类型
            String sourceName = model.getSourceName();
            if (StrUtil.isNotEmpty(sourceName)) {
                if (!MemberSourceEnum.IMPORT_GIVE.getName().contains(sourceName) && !MemberSourceEnum.IMPORT_P2P.getName().contains(sourceName)) {
                    return this.error("导入类型错误");
                }
            }
        }

        {
            // 推广方
            Long promoterId = model.getPromoterId();
            if (Objects.nonNull(promoterId) && promoterId != 0) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(promoterId);
                if (Objects.isNull(enterpriseDTO)) {
                    return this.error("推广方不存在");
                }
            }
        }

        {
            // 推广人
            Long promoterUserId = model.getPromoterUserId();
            if (Objects.nonNull(promoterUserId) && promoterUserId != 0) {
                UserDTO userDTO = userApi.getById(promoterUserId);
                if (Objects.isNull(userDTO)) {
                    return this.error("推广人不存在");
                }
            }
        }

        return result;
    }

    @Override
    public List<ImportOpenMemberModel> importData(List<ImportOpenMemberModel> object, Map<String,Object> paramMap) {
        for (ImportOpenMemberModel form : object) {
            try {
                ImportOpenMemberRequest request = PojoUtils.map(form, ImportOpenMemberRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                request.setEname(enterpriseApi.getById(form.getEid()).getName());
                request.setMemberName(memberApi.getById(form.getMemberId()).getName());

                String buyRule = request.getBuyRule();
                int index1 = buyRule.indexOf("元");
                String price = buyRule.substring(0, index1);
                int index2 = buyRule.indexOf("买");
                int index3 = buyRule.indexOf("天");
                String validTime = buyRule.substring(index2 + 1, index3);
                String name = buyRule.substring(index3 + 1);

                QueryMemberBuyStageRequest buyStageRequest = new QueryMemberBuyStageRequest();
                buyStageRequest.setMemberId(form.getMemberId());
                buyStageRequest.setPrice(new BigDecimal(price));
                buyStageRequest.setValidTime(Integer.valueOf(validTime));
                buyStageRequest.setName(name);
                MemberBuyStageDTO buyStageDTO = memberBuyStageApi.getBuyStageByCond(buyStageRequest);
                // 购买条件ID
                request.setBuyStageId(buyStageDTO.getId());

                // 来源
                if (MemberSourceEnum.IMPORT_GIVE.getName().contains(form.getSourceName())) {
                    request.setSource(MemberSourceEnum.IMPORT_GIVE.getCode());
                } else if (MemberSourceEnum.IMPORT_P2P.getName().contains(form.getSourceName())) {
                    request.setSource(MemberSourceEnum.IMPORT_P2P.getCode());
                }
                // 推广方名称
                if (Objects.nonNull(form.getPromoterId()) && form.getPromoterId() != 0) {
                    request.setPromoterName(enterpriseApi.getById(form.getPromoterId()).getName());
                }
                // 推广人名称
                if (Objects.nonNull(form.getPromoterUserId()) && form.getPromoterUserId() != 0) {
                    request.setPromoterUserName(userApi.getById(form.getPromoterUserId()).getName());
                }

                boolean result = memberApi.importBuyMember(request);
                if (!result) {
                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                }
            } catch (BusinessException be) {
                form.setErrorMsg(be.getMessage());
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }

}
