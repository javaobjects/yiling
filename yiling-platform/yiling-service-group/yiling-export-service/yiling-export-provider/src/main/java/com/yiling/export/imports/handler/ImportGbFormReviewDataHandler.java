package com.yiling.export.imports.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.alibaba.fastjson.JSON;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.export.bo.GbFormReviewMessageBO;
import com.yiling.export.imports.model.ImportGbFormReviewDataModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.dto.GbFormDTO;
import com.yiling.sjms.gb.enums.GbFormReviewStatusEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入团购复核数据
 *
 * @author wei.wang
 * @date 2023/02/28
 */
@Slf4j
@Service
public class ImportGbFormReviewDataHandler extends AbstractExcelImportHandler<ImportGbFormReviewDataModel> {

    @DubboReference
    FormApi formApi;
    @DubboReference
    GbFormApi gbFormApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;


    @Override
    public ExcelVerifyHandlerResult verify(ImportGbFormReviewDataModel form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        if(StringUtils.isNotBlank(form.getReviewReply()) && form.getReviewReply().length()>200){

            return this.error(form.getGbNo()+"团购复核编号长度最大200");
        }
        return result;
    }

    @Override
    public List<ImportGbFormReviewDataModel> importData(List<ImportGbFormReviewDataModel> object, Map<String, Object> paramMap) {
        //Long opUserId = (Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0L);
        log.info("ImportGbFormReviewDataHandler团购复核导入开始");
        List< String> codes = object.stream().map(ImportGbFormReviewDataModel::getGbNo).collect(Collectors.toList());
        List<FormDTO> formList = formApi.listByCodes(codes);
        Map<String, FormDTO> map = new HashMap<>();
        if(CollectionUtil.isNotEmpty(formList)){
            map = formList.stream().collect(Collectors.toMap(o -> o.getCode(), e -> e, (k1, k2) -> k1));
        }
        List<GbFormReviewMessageBO> list = new ArrayList<>();
        for (ImportGbFormReviewDataModel form : object) {
            FormDTO formDTO = map.get(form.getGbNo());
            if(formDTO == null){
                form.setErrorMsg(form.getGbNo()+"团购复核编号查询不到数据");
                continue;
            }


            if(FormStatusEnum.getByCode(formDTO.getStatus()) != FormStatusEnum.APPROVE){
                form.setErrorMsg(form.getGbNo()+"团购复核编码状态必须是审核通过");
                continue;
            }

            try {
                GbFormDTO gbFormDTO = new GbFormDTO();
                gbFormDTO.setReviewStatus(GbFormReviewStatusEnum.PASS.getCode());
                gbFormDTO.setFormId(formDTO.getId());
                gbFormDTO.setReviewReply(form.getReviewReply());

                Boolean result = gbFormApi.updateByFormId(gbFormDTO);

                if (!result) {
                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                }
                FormDTO formOne = formApi.getById(formDTO.getId());
                if(FormTypeEnum.getByCode(formOne.getType()) == FormTypeEnum.GB_SUBMIT ){
                    GbFormReviewMessageBO reviewMessageBO = new GbFormReviewMessageBO();
                    reviewMessageBO.setFormId(formDTO.getId());
                    reviewMessageBO.setReviewReply(form.getReviewReply());
                    reviewMessageBO.setReviewStatus(GbFormReviewStatusEnum.PASS.getCode());
                    list.add(reviewMessageBO);
                }
            } catch (BusinessException be) {
                form.setErrorMsg(be.getMessage());
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        if(CollectionUtil.isNotEmpty(list)){
            int toIndex = 200;
            for (int i = 0; i < list.size(); i+=200) {
                if (i+200>list.size()) {
                    toIndex = list.size() - i;
                }
                List<GbFormReviewMessageBO> newList = list.subList(i, i+toIndex);
                log.info("团购复核发送团购数据list:{}",JSON.toJSONString(newList));
                MqMessageBO mqMessage = new MqMessageBO(Constants.TOPIC_FLOW_SALE_GB_REVIEW_APPROVED_TASK, Constants.TAG_FLOW_SALE_GB_REVIEW_APPROVED_TASK, JSON.toJSONString(newList));
                mqMessage = mqMessageSendApi.prepare(mqMessage);
                mqMessageSendApi.send(mqMessage);
            }
        }
        return object;
    }


}
