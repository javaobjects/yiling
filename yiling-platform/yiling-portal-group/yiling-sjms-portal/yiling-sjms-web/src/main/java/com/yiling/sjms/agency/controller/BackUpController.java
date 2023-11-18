package com.yiling.sjms.agency.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmEnterpriseBackUpPageForm;
import com.yiling.sjms.agency.vo.CrmAgencyDetailsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2023/3/14 0014
 */
@Slf4j
@RestController
@RequestMapping("/backUp")
@Api(tags = "备份查询")
public class BackUpController extends BaseController {

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @ApiOperation(value = "根据条件分页查询备份表机构基础信息")
    @PostMapping("/crmEnterprise/pageList")
    public Result<Page<CrmAgencyDetailsVO>> pageEnterpriseList(@RequestBody @Valid QueryCrmEnterpriseBackUpPageForm form) {
        QueryCrmEnterpriseBackUpPageRequest request = PojoUtils.map(form, QueryCrmEnterpriseBackUpPageRequest.class);
        request.setBusinessCode(1);
        Page<CrmEnterpriseDTO> dtoPage = crmEnterpriseApi.pageListSuffixBackUpInfo(request);
        Page<CrmAgencyDetailsVO> voPage = PojoUtils.map(dtoPage, CrmAgencyDetailsVO.class);
        return Result.success(voPage);
    }
}
