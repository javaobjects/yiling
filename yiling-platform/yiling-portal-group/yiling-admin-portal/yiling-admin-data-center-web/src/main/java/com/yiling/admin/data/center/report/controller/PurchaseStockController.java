package com.yiling.admin.data.center.report.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.report.form.QueryPurchasePageForm;
import com.yiling.admin.data.center.report.form.QueryReviseStockPageForm;
import com.yiling.admin.data.center.report.form.QueryStockOccupayPageForm;
import com.yiling.admin.data.center.report.form.ReviseStockForm;
import com.yiling.admin.data.center.report.vo.GoodsInfoVO;
import com.yiling.admin.data.center.report.vo.RebatedGoodsCountVO;
import com.yiling.admin.data.center.report.vo.ReportPurchasePageItemVO;
import com.yiling.admin.data.center.report.vo.ReportPurchaseStockReviseVO;
import com.yiling.admin.data.center.report.vo.ReviseStockPageItemVO;
import com.yiling.admin.data.center.report.vo.StockOccupyPageItemVO;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryListPageDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryLogListPageDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequestDetail;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.RebatedGoodsCountDTO;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.request.QueryRevisePurchaseStockRequest;
import com.yiling.settlement.report.dto.request.QueryReviseStockPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.enums.ReportTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@RestController
@RequestMapping("/report/stock")
@Api(tags = "进销存中心")
@Slf4j
public class PurchaseStockController extends BaseController {

    @DubboReference
    ReportApi reportApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    FlowPurchaseInventoryApi flowPurchaseInventoryApi;

    @ApiOperation(value = "查询商品")
    @GetMapping("/queryGoods")
    public Result<CollectionObject<List<GoodsInfoVO>>> queryGoods(@RequestParam @Valid @NotEmpty String key) {
        // 出货价、供货价、商品类型
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setSize(100);
        request.setName(key);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);

