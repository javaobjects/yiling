package com.yiling.admin.cms.document.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.cms.content.vo.ContentVO;
import com.yiling.admin.cms.document.form.AddDocumentForm;
import com.yiling.admin.cms.document.form.QueryDocumentPageForm;
import com.yiling.admin.cms.document.form.UpdateDocumentForm;
import com.yiling.admin.cms.document.vo.DocumentGoodsVO;
import com.yiling.admin.cms.document.vo.DocumentVO;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.AddDocumentRequest;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
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
    @Autowired
    private FileService fileService;

    @DubboReference
    private StandardGoodsApi standardGoodsApi;

    @Log(title = "添加文献",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加")
    @PostMapping("addContent")
    public Result<Boolean> addContent(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid AddDocumentForm addDocumentForm){
        AddDocumentRequest request = new AddDocumentRequest();
        PojoUtils.map(addDocumentForm,request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        documentApi.addDocument(request);
        return Result.success(true);
    }

    @Log(title = "编辑文献",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("编辑")
    @PostMapping("updateContent")
    public Result<Boolean> updateContent(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdateDocumentForm updateDocumentForm){
        UpdateDocumentRequest request = new UpdateDocumentRequest();
        PojoUtils.map(updateDocumentForm,request);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        documentApi.updateDocument(request);
        return Result.success(true);
    }

    @ApiOperation("详情")
    @GetMapping("getDocumentById")
    public Result<DocumentVO> getContentById(@RequestParam Long id){
        DocumentDTO documentDTO = documentApi.getDocumentById(id);
        DocumentVO documentVO = PojoUtils.map(documentDTO, DocumentVO.class);
        documentVO.setDocumentFileUrlKey(documentVO.getDocumentFileUrl());
        documentVO.setDocumentFileUrl(fileService.getUrl(documentVO.getDocumentFileUrl(), FileTypeEnum.DOCUMENT_FILE_URL));

        List<String> stringList = Arrays.asList(documentDTO.getDisplayLine().split(","));
        List<Long> longList = PojoUtils.map(stringList,Long.class);
        documentVO.setDisplayLines(longList);
        List<Long> standardGoodsIdList = documentDTO.getStandardGoodsIdList();
        if(CollUtil.isNotEmpty(standardGoodsIdList)){
            List<DocumentGoodsVO> standardGoodsList = Lists.newArrayListWithExpectedSize(standardGoodsIdList.size());

            standardGoodsIdList.forEach(standardGoodsId->{
                DocumentGoodsVO documentGoodsVO = new DocumentGoodsVO();
                documentGoodsVO.setId(standardGoodsId);
                StandardGoodsAllInfoDTO standardGoods = standardGoodsApi.getStandardGoodsById(standardGoodsId);
                if(Objects.nonNull(standardGoods)){
                    documentGoodsVO.setName(Optional.ofNullable(standardGoods).get().getBaseInfo().getName());
                }
                standardGoodsList.add(documentGoodsVO);
            });
            documentVO.setStandardGoodsList(standardGoodsList);
        }


        return Result.success(documentVO);
    }

    @ApiOperation("分页列表")
    @GetMapping("queryContentPage")
    public Result<Page<ContentVO>> queryContentPage(QueryDocumentPageForm queryDocumentPageForm){
        QueryDocumentPageRequest request = new QueryDocumentPageRequest();
        PojoUtils.map(queryDocumentPageForm,request);
        Page<DocumentDTO> documentDTOPage = documentApi.listPage(request);
        if(documentDTOPage.getTotal()==0){
            return Result.success(queryDocumentPageForm.getPage());
        }
        List<Long> createUserIds = documentDTOPage.getRecords().stream().map(DocumentDTO::getCreateUser).collect(Collectors.toList());
        List<Long> updateUserIds = documentDTOPage.getRecords().stream().map(DocumentDTO::getUpdateUser).distinct().collect(Collectors.toList());
        List<Long> userIds = CollUtil.union(createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        Page<ContentVO> contentVOPage = PojoUtils.map(documentDTOPage,ContentVO.class);
        contentVOPage.getRecords().forEach(contentVO -> {
            contentVO.setCreateUserName(userDTOMap.getOrDefault(contentVO.getCreateUser(), new UserDTO()).getName());
            UserDTO userDTO = userDTOMap.get(contentVO.getUpdateUser());
            contentVO.setUpdateUserName(Optional.ofNullable(userDTO).map(UserDTO::getName).get());
        });
        return Result.success(contentVOPage);
    }
}
