package com.yiling.admin.sales.assistant.task.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.sales.assistant.task.form.ImportTaskGoodsForm;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.user.enterprise.api.EnterpriseApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/5/28
 */
@Component
@Slf4j
public class ImportTaskGoodsDataHandler implements ImportDataHandler<ImportTaskGoodsForm> {

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    private B2bGoodsApi b2bGoodsApi;
    @Override
    public List<ImportTaskGoodsForm> execute(List<ImportTaskGoodsForm> object, Map<String,Object> paramMap) {
        List<Long> goodsIds = object.stream().map(ImportTaskGoodsForm::getGid).collect(Collectors.toList());
        List<GoodsDTO> goodsInfoDTOS = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> goodsDTOMap = goodsInfoDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        //查询是否以岭品
        List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setSize(3000).setCurrent(1);
        request.setGoodsLine(GoodsLineEnum.B2B.getCode()).setEidList(eidList);
        Page<GoodsListItemBO> goodsListItemBOPage = b2bGoodsApi.queryB2bGoodsPageList(request);
        List<Long> yilingGoodsIds = goodsListItemBOPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
        //Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIds,subEids);
        HashSet<Long> set = new HashSet<>();

        for (ImportTaskGoodsForm importGoodsForm : object) {
            if(null == goodsDTOMap.get(importGoodsForm.getGid())){
                importGoodsForm.setErrorMsg(GoodsErrorCode.NOT_EXIST.getMessage());
            }else if(!yilingGoodsIds.contains(importGoodsForm.getGid())){
                importGoodsForm.setErrorMsg("非B2B以岭品");
            }
            boolean add = set.add(importGoodsForm.getGid());
            if(!add){
                importGoodsForm.setErrorMsg("数据重复");
            }
        }
        return object;
    }
}
