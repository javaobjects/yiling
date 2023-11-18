package com.yiling.sales.assistant.app.mr.document.controller;


import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.enums.ContentStatusEnum;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.sales.assistant.app.mr.document.form.QueryDocumentPageForm;
import com.yiling.sales.assistant.app.mr.document.vo.DocumentVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 文献 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
@Api(tags = "文献")
@RestController
@RequestMapping("/document")
public class DocumentController extends BaseController {
    @DubboReference
    private DocumentApi documentApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private StandardGoodsApi standardGoodsApi;
    @DubboReference
    private EmployeeApi employeeApi;

    @ApiOperation("详情")
    @GetMapping("getDocumentById")
    public Result<DocumentVO> getContentById(@RequestParam Long id){
        DocumentDTO documentDTO = documentApi.getAppDocumentById(id);
        DocumentVO documentVO = PojoUtils.map(documentDTO, DocumentVO.class);
        return Result.success(documentVO);
    }

    @ApiOperation("分页列表")
    @GetMapping("queryContentPage")
    public Result<Page<DocumentVO>> queryContentPage(@CurrentUser CurrentStaffInfo staffInfo, QueryDocumentPageForm queryDocumentPageForm){
        QueryDocumentPageRequest request = new QueryDocumentPageRequest();
        PojoUtils.map(queryDocumentPageForm,request);
        //业务线药代
        request.setDisplayLine("5");
        request.setStatus(ContentStatusEnum.PUBLISHED.getCode());
        request.setIsOpen(1);
        if(Objects.nonNull(staffInfo.getCurrentEmployeeId()) && staffInfo.getCurrentEmployeeId()>0 && staffInfo.getCurrentEid().equals(Constants.YILING_EID)){
            EnterpriseEmployeeDTO userEnterpriseEmployeeDTO = employeeApi.getById( staffInfo.getCurrentEmployeeId());
            if(Objects.nonNull(userEnterpriseEmployeeDTO)){
                //医药代表 可以看到非公开的
                if(userEnterpriseEmployeeDTO.getType().equals(EmployeeTypeEnum.MR.getCode())){
                    request.setIsOpen(null);
                }
            }
        }
        Page<DocumentDTO> documentDTOPage = documentApi.listPage(request);
        if(documentDTOPage.getTotal()==0){
            return Result.success(queryDocumentPageForm.getPage());
        }
        Page<DocumentVO> contentVOPage = PojoUtils.map(documentDTOPage,DocumentVO.class);

        return Result.success(contentVOPage);
    }
}
