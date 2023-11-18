package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.bi.company.api.DimLsflChaincompanyApi;
import com.yiling.bi.company.dto.DimLsflChaincompanyDTO;
import com.yiling.bi.goods.api.InputGoodsRelationShipApi;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.protocol.api.InputTthreelsflRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflRecordRequest;
import com.yiling.bi.protocol.enums.QdTypeEnum;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportBiTthreelsflModel;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/4
 */
@Slf4j
@Service
public class ImportBiTthreelsflHandler extends BaseImportHandler<ImportBiTthreelsflModel> {

    @DubboReference
    private InputGoodsRelationShipApi inputGoodsRelationShipApi;

    @DubboReference
    private InputTthreelsflRecordApi inputTthreelsflRecordApi;

    @DubboReference
    private DimLsflChaincompanyApi dimLsflChaincompanyApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportBiTthreelsflModel obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        // 规格校验
        {
            String goodsSpec = obj.getGoodsSpec();
            if (StringUtils.isNotEmpty(goodsSpec)) {
                List<InputGoodsRelationShipDTO> inputGoodsRelationShipDTOList = inputGoodsRelationShipApi.getByLsSpecAndLikeAppId(goodsSpec, "3");
                if (CollUtil.isEmpty(inputGoodsRelationShipDTOList)) {
                    return this.error("规格不在范围内");
                }
            }
        }

        // 是连花清瘟配额产品的品种校验
        {
            String sfLhpe = obj.getSfLhpe();
            if (!StringUtils.isEmpty(sfLhpe) && !"是".equals(sfLhpe)) {
                return this.error("是连花清瘟配额产品的品种值不在范围内");
            }
        }

        // 签订类型校验
        {
            String qdType = obj.getQdType();
            if (!StringUtils.isEmpty(qdType) && Arrays.stream(QdTypeEnum.values()).map(QdTypeEnum::getValue).noneMatch(v -> v.equals(qdType))) {
                return this.error("签订类型值不在范围内");
            }
        }

        // 数值校验
        {
            StringBuilder sb = new StringBuilder();

            if (!awardVerify(obj.getClAward())) {
                sb.append("陈列奖励类型异常 ");
            }
            if (!awardVerify(obj.getLxAward())) {
                sb.append("流向奖励类型异常 ");
            }
            if (!awardVerify(obj.getWjAward())) {
                sb.append("维价奖励类型异常 ");
            }
            if (!awardVerify(obj.getTargetAward())) {
                sb.append("目标达成奖励类型异常 ");
            }

            if (StringUtils.isNotEmpty(obj.getTtwoRecord()) && !isNumeric(obj.getTtwoRecord())) {
                sb.append("2022年达成类型异常 ");
            }
            if (StringUtils.isNotEmpty(obj.getQuarter1Num()) && !isNumeric(obj.getQuarter1Num())) {
                sb.append("1季度类型异常 ");
            }
            if (StringUtils.isNotEmpty(obj.getQuarter2Num()) && !isNumeric(obj.getQuarter2Num())) {
                sb.append("2季度类型异常 ");
            }
            if (StringUtils.isNotEmpty(obj.getQuarter3Num()) && !isNumeric(obj.getQuarter3Num())) {
                sb.append("3季度类型异常 ");
            }
            if (StringUtils.isNotEmpty(obj.getQuarter4Num()) && !isNumeric(obj.getQuarter4Num())) {
                sb.append("4季度类型异常 ");
            }
            if (StringUtils.isNotEmpty(sb.toString())) {
                return this.error(sb.toString());
            }
        }

