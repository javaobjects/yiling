package com.yiling.sales.assistant.app.mr.question.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.question.api.QuestionApi;
import com.yiling.cms.question.api.QuestionReplyApi;
import com.yiling.cms.question.dto.QuestionDTO;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionReplyDetailInfoDTO;
import com.yiling.cms.question.dto.ReplyFileDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.SaveQuestionReplyRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.standard.api.StandardGoodsPicApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.sales.assistant.app.mr.question.form.QueryQuestionDelegatePageForm;
import com.yiling.sales.assistant.app.mr.question.form.SaveQuestionReplyForm;
import com.yiling.sales.assistant.app.mr.question.vo.DocumentVO;
import com.yiling.sales.assistant.app.mr.question.vo.FileReplyVO;
import com.yiling.sales.assistant.app.mr.question.vo.QuestionDelegateDetailVO;
import com.yiling.sales.assistant.app.mr.question.vo.QuestionDelegatePageListVO;
import com.yiling.sales.assistant.app.mr.question.vo.QuestionReplyDetailInfoVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * <p>
 * 疑问处理
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Api(tags = "疑问处理")
@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {

    @DubboReference
    QuestionApi questionApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    DocumentApi documentApi;
    @DubboReference
    StandardGoodsPicApi standardGoodsPicApi;
    @DubboReference
    QuestionReplyApi questionReplyApi;
    @DubboReference
    UserApi userApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "医生问题列表")
    @PostMapping("/pageList")
    public Result<Page<QuestionDelegatePageListVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryQuestionDelegatePageForm form) {
        QueryQuestionPageRequest request = PojoUtils.map(form, QueryQuestionPageRequest.class);
        request.setType(2);
        request.setToUserId(staffInfo.getCurrentUserId());
        Page<QuestionDTO> questionList = questionApi.listPage(request);
        Page<QuestionDelegatePageListVO> result = PojoUtils.map(questionList, QuestionDelegatePageListVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/delegate/detail")
    public Result<QuestionDelegateDetailVO> delegateDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "questionId") Long questionId) {
        QuestionDetailInfoDTO questionDetail = questionApi.getQuestionDetail(questionId);
        QuestionDelegateDetailVO result = PojoUtils.map(questionDetail, QuestionDelegateDetailVO.class);
        if(result != null){
            Long specificationId = questionDetail.getStandardGoodsList().stream().map(o -> o.getSellSpecificationsId()).collect(Collectors.toList()).get(0);
            StandardGoodsSpecificationDTO specification = standardGoodsSpecificationApi.getStandardGoodsSpecification(specificationId);
            if(specification != null){
                result.setName(specification.getName());
                result.setSellSpecifications(specification.getSellSpecifications());
                List<StandardGoodsPicDTO> standardGoodsPic = standardGoodsPicApi.getStandardGoodsPic(specification.getStandardId(), specification.getId());
                String goodsSpecificationsPicture ;
                if(CollectionUtil.isNotEmpty(standardGoodsPic)){
                    goodsSpecificationsPicture = getGoodsPicUrl(standardGoodsPic.get(0).getPic());
                }else{
                    goodsSpecificationsPicture = getGoodsPicUrl(null);
                }
                result.setGoodsSpecificationsPicture(goodsSpecificationsPicture);
            }

            List<String> keyList = questionDetail.getKeyList();
            if(CollectionUtil.isNotEmpty(keyList)){
                List<FileInfoVO> pictureList = new ArrayList<>();
                for(String one : keyList){
                    FileInfoVO file = new FileInfoVO();
                    file.setFileKey(one);
                    file.setFileUrl(fileService.getUrl(one, FileTypeEnum.QUESTION_RESOURCE_PICTURE));
                    pictureList.add(file);
                }
                result.setPictureList(pictureList);
            }

            List<QuestionReplyDetailInfoDTO> replyDetailList = questionDetail.getReplyDetailList();
            if(CollectionUtil.isNotEmpty(replyDetailList)){
                List<QuestionReplyDetailInfoVO> questionReplyList = PojoUtils.map(replyDetailList, QuestionReplyDetailInfoVO.class);
                Map<Long, QuestionReplyDetailInfoVO> replyMap = questionReplyList.stream().collect(Collectors.toMap(QuestionReplyDetailInfoVO::getId, o -> o, (k1, k2) -> k1));
                List<QuestionReplyDetailInfoVO> replyList = new ArrayList<>();
                for(QuestionReplyDetailInfoDTO replyOne : replyDetailList){
                    QuestionReplyDetailInfoVO questionReplyDetailOne = replyMap.get(replyOne.getId());
                    UserDTO user = userApi.getById(replyOne.getCreateUser());
                    if(user != null){
                        questionReplyDetailOne.setCreateUserName(user.getName());
                    }

                    if(CollectionUtil.isNotEmpty(replyOne.getReplyDocumentIdList())){
                        List<DocumentDTO> documentDTOS = documentApi.getDocumentByIds(replyOne.getReplyDocumentIdList());
                        if(CollectionUtil.isNotEmpty(documentDTOS)){
                            List<DocumentVO> replyDocumentList = new ArrayList<>();
                            documentDTOS.stream().forEach(s->{
                                DocumentVO documentOne = new DocumentVO();
                                documentOne.setDocumentId(s.getId());
                                documentOne.setDocumentTitle(s.getTitle());
                                replyDocumentList.add(documentOne);
                            });
                            questionReplyDetailOne.setReplyDocumentList(replyDocumentList);
                        }
                    }

                    if(CollectionUtil.isNotEmpty(replyOne.getReplyFileKeyList())){
                        List<FileReplyVO> replyFileList = new ArrayList<>();
                        for(ReplyFileDTO one : replyOne.getReplyFileKeyList() ){
                            FileReplyVO file = new FileReplyVO();
                            file.setFileKey(one.getReplyFileKey());
                            file.setFileUrl(fileService.getUrl(one.getReplyFileKey(), FileTypeEnum.QUESTION_REPLY_RESOURCE_PICTURE));
                            file.setFileName(one.getFileName());
                            replyFileList.add(file);
                        }
                        questionReplyDetailOne.setReplyFileList(replyFileList);
                    }

                    if(CollectionUtil.isNotEmpty(replyOne.getReplyPictureKeyList())){
                        List<FileInfoVO> replyPictureList = new ArrayList<>();
                        for(String one : replyOne.getReplyPictureKeyList() ){
                            FileInfoVO file = new FileInfoVO();
                            file.setFileKey(one);
                            file.setFileUrl(fileService.getUrl(one, FileTypeEnum.QUESTION_REPLY_RESOURCE_PICTURE));
                            replyPictureList.add(file);
                        }
                        questionReplyDetailOne.setReplyPictureList(replyPictureList);
                    }
                    replyList.add(questionReplyDetailOne);
                }
                result.setReplyList(replyList);
            }
        }
        return Result.success(result);
    }

    @ApiOperation(value = "用药回复")
    @PostMapping("/reply/add")
    public Result<Boolean> saveQuestionReply(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveQuestionReplyForm form) {
        SaveQuestionReplyRequest request = PojoUtils.map(form, SaveQuestionReplyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = questionReplyApi.saveQuestionReply(request);
        return Result.success(result);
    }

    private String getGoodsPicUrl(String pic) {
        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
        }
    }

}
