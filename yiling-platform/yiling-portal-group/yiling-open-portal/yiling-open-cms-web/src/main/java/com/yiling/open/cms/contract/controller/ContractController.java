package com.yiling.open.cms.contract.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.contract.api.ContractApi;
import com.yiling.basic.contract.dto.request.CallBackRequest;
import com.yiling.basic.contract.dto.request.ContractCancelRequest;
import com.yiling.basic.contract.dto.request.ContractCreateRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.IdObject;
import com.yiling.framework.common.web.rest.UrlObject;
import com.yiling.open.cms.contract.form.CallBackForm;
import com.yiling.open.cms.contract.form.ContractCancelForm;
import com.yiling.open.cms.contract.form.ContractCreateForm;
import com.yiling.open.cms.contract.form.ContractRecallForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author fucheng.bai
 * @date 2022/11/18
 */
@Api(tags = "合同管理")
@RestController
@RequestMapping("/contract")
public class ContractController extends BaseController {

    @DubboReference(timeout = 30 * 1000)
    private ContractApi contractApi;

    @ApiOperation(value = "创建合同")
    @PostMapping("/createByCategory")
    public Result<IdObject<String>> createByCategory(@RequestBody ContractCreateForm form) {
        Long contractId = contractApi.createByCategory(PojoUtils.map(form, ContractCreateRequest.class));
        return Result.success(new IdObject<>(String.valueOf(contractId)));
    }

    @ApiOperation(value = "查看合同")
    @PostMapping("/viewUrl")
    public Result<UrlObject<String>> viewUrl(@RequestParam(value = "contractId") Long contractId) {
        return Result.success(new UrlObject<>(contractApi.viewUrl(contractId)));
    }

    @ApiOperation(value = "发起合同")
    @PostMapping("/send")
    public Result sendContract(@RequestParam(value = "contractId") Long contractId) {
        contractApi.sendContract(contractId);
        return Result.success();
    }

    @ApiOperation(value = "撤回合同")
    @PostMapping("/recall")
    public Result recallContract(ContractRecallForm form) {
        Long contractId = form.getContractId();
        String reason = form.getReason();
        contractApi.recallContract(contractId, reason);
        return Result.success();
    }

    @ApiOperation(value = "作废合同")
    @PostMapping("/cancel")
    public Result cancelContract(@RequestBody ContractCancelForm form) {
        contractApi.cancelContract(PojoUtils.map(form, ContractCancelRequest.class));
        return Result.success();
    }

    @ApiOperation(value = "删除合同")
    @PostMapping("/delete")
    public Result deleteContract(@RequestParam(value = "contractId") Long contractId) {
        contractApi.deleteContract(contractId);
        return Result.success();
    }

    @ApiOperation(value = "合同完成回调（契约锁回调接口）")
    @PostMapping(value = "/complete/callback")
    public Result completeCallBack(CallBackForm form) {
        contractApi.completeCallBack(PojoUtils.map(form, CallBackRequest.class));
        return Result.success();
    }

    @ApiOperation(value = "下载合同")
    @PostMapping("/download")
    public Result<UrlObject<String>> downloadContract(@RequestParam(value = "contractId") Long contractId) {
        return Result.success(new UrlObject<>(contractApi.downloadContract(contractId)));
    }

    @ApiOperation(value = "查看合同状态")
    @PostMapping("/status")
    public Result<Map<String, Object>> status(@RequestParam(value = "contractId") Long contractId) {
        String status = contractApi.getContractStatus(contractId);
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return Result.success(map);
    }

}
