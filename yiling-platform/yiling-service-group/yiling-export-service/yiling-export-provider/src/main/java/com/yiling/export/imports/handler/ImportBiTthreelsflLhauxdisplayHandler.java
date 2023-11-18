package com.yiling.export.imports.handler;

import java.math.BigDecimal;
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
import com.yiling.bi.protocol.api.InputTthreelsflLhauxdisplayRecordApi;
import com.yiling.bi.protocol.dto.InputTthreelsflLhauxdisplayRecordDTO;
import com.yiling.bi.protocol.dto.request.InputTthreelsflLhauxdisplayRecordRequest;
import com.yiling.bi.protocol.enums.DisplayItemEnum;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportBiTthreelsflLhauxdisplayModel;
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
public class ImportBiTthreelsflLhauxdisplayHandler  extends BaseImportHandler<ImportBiTthreelsflLhauxdisplayModel> {

    @DubboReference
    private InputTthreelsflLhauxdisplayRecordApi inputTthreelsflLhauxdisplayRecordApi;

    @DubboReference
    private DimLsflChaincompanyApi dimLsflChaincompanyApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportBiTthreelsflLhauxdisplayModel obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        // 验证陈列项目
        {
            if (Arrays.stream(DisplayItemEnum.values()).noneMatch(d -> d.getValue().equals(obj.getDisplayXm()))) {
                return this.error("陈列项目值不在范围内");
            }
        }

        // 辅助陈列门店家数验证
        {
            if (StringUtils.isNotEmpty(obj.getDisplayStorenum()) && !isNumeric(obj.getDisplayStorenum())) {
                return this.error("辅助陈列门店家数类型错误");
            }
        }

        // 陈列具体项目验证
        {
            if (StringUtils.isNotEmpty(obj.getBracket()) && !"是".equals(obj.getBracket())) {
                return this.error("端架值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getPilehead()) && !"是".equals(obj.getPilehead())) {
                return this.error("堆头值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getFlowerCar()) && !"是".equals(obj.getFlowerCar())) {
                return this.error("花车值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getGtPilehead()) && !"是".equals(obj.getGtPilehead())) {
                return this.error("柜台堆头值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getCashDesk()) && !"是".equals(obj.getCashDesk())) {
                return this.error("收银台值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getStud()) && !"是".equals(obj.getStud())) {
                return this.error("立柱值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getLampBox()) && !"是".equals(obj.getLampBox())) {
                return this.error("灯箱值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getShowbill()) && !"是".equals(obj.getShowbill())) {
                return this.error("吊旗值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getShopwindow()) && !"是".equals(obj.getShopwindow())) {
                return this.error("橱窗值不在范围内");
            }
        }

        return result;
    }

    @Override
    public List<ImportBiTthreelsflLhauxdisplayModel> execute(List<ImportBiTthreelsflLhauxdisplayModel> object, Map<String, Object> paramMap) {
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

        for (ImportBiTthreelsflLhauxdisplayModel model : object) {
            try {
                InputTthreelsflLhauxdisplayRecordRequest request = new InputTthreelsflLhauxdisplayRecordRequest();
                request.setProvince(model.getProvince());
                request.setBzCode(model.getBzCode());
                request.setBzName(model.getBzName());
                request.setDisplayXm(model.getDisplayXm());
                request.setDisplayStorenum(StringUtils.isNotEmpty(model.getDisplayStorenum()) ? new BigDecimal(model.getDisplayStorenum()) : null);
                request.setStoreLevel(model.getStoreLevel());
                request.setBracket("是".equals(model.getBracket()) ? "1" : "0");
                request.setPilehead("是".equals(model.getPilehead()) ? "1" : "0");
                request.setFlowerCar("是".equals(model.getFlowerCar()) ? "1" : "0");
                request.setGtPilehead("是".equals(model.getGtPilehead()) ? "1" : "0");
                request.setCashDesk("是".equals(model.getCashDesk()) ? "1" : "0");
                request.setStud("是".equals(model.getStud()) ? "1" : "0");
                request.setLampBox("是".equals(model.getLampBox()) ? "1" : "0");
                request.setShowbill("是".equals(model.getShowbill()) ? "1" : "0");
                request.setShopwindow("是".equals(model.getShopwindow()) ? "1" : "0");
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
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }

                boolean result = inputTthreelsflLhauxdisplayRecordApi.saveOrUpdateByUnique(request);
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
        return null;
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
