package com.yiling.open.cms.document.controller;


import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.api.MyCollectApi;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.enums.CollectStatusEnums;
import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.cms.content.enums.ContentStatusEnum;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.cms.read.api.MyReadApi;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.enums.ReadTypeEnums;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.open.cms.content.form.GetContentForm;
import com.yiling.open.cms.document.form.QueryDocumentPageForm;
import com.yiling.open.cms.document.vo.DocumentVO;
import com.yiling.user.system.api.UserApi;

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

    @DubboReference(async = true)
    MyReadApi myReadApi;

    @DubboReference
    MyCollectApi myCollectApi;

    @ApiOperation("详情")
    @GetMapping("getDocumentById")
    public Result<DocumentVO> getContentById(@Valid GetContentForm form){
        DocumentDTO documentDTO = documentApi.getAppDocumentById(form.getId());
        DocumentVO documentVO = PojoUtils.map(documentDTO, DocumentVO.class);
        QueryCollectRequest request = new QueryCollectRequest();
        request.setCollectId(form.getId()).setCollectType(ReadTypeEnums.DOCUMENT.getCode()).setSource(BusinessLineEnum.DOCTOR.getCode()).setOpUserId(form.getWxDoctorId());
        MyCollectDTO  myCollectDTO =myCollectApi.getOne(request);
        if(Objects.isNull(myCollectDTO) || null== myCollectDTO.getStatus()){
            documentVO.setCollectStatus(CollectStatusEnums.UN_COLLECTED.getCode());
        }else {
            documentVO.setCollectStatus(myCollectDTO.getStatus());
        }
        //我的阅读插入数据
        AddMyReadRequest readRequest = new AddMyReadRequest();
        readRequest.setContentTime(documentDTO.getCreateTime()).setReadType(ReadTypeEnums.DOCUMENT.getCode()).setSource(BusinessLineEnum.DOCTOR.getCode()).setReadId(documentDTO.getId()).setTitle(documentDTO.getTitle()).setOpTime(new Date());
        readRequest.setOpUserId(form.getWxDoctorId());
        myReadApi.save(readRequest);
        DubboUtils.quickAsyncCall("myReadApi", "save");
        return Result.success(documentVO);
    }

    @ApiOperation("分页列表")
    @GetMapping("queryContentPage")
    public Result<Page<DocumentVO>> queryContentPage(QueryDocumentPageForm queryDocumentPageForm){
        QueryDocumentPageRequest request = new QueryDocumentPageRequest();
        PojoUtils.map(queryDocumentPageForm,request);
        //业务线医生端
        request.setDisplayLine("2");
        request.setStatus(ContentStatusEnum.PUBLISHED.getCode());
        request.setIsOpen(1);
        Page<DocumentDTO> documentDTOPage = documentApi.listPage(request);
        if(documentDTOPage.getTotal()==0){
            return Result.success(queryDocumentPageForm.getPage());
        }
        Page<DocumentVO> contentVOPage = PojoUtils.map(documentDTOPage,DocumentVO.class);

        return Result.success(contentVOPage);
    }
}
