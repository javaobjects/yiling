package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockThirdRecordApi;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.UnlockThirdRecordExcelModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author fucheng.bai
 * @date 2023/5/18
 */
@Service("unlockThirdRecordExportService")
public class UnlockThirdRecordExportServiceImpl implements BaseExportQueryDataService<QueryUnlockThirdRecordPageRequest> {

    @DubboReference
    private UnlockThirdRecordApi unlockThirdRecordApi;

    @DubboReference
    private UserApi userApi;

    @Override
    public QueryExportDataDTO queryData(QueryUnlockThirdRecordPageRequest queryUnlockThirdRecordPageRequest) {
        return null;
    }

    @Override
    public QueryUnlockThirdRecordPageRequest getParam(Map<String, Object> map) {
        // TODO DIH-239 数据权限暂未确定
        return PojoUtils.map(map, QueryUnlockThirdRecordPageRequest.class);
    }

    @Override
    public String getFileSuffix() {
        return "xlsx";
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(QueryUnlockThirdRecordPageRequest request, String fileName) {
        if (request == null) {
            return null;
        }

        String tmpDir = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
        String excelFileName = tmpDir + File.separator + fileName;
        File dirFile = FileUtil.newFile(tmpDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        ExcelWriter excelWriter = EasyExcel.write(excelFileName, UnlockThirdRecordExcelModel.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

        int current = 1;
        int size = 2000;

        try {
            Map<Long, String> userNameMap = new HashMap<>();

            while (true) {
                request.setCurrent(current);
                request.setSize(size);
                Page<UnlockThirdRecordDTO> pageResult = unlockThirdRecordApi.listPage(request);

                List<UnlockThirdRecordExcelModel> list = new ArrayList<>();
                for (UnlockThirdRecordDTO unlockThirdRecordDTO : pageResult.getRecords()) {
                    UnlockThirdRecordExcelModel model = PojoUtils.map(unlockThirdRecordDTO, UnlockThirdRecordExcelModel.class);
                    // 业务部门转换
                    List<Map> mapList =  JSONUtil.toList(JSONUtil.parseArray(model.getEffectiveDepartment()), Map.class);
                    List<String> nameList = mapList.stream().map(map -> String.valueOf(map.get("name"))).collect(Collectors.toList());
                    String effectiveDepartmentDesc = StringUtils.join(nameList, "、");
                    model.setEffectiveDepartmentDesc(effectiveDepartmentDesc);

                    //  获取操作人姓名
                    if (model.getLastOpUser() != null) {
                        if (!userNameMap.containsKey(model.getLastOpUser())) {
                            UserDTO userDTO = userApi.getById(model.getLastOpUser());
                            userNameMap.put(model.getLastOpUser(), userDTO.getName());
                        }
                        model.setLastOpUserName(userNameMap.get(model.getLastOpUser()));
                    }

                    list.add(model);
                }

                if (CollUtil.isEmpty(list)) {
                    break;
                }

                // 写文件
                excelWriter.write(list, writeSheet);
                if (list.size() < size) {
                    break;
                }
                current++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        try {
            File file = new File(excelFileName);
            if (file.exists()) {
                return  FileUtil.readBytes(file);
            }
        } finally {
            FileUtil.del(tmpDir);
        }
        return null;
    }


}
