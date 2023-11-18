package com.yiling.basic.contract.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.contract.dto.ContractCreateDTO;
import com.yiling.basic.contract.dto.ContractParamDTO;
import com.yiling.basic.contract.dto.DocumentParamDTO;
import com.yiling.basic.contract.dto.request.CallBackRequest;
import com.yiling.basic.contract.entity.CovenantDO;
import com.yiling.basic.contract.enums.ContractStatusEnum;
import com.yiling.basic.contract.service.CovenantService;
import com.yiling.basic.contract.service.QysContractService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.qiyuesuo.sdk.api.ContractService;
import net.qiyuesuo.sdk.bean.company.TenantType;
import net.qiyuesuo.sdk.bean.contract.CreateContractRequest;
import net.qiyuesuo.sdk.bean.contract.SendContractRequest;
import net.qiyuesuo.sdk.bean.contract.ViewUrlRequest;
import net.qiyuesuo.sdk.bean.sign.Signatory;
import net.qiyuesuo.sdk.common.exception.PrivateAppException;
import net.qiyuesuo.sdk.common.utils.IOUtils;

/**
 * @author fucheng.bai
 * @date 2022/11/17
 */
@Slf4j
@Service
public class QysContractServiceImpl implements QysContractService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CovenantService covenantService;

    @Value("${qiyuesuo.signature.host:222.223.229.43}")
    private String qysHost;

    @Value("${qiyuesuo.signature.port:9180}")
    private String qysPort;


    @Override
    public Long createByCategory(ContractCreateDTO contractCreateDTO) {
        //  验证参数
        ContractParamDTO contractParamDTO = contractCreateDTO.getContractParam();
        DocumentParamDTO documentParamDTO = contractCreateDTO.getDocumentParam();
        if (contractParamDTO == null || documentParamDTO == null) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        //  创建合同文档
        Long documentId = createDocumentByTemplate(contractParamDTO, documentParamDTO);

        //  创建合同
        Long contractId = createContractByDocument(documentId, contractParamDTO);

        //  生成合同数据
        CovenantDO covenantDO = new CovenantDO();
        covenantDO.setSubject(contractParamDTO.getSubject());
        covenantDO.setContractNo(documentParamDTO.getContractNo());
        covenantDO.setQysContractId(contractId);
        covenantDO.setQysCategoryId(contractParamDTO.getQysCategoryId());
        if (!StringUtils.isEmpty(contractParamDTO.getExpireTime())) {
            covenantDO.setExpireTime(contractParamDTO.getExpireTime());
        }
        covenantDO.setEndTime(contractParamDTO.getEndTime());
        covenantDO.setInitiatorName(contractParamDTO.getInitiatorName());
        covenantDO.setInitiatorOperator(contractParamDTO.getInitiatorOperator());
        covenantDO.setInitiatorContact(contractParamDTO.getInitiatorContact());
        covenantDO.setReceiverName(contractParamDTO.getReceiverName());
        covenantDO.setReceiverOperator(contractParamDTO.getReceiverOperator());
        covenantDO.setReceiverContact(contractParamDTO.getReceiverContact());
        covenantDO.setStatus(ContractStatusEnum.DRAFT.name());
        covenantService.save(covenantDO);

        return contractId;
    }

    @Override
    public String viewUrl(Long contractId) {
        ViewUrlRequest request = new ViewUrlRequest();
        request.setContractId(contractId);
        request.setPageType(ViewUrlRequest.PageType.CONTENT);
        String viewUrl = null;
        try {
            viewUrl = contractService.viewUrl(request);
        } catch (PrivateAppException e) {
            throw new ServiceException(ResultCode.FAILED, "获取合同失败");
        }

        // 获取ip
        String prefix = "";
        if (viewUrl.startsWith("https://")) {
            prefix = "https://";
        }
        if (viewUrl.startsWith("http://")) {
            prefix = "http://";
        }
        String suffix = viewUrl.substring(prefix.length());
        String ipPort = suffix.substring(0, suffix.indexOf("/"));

        //  替换
        viewUrl = viewUrl.replace(ipPort, qysHost + ":" + qysPort);

        return viewUrl;
    }

    @Override
    public void sendContract(Long contractId) {
        // 只能发起草稿状态的合同
        CovenantDO covenantDO = covenantService.getByQysContractId(contractId);
        if (covenantDO == null || !ContractStatusEnum.DRAFT.name().equals(covenantDO.getStatus())) {
            throw new BusinessException(ResultCode.FAILED, "合同id：" + contractId + "发起失败，合同不存在或未处于草稿状态");
        }

        SendContractRequest request = new SendContractRequest();
        request.setContractId(contractId);
        try {
            contractService.send(request);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILED, "合同id：" + contractId + "发起失败，sdk调用失败");
        }
        // 修改合同状态 -> 签署中
        covenantService.updateStatus(covenantDO.getId(), ContractStatusEnum.SIGNING.name());

    }

    @Override
    public void recallContract(Long contractId, String reason) {
        // 只有签署中的合同可以撤回
        CovenantDO covenantDO = covenantService.getByQysContractId(contractId);
        if (covenantDO == null || !ContractStatusEnum.SIGNING.name().equals(covenantDO.getStatus())) {
            throw new BusinessException(ResultCode.FAILED, "合同id：" + contractId + "撤回失败，合同不存在或未处于签署中状态");
        }
        try {
            contractService.recallContract(contractId, reason);
        } catch (PrivateAppException e) {
            throw new ServiceException(ResultCode.FAILED, "合同id：" + contractId + "发起合同失败");
        }
        //  修改合同状态 -> 已撤回2
        covenantService.updateStatus(covenantDO.getId(), ContractStatusEnum.RECALLED.name());
    }

    @Override
    public void cancelContract(Long contractId, Long sealId, String reason, Boolean removeContract) {
        // 调用此接口对已完成的合同发起作废，生成作废文件，并代合同发起方签署作废文件
        CovenantDO covenantDO = covenantService.getByQysContractId(contractId);
        if (covenantDO == null || !ContractStatusEnum.SIGNING.name().equals(covenantDO.getStatus())) {
            throw new BusinessException(ResultCode.FAILED, "作废合同失败，合同不存在或未处于签署中状态");
        }
        try {
            contractService.cancelContract(contractId, sealId, reason, removeContract);
        } catch (PrivateAppException e) {
            throw new RuntimeException(e);
        }
        //  修改合同状态 -> 作废中
        covenantService.updateStatus(covenantDO.getId(), ContractStatusEnum.TERMINATING.name());
    }

    @Override
    public void deleteContract(Long contractId) {
        try {
            contractService.delete(contractId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String downloadContract(Long contractId) {
        //  查看合同状态
        CovenantDO covenantDO = covenantService.getByQysContractId(contractId);
        if (covenantDO == null) {
            throw new BusinessException(ResultCode.FAILED, "合同数据不存在");
        }
        if (!ContractStatusEnum.COMPLETE.name().equals(covenantDO.getStatus())
                && !ContractStatusEnum.FINISHED.name().equals(covenantDO.getStatus())
                && !ContractStatusEnum.TERMINATING.name().equals(covenantDO.getStatus())
                && !ContractStatusEnum.TERMINATED.name().equals(covenantDO.getStatus())) {
            // 只有已完成、作废中、作废完成的合同可以下载
            throw new BusinessException(ResultCode.FAILED, "当前合同状态：" + covenantDO.getStatus() + "，不支持下载");
        }
        if (!StringUtils.isEmpty(covenantDO.getFileKey())) {
            return fileService.getUrl(covenantDO.getFileKey(), FileTypeEnum.FILE_EXPORT_CENTER);
        }

        FileInfo fileInfo = downloadAndUploadToOss(contractId);
        String fileKey = fileInfo.getUrl();
        covenantService.updateFileKey(covenantDO.getId(), fileKey);
        return fileInfo.getUrl();
    }

    @Transactional
    @Override
    public void completeCallBack(CallBackRequest callBackRequest) {
        Long contractId = callBackRequest.getContractId();
        String status = callBackRequest.getStatus();
        CovenantDO covenantDO = covenantService.getByQysContractId(contractId);
        if (covenantDO == null) {
            // 只有已完成、作废中、作废完成的合同可以下载
            throw new BusinessException(ResultCode.FAILED, "合同id：" + contractId + "不存在");
        }
        covenantService.updateStatus(covenantDO.getId(), status);

        if (ContractStatusEnum.COMPLETE.name().equals(status) || ContractStatusEnum.FINISHED.name().equals(status)) {
            FileInfo fileInfo = downloadAndUploadToOss(contractId);
            String fileKey = fileInfo.getKey();
            covenantService.updateFileKey(covenantDO.getId(), fileKey);
        }
    }

    @Override
    public String getContractStatus(Long contractId) {
        CovenantDO covenantDO = covenantService.getByQysContractId(contractId);
        if (covenantDO == null) {
            // 合同不存在
            throw new BusinessException(ResultCode.FAILED, "合同id：" + contractId + "不存在");
        }
        return covenantDO.getStatus();
    }

    // 根据模板创建合同文档
    private Long createDocumentByTemplate(ContractParamDTO contractParamDTO, DocumentParamDTO documentParamDTO) {
        Long templateId = contractParamDTO.getQysTemplateId();
        String subject = contractParamDTO.getSubject();

        if (StringUtils.isEmpty(contractParamDTO.getSn())) {
            //  生成合同编号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String sn = "YL-" + sdf.format(new Date());
            contractParamDTO.setSn(sn);
            documentParamDTO.setContractNo(sn);
        }

        // 生成模板文档参数
        Map<String, String> paramMap = null;
        try {
            paramMap = generateDocParams(documentParamDTO);
        } catch (Exception e) {
            log.error("模板参数生成失败:", e);
            //  模板参数生成失败
            throw new ServiceException(ResultCode.FAILED, "模板参数生成失败");
        }

        // 合同文档id
        Long documentId = null;
        try {
            documentId = contractService.createDocument(templateId, paramMap, subject, null);
        } catch (Exception e) {
            log.error("创建合同文档失败:", e);
            throw new ServiceException(ResultCode.FAILED, "创建合同文档失败");
        }
        return documentId;
    }

    // 创建合同
    private Long createContractByDocument(Long documentId, ContractParamDTO contractParamDTO) {
        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setSn(contractParamDTO.getSn());
        createContractRequest.setCategoryId(contractParamDTO.getQysCategoryId());
        if (!StringUtils.isEmpty(contractParamDTO.getExpireTime())) {
            contractParamDTO.setExpireTime(contractParamDTO.getExpireTime().replace("/", "-") + " 23:59:59");
        }
        if (!StringUtils.isEmpty(contractParamDTO.getEndTime())) {
            contractParamDTO.setEndTime(contractParamDTO.getEndTime().replace("/", "-") + " 23:59:59");
        }
        createContractRequest.setExpireTime(contractParamDTO.getExpireTime());  // 过期时间
        createContractRequest.setEndTime(contractParamDTO.getEndTime());    // 合同终止时间
        createContractRequest.setSend(false);   // 不发送
        createContractRequest.setMustSign(true);
        createContractRequest.setExtraSign(false);
        createContractRequest.setTenantName(contractParamDTO.getTenantName());
        createContractRequest.setSubject(contractParamDTO.getSubject());

        // 接收方
        Signatory signatory1 = new Signatory();
        signatory1.setContact(contractParamDTO.getReceiverContact());
        signatory1.setTenantType(TenantType.COMPANY);
        signatory1.setTenantName(contractParamDTO.getReceiverName());
        signatory1.setSerialNo(1);
        // 签署动作
        createContractRequest.addSignatory(signatory1);

        // 发起方
        Signatory signatory2 = new Signatory();
        signatory2.setContact(contractParamDTO.getInitiatorContact());
        signatory2.setTenantType(TenantType.COMPANY);
        signatory2.setTenantName(contractParamDTO.getInitiatorName());
        signatory2.setSerialNo(2);
        createContractRequest.addSignatory(signatory2);

        // 添加文档
        List<Long> documents = new ArrayList<>();
        documents.add(documentId);
        createContractRequest.setDocuments(documents);

        Long contractId = null;
        try {
            contractId = contractService.createContractByCategory(createContractRequest);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILED, "创建合同失败");
        }
        return contractId;
    }


    private Map<String, String> generateDocParams(DocumentParamDTO documentParamDTO) throws Exception {

        Map<String, String> paramMap = new HashMap<>();
        Class<DocumentParamDTO> clazz = DocumentParamDTO.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            Method method = clazz.getDeclaredMethod("get" + upperCaseFirst(field.getName()));
            Object value = method.invoke(documentParamDTO);

            if (value instanceof String) {
                paramMap.put(field.getName(), String.valueOf(value));
            } else {
                paramMap.put(field.getName(), JSONObject.toJSONString(value));
            }
        }
        return paramMap;
    }

    private FileInfo downloadAndUploadToOss(Long contractId) {
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".zip";
        File tmpDir = FileUtil.getTmpDir();
        File dir = FileUtil.newFile(tmpDir.getPath() + File.separator + "contract" + File.separator + contractId);
        File file = FileUtil.newFile(dir.getPath() + File.separator + fileName);
        FileUtil.touch(file);
        // 从签章平台上下载合同
        OutputStream outputStream = null;
        try {
            outputStream = Files.newOutputStream(file.toPath());
            contractService.download(contractId, outputStream);
            IOUtils.safeClose(outputStream);
        }  catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.FAILED, "合同id：" + contractId + "，下载失败！");
        }
        // 将下载完成的合同上传至oss
        FileInfo fileInfo = null;
        try {
            fileInfo = fileService.upload(file, FileTypeEnum.QIYUESUO_CONTRACT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.FAILED, "合同id：" + contractId + "，上传至oss失败！");
        }
        return fileInfo;
    }


    private String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