        return result;
    }

    @Override
    public List<ImportBiTthreelsflModel> execute(List<ImportBiTthreelsflModel> object, Map<String, Object> paramMap) {
        String param = (String) paramMap.get(ImportConstants.PARAM);
        String dyear = null;
        String createId = null;
        String createRole = null;
        if (!StringUtils.isEmpty(param)) {
            JSONObject json = JSONUtil.parseObj(param);
            dyear = json.getStr("dyear");
            createId =  json.getStr("createId");
            createRole = json.getStr("createRole");
        }

        for (ImportBiTthreelsflModel model : object) {
            try {
                InputTthreelsflRecordRequest request = new InputTthreelsflRecordRequest();
                request.setProvince(model.getProvince());
                request.setBzCode(model.getBzCode());
                request.setBzName(model.getBzName());
                request.setNkaZb(model.getNkaZb());
                request.setLsType(model.getLsType());
                request.setStoreNum(StringUtils.isNotEmpty(model.getStoreNum()) ? new BigDecimal(model.getStoreNum()) : null);
                request.setGoodsType(model.getGoodsType());
                request.setBreed(model.getBreed());
                request.setGoodsId(model.getGoodsId());
                request.setDosageForm(model.getDosageForm());
                request.setGoodsSpec(model.getGoodsSpec());
                request.setHsPrice(model.getHsPrice());
                request.setLsPrice(StringUtils.isNotEmpty(model.getLsPrice()) ? new BigDecimal(model.getLsPrice()) : null);
                request.setSfLhpe("是".equals(model.getSfLhpe()) ? "1" : "0");
                String key = QdTypeEnum.getKey(model.getQdType());
                request.setQdType(key);
                request.setGjBusiness1(model.getGjBusiness1());
                request.setGjBusiness2(model.getGjBusiness2());
                request.setGjBusiness3(model.getGjBusiness3());
                request.setClAward(awardHandle(model.getClAward()));
                request.setLxAward(awardHandle(model.getLxAward()));
                request.setWjAward(awardHandle(model.getWjAward()));
                request.setTargetAward(awardHandle(model.getTargetAward()));
                request.setTtwoRecord(StringUtils.isNotEmpty(model.getTtwoRecord()) ? new  BigDecimal(model.getTtwoRecord()): null);
                request.setQuarter1Num(StringUtils.isNotEmpty(model.getQuarter1Num()) ? new BigDecimal(model.getQuarter1Num()): null);
                request.setQuarter2Num(StringUtils.isNotEmpty(model.getQuarter2Num()) ? new BigDecimal(model.getQuarter2Num()): null);
                request.setQuarter3Num(StringUtils.isNotEmpty(model.getQuarter3Num()) ? new BigDecimal(model.getQuarter3Num()): null);
                request.setQuarter4Num(StringUtils.isNotEmpty(model.getQuarter4Num()) ? new BigDecimal(model.getQuarter4Num()): null);
                request.setDataTime(new Date());
                request.setDataName(createId);
                if (StringUtils.isEmpty(dyear)) {
                    throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED);
                }
                request.setDyear(dyear);

                if (StringUtils.isEmpty(createRole) || StringUtils.isEmpty(createId)) {
                    throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED);
                }

                List<DimLsflChaincompanyDTO> companyList = null;
                if ("0".equals(createRole)) {   // 省区经理
                    companyList = dimLsflChaincompanyApi.getByDbCodeAndChainCode(createId, request.getBzCode());
                } else if ("1".equals(createRole)) {    // 运营经理
                    companyList = dimLsflChaincompanyApi.getByChainCode(request.getBzCode());
                }
                if (CollUtil.isEmpty(companyList)) {
                    throw new BusinessException(ResultCode.FORBIDDEN, "连锁公司编码权限验证失败");
                }

                boolean result = inputTthreelsflRecordApi.saveOrUpdateByUnique(request);
                if (!result) {
                    throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED);
                }

            } catch (BusinessException e) {
                model.setErrorMsg(e.getMessage());
            } catch (Exception e) {
                model.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private boolean awardVerify(String award) {
        if (StringUtils.isNotEmpty(award)) {
            if (award.contains("%")) {
                String numStr = award.substring(0, award.lastIndexOf("%"));
                if (!isNumeric(numStr)) {
                    return false;
                }
            } else if (!isNumeric(award)) {
                return false;
            }
        }
        return true;
    }

    private BigDecimal awardHandle(String award) {
        if (StringUtils.isEmpty(award)) {
            return null;
        }
        if (award.contains("%")) {
            String numStr = award.substring(0, award.lastIndexOf("%"));
            // 百分比转换为小数
            BigDecimal num = new BigDecimal(numStr);
            return num.divide(new BigDecimal(100), 6, RoundingMode.HALF_UP);
        }

        return new BigDecimal(award);
    }
}
