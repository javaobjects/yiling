package com.yiling.open.cms.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.bo.OssCallbackResult;
import com.yiling.framework.oss.bo.OssPolicyResult;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/6/17
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "文件上传模块")
@Slf4j
public class UploadController extends BaseController {

    @Autowired
    FileService fileService;


    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "上传文件", required = true), @ApiImplicitParam(name = "type", value = "文件类型：" + "商品图片（goodsPicture）" + "，企业资质图片（enterpriseCertificate）" + "，banner图片（bannerPicture）" + "，订单收货回执单（orderReceiveOneReceipt）" + "，订单购销合同附件（orderContract）" + "，富文本编辑器中的图片及附件（richTextEditorFile）" + "，移动端安装包文件（appInstallPackage）" + "，会员权益图标（memberEquityPicture）" + "，赠品库图片（marketingGoodsGiftPicture）" + "，身份证正面照（idCardFrontPhoto）" + "，身份证反面照（idCardBackPhoto）" + "，组合包活动图片（combinationPackagePicture）" + "，专场活动图片（specialActivityPicture）" + "，店铺Logo（shopLogoPicture）" + "，会员购买推广分享二维码（taskMemberShare）" + "，协议附件（agreementAttachment）" + "，内容封面（contentCover）" + "，药加险广告图片（advertisementPic）" + "，处方图片（prescriptionPic）" + "，会议活动封面图（meetingBackgroundPic）" + ",疑问知识库图片视频(questionResourcePicture)" + ",疑问知识库回复图片视频(questionReplyResourcePicture)" + "", required = true) })
    @PostMapping("/upload")
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws Exception {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }
        FileInfo fileInfo = fileService.upload(file, fileType);
        return Result.success(fileInfo);
    }


    @ApiOperation("获取oss上传签名生成")
    @ApiImplicitParams({ @ApiImplicitParam(name = "type", value = "文件类型，参考FileTypeEnum枚举", required = true) })
    @GetMapping("/policy/{type}")
    public Result<OssPolicyResult> policy(@PathVariable("type") String type) {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }

        OssPolicyResult result = fileService.policy(fileType);
        return Result.success(result);
    }

    @ApiOperation(value = "oss上传成功回调")
    @PostMapping("/callback")
    public Result<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = fileService.callback(request);
        log.debug("ossCallbackResult={}", JSONUtil.toJsonStr(ossCallbackResult));
        return Result.success(ossCallbackResult);
    }

    @ApiOperation(value = "跳转到文件URL")
    @GetMapping("/redirect")
    public Result redirect(@RequestParam("type") String type, @RequestParam("key") String key, HttpServletResponse response) throws IOException {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }

        String url = fileService.getUrl(key, fileType);
        response.sendRedirect(url);
        return Result.success();
    }
}
