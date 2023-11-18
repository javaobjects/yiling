package com.yiling.sjms.goodsplans.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockForecastApi;
import com.yiling.dataflow.statistics.dto.StockForecastInfoDTO;
import com.yiling.dataflow.statistics.dto.StockForecastSaleIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.goodsplans.form.StockForecastInfoForm;
import com.yiling.sjms.goodsplans.vo.StockForecastIconVo;
import com.yiling.sjms.goodsplans.vo.StockForecastInfoVo;
import com.yiling.sjms.goodsplans.vo.StockForecastSaleIconVo;
import com.yiling.sjms.goodsplans.vo.StockReferenceTimeInfoVo;
import com.yiling.sjms.goodsplans.vo.StockWarnVo;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 补货预测
 */
@Slf4j
@RestController
@RequestMapping("/sf")
@Api(tags = "补货预测")
public class StockForecastController extends BaseController {
    @DubboReference
    private FlowAnalyseStockForecastApi flowAnalyseStockForecastApi;
    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @ApiOperation(value = "补货预测详情")
    @PostMapping("/info")
    public Result<StockForecastInfoVo> getInfo(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid StockForecastInfoForm form) {
        SjmsUserDatascopeBO sjmsUserDataScope = getSjmsUserDataScope(userInfo);
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(sjmsUserDataScope.getOrgDatascope())){
            return Result.success(new StockForecastInfoVo());
        }
        StockForecastInfoRequest request = new StockForecastInfoRequest();
        PojoUtils.map(form, request);
        request.setSjmsUserDatascopeBO(sjmsUserDataScope);
        StockForecastInfoDTO dto = flowAnalyseStockForecastApi.stockForecastInfoVo(request);
        return Result.success(PojoUtils.map(dto, StockForecastInfoVo.class));
    }

    @ApiOperation(value = "补货预测销售数据")
    @PostMapping("/sale")
    public Result<List<StockForecastSaleIconVo>> getSaleData(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid StockForecastInfoForm form) {
        SjmsUserDatascopeBO sjmsUserDataScope = getSjmsUserDataScope(userInfo);
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(sjmsUserDataScope.getOrgDatascope())){
            return Result.success(new ArrayList<>());
        }
        StockForecastInfoRequest request = new StockForecastInfoRequest();
        PojoUtils.map(form, request);
        request.setSjmsUserDatascopeBO(sjmsUserDataScope);
        List<StockForecastSaleIconDTO> saleDataList = flowAnalyseStockForecastApi.getSaleData(request);
        List<StockForecastSaleIconVo> saleIconVos = PojoUtils.map(saleDataList, StockForecastSaleIconVo.class);
        return Result.success(saleIconVos);
    }
    private SjmsUserDatascopeBO getSjmsUserDataScope(CurrentSjmsUserInfo userInfo){
        // 获取权限
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("销售预测权限获取:empId={},longs={}",userInfo.getCurrentUserCode(),byEmpId);
        return byEmpId;
    }
}