        List<GoodsInfoVO> list = PojoUtils.map(page.getRecords(), GoodsInfoVO.class);
        list.forEach(o -> {
            o.setSellSpecificationsId(0L);
        });
        CollectionObject<List<GoodsInfoVO>> result = new CollectionObject(list);
        return Result.success(result);
    }


    @ApiOperation(value = "查询采购库存列表")
    @PostMapping("/queryPurchasePage")
    public Result<Page<ReportPurchasePageItemVO>> queryPurchasePage(@RequestBody @Valid QueryPurchasePageForm form) {
        QueryFlowPurchaseInventoryListPageRequest request=PojoUtils.map(form,QueryFlowPurchaseInventoryListPageRequest.class);

        //查询流向库存
        Page<FlowPurchaseInventoryListPageDTO> page = flowPurchaseInventoryApi.pageByEidAndYlGoodsId(request);
        Page<ReportPurchasePageItemVO> result=PojoUtils.map(page,ReportPurchasePageItemVO.class);
        List<ReportPurchasePageItemVO> records=result.getRecords();
        if (CollUtil.isEmpty(records)){
            return Result.success(form.getPage());
        }
        //查询调整库存
        Map<Long, BigDecimal> goodsStockList = flowPurchaseInventoryApi.getSumAdjustmentQuantityByInventoryIdList(records.stream().map(ReportPurchasePageItemVO::getId).collect(Collectors.toList()));

        //查询已返利数量
        List<QueryRevisePurchaseStockRequest> queryParList= ListUtil.toList();
        records.forEach(e->{
            QueryRevisePurchaseStockRequest var=PojoUtils.map(e,QueryRevisePurchaseStockRequest.class);
            var.setPurchaseChannel(e.getPoSource());
            queryParList.add(var);
        });
        List<RebatedGoodsCountDTO> goodsRebateCountList = reportApi.queryRebateCount(PojoUtils.map(queryParList,QueryStockOccupyPageRequest.class));
        Map<RebatedGoodsCountVO,Integer> goodsRebateCountMap = PojoUtils.map(goodsRebateCountList, RebatedGoodsCountVO.class).stream().collect(Collectors.toMap(e->e,RebatedGoodsCountVO::getQuantity));
        records.forEach(e->{
            RebatedGoodsCountVO var=new RebatedGoodsCountVO();
            var.setEid(e.getEid());
            var.setYlGoodsId(e.getYlGoodsId());
            var.setGoodsInSn(e.getGoodsInSn());
            var.setPurchaseChannel(e.getPoSource());
            e.setRebateStock(goodsRebateCountMap.getOrDefault(var,0).longValue());
            e.setReviseStock(goodsStockList.getOrDefault(e.getId(),BigDecimal.ZERO).longValue());

            e.setTotalPoQuantity(e.getTotalPoQuantity()+goodsStockList.getOrDefault(e.getId(),BigDecimal.ZERO).longValue());
            e.setPoQuantity(e.getPoQuantity());
        });
        return Result.success(result);
    }

    @ApiOperation(value = "调整采购库存")
    @PostMapping("/reviseStock")
    public Result<Boolean> reviseStock(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid ReviseStockForm form) {
        UpdateFlowPurchaseInventoryQuantityRequest request=new UpdateFlowPurchaseInventoryQuantityRequest();
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT_ADJUSTMENT.getCode());
        UpdateFlowPurchaseInventoryQuantityRequestDetail detailRequest=new UpdateFlowPurchaseInventoryQuantityRequestDetail();
        detailRequest.setQuantity(new BigDecimal(form.getQuantity().toString()));
        detailRequest.setId(form.getId());
        request.setList(ListUtil.toList(detailRequest));
        int result = flowPurchaseInventoryApi.updateQuantityById(request);
        return Result.success(result>0);
    }

    @ApiOperation(value = "查询库存调整记录")
    @PostMapping("/queryStockRevisePage")
    public Result<Page<ReviseStockPageItemVO>> queryStockRevisePage(@RequestBody @Valid QueryReviseStockPageForm form) {
        QueryFlowPurchaseInventoryLogListPageRequest request = new QueryFlowPurchaseInventoryLogListPageRequest();
        request.setInventoryId(form.getId());
        request.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT_ADJUSTMENT.getCode());
        Page<FlowPurchaseInventoryLogListPageDTO> page=flowPurchaseInventoryApi.adjustmentLogPageLogByInventoryId(request);
        Page<ReviseStockPageItemVO> result = PojoUtils.map(page, ReviseStockPageItemVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "查询库存占用记录")
    @PostMapping("/queryStockOccupyPage")
    public Result<Page<StockOccupyPageItemVO>> queryStockOccupyPage(@RequestBody @Valid QueryStockOccupayPageForm form) {
        Page<ReportPurchaseStockOccupyDTO> page = reportApi.queryStockOccupyPage(PojoUtils.map(form, QueryStockOccupyPageRequest.class));
        Page<StockOccupyPageItemVO> result = PojoUtils.map(page, StockOccupyPageItemVO.class);
        if (CollUtil.isEmpty(result.getRecords())){
            return Result.success(result);
        }
        //查询占用库存的商品单位
        List<Long> goodsIdList = result.getRecords().stream().filter(e->ObjectUtil.equal(e.getType(), ReportTypeEnum.B2B.getCode())).map(StockOccupyPageItemVO::getGoodsId).distinct().collect(Collectors.toList());
        Map<Long, GoodsDTO> goodsMap = goodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsDTO::getId,e->e));
        //查询企业信息
        List<Long> eidList = result.getRecords().stream().map(StockOccupyPageItemVO::getSellerEid).distinct().collect(Collectors.toList());
        Map<Long, String> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        result.getRecords().forEach(e->{
            e.setSellerName(entMap.getOrDefault(e.getSellerEid(),""));
            GoodsDTO goodsDTO = goodsMap.get(e.getYlGoodsId());
            if (ObjectUtil.equal(e.getType(), ReportTypeEnum.B2B.getCode())&&ObjectUtil.isNotNull(goodsDTO)){
                e.setSoUnit(goodsDTO.getSellUnit());
            }
        });
        return Result.success(result);
    }
}
