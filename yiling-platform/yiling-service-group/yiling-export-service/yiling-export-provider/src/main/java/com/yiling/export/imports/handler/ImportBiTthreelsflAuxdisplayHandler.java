package com.yiling.export.imports.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.bi.company.api.DimLsflChaincompanyApi;
import com.yiling.bi.company.dto.DimLsflChaincompanyDTO;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.protocol.api.InputTthreelsflAuxdisplayRecordApi;
import com.yiling.bi.protocol.dto.request.InputTthreelsflAuxdisplayRecordRequest;
import com.yiling.bi.protocol.enums.DisplayContentEnum;
import com.yiling.bi.protocol.enums.DisplayItemEnum;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportBiTthreelsflAuxdisplayModel;
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
public class ImportBiTthreelsflAuxdisplayHandler extends BaseImportHandler<ImportBiTthreelsflAuxdisplayModel> {

    @DubboReference
    private InputTthreelsflAuxdisplayRecordApi inputTthreelsflAuxdisplayRecordApi;

    @DubboReference
    private DimLsflChaincompanyApi dimLsflChaincompanyApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportBiTthreelsflAuxdisplayModel obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        // 验证陈列项目
        {
            if (Arrays.stream(DisplayItemEnum.values()).noneMatch(d -> d.getValue().equals(obj.getDisplayXm()))) {
                return this.error("陈列项目值不在范围内");
            }
        }

        // 验证陈列内容
        {
            if (Arrays.stream(DisplayContentEnum.values()).noneMatch(d -> d.getValue().equals(obj.getDisplayNr()))) {
                return this.error("陈列内容值不在范围内");
            }
        }

        // 验证品种
        {
            if (StringUtils.isNotEmpty(obj.getTxl()) && !"是".equals(obj.getTxl())) {
                return this.error("通心络值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getSsyx()) && !"是".equals(obj.getSsyx())) {
                return this.error("参松养心值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getQlqx()) && !"是".equals(obj.getQlqx())) {
                return this.error("芪苈强心值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getJld()) && !"是".equals(obj.getJld())) {
                return this.error("津力达值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getXlq()) && !"是".equals(obj.getXlq())) {
                return this.error("夏荔芪值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getRjt()) && !"是".equals(obj.getRjt())) {
                return this.error("乳结泰值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getYzxj()) && !"是".equals(obj.getYzxj())) {
                return this.error("养正消积值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getSll()) && !"是".equals(obj.getSll())) {
                return this.error("参灵蓝值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getJycf()) && !"是".equals(obj.getJycf())) {
                return this.error("解郁除烦值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getYsyx()) && !"是".equals(obj.getYsyx())) {
                return this.error("益肾养心值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getLhqw()) && !"是".equals(obj.getLhqw())) {
                return this.error("连花清瘟值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getLhqk()) && !"是".equals(obj.getLhqk())) {
                return this.error("连花清咳值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getXsfh()) && !"是".equals(obj.getXsfh())) {
                return this.error("消杀防护值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getSh()) && !"是".equals(obj.getSh())) {
                return this.error("双花值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getBzbs()) && !"是".equals(obj.getBzbs())) {
                return this.error("八子补肾值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getWba()) && !"是".equals(obj.getWba())) {
                return this.error("晚必安值不在范围内");
            }
            if (StringUtils.isNotEmpty(obj.getZsas()) && !"是".equals(obj.getZsas())) {
                return this.error("枣椹安神值不在范围内");
            }
        }

        return result;
    }

    @Override
    public List<ImportBiTthreelsflAuxdisplayModel> execute(List<ImportBiTthreelsflAuxdisplayModel> object, Map<String, Object> paramMap) {
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

        for (ImportBiTthreelsflAuxdisplayModel model : object) {
            try {
                InputTthreelsflAuxdisplayRecordRequest request = new InputTthreelsflAuxdisplayRecordRequest();
                request.setProvince(model.getProvince());
                request.setBzCode(model.getBzCode());
                request.setBzName(model.getBzName());
                request.setDisplayXm(model.getDisplayXm());
                request.setDisplayNr(model.getDisplayNr());
                request.setTxl("是".equals(model.getTxl()) ? "1" : "0");
                request.setSsyx("是".equals(model.getSsyx()) ? "1" : "0");
                request.setQlqx("是".equals(model.getQlqx()) ? "1" : "0");
                request.setJld("是".equals(model.getJld()) ? "1" : "0");
                request.setXlq("是".equals(model.getXlq()) ? "1" : "0");
                request.setRjt("是".equals(model.getRjt()) ? "1" : "0");
                request.setYzxj("是".equals(model.getYzxj()) ? "1" : "0");
                request.setSll("是".equals(model.getSll()) ? "1" : "0");
                request.setJycf("是".equals(model.getJycf()) ? "1" : "0");
                request.setYsyx("是".equals(model.getYsyx()) ? "1" : "0");
                request.setLhqw("是".equals(model.getLhqw()) ? "1" : "0");
                request.setLhqk("是".equals(model.getLhqk()) ? "1" : "0");
                request.setXsfh("是".equals(model.getXsfh()) ? "1" : "0");
                request.setSh("是".equals(model.getSh()) ? "1" : "0");
                request.setBzbs("是".equals(model.getBzbs()) ? "1" : "0");
                request.setWba("是".equals(model.getWba()) ? "1" : "0");
                request.setZsas("是".equals(model.getZsas()) ? "1" : "0");
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

                boolean result = inputTthreelsflAuxdisplayRecordApi.saveOrUpdateByUnique(request);
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
}
