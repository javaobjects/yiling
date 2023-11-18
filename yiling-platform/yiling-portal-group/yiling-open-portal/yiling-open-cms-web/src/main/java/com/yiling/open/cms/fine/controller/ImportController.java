package com.yiling.open.cms.fine.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskRecordDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.open.cms.fine.form.ImportRecordForm;
import com.yiling.open.cms.fine.vo.ImportRecordInfoVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@RestController
@Slf4j
@RequestMapping("/import")
public class ImportController {

    @DubboReference
    private  ExcelTaskRecordApi excelTaskRecordApi;

    @Autowired
    private FileService fileService;


    @PostMapping("/recordInfo")
    public Result getImportRecordById(@RequestBody ImportRecordForm form) {
        Long recordId = form.getRecordId();

        ExcelTaskRecordDTO excelTaskRecordDTO = excelTaskRecordApi.findExcelTaskRecordById(recordId);
        if (excelTaskRecordDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "无该条导入信息记录");
        }
        ImportRecordInfoVO importRecordInfoVO = new ImportRecordInfoVO();
        importRecordInfoVO.setId(excelTaskRecordDTO.getId());

        if (excelTaskRecordDTO.getStatus() == 2) {
            importRecordInfoVO.setResultUrl(fileService.getUrl(excelTaskRecordDTO.getResultUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT));
        }
        importRecordInfoVO.setSuccessNumber(excelTaskRecordDTO.getSuccessNumber());
        importRecordInfoVO.setFailNumber(excelTaskRecordDTO.getFailNumber());
        // 上传完成若有失败数，则返回上传失败，否则返回成功
        if (excelTaskRecordDTO.getStatus() == 2 && excelTaskRecordDTO.getFailNumber() > 0) {
            importRecordInfoVO.setStatus(3);
        } else {
            importRecordInfoVO.setStatus(excelTaskRecordDTO.getStatus());
        }
        importRecordInfoVO.setRemark(excelTaskRecordDTO.getRemark());

        return Result.success(importRecordInfoVO);
    }

}
