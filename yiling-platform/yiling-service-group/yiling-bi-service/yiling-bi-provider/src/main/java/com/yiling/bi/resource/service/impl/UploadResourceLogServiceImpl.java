package com.yiling.bi.resource.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.setting.SettingUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.bi.company.entity.DimLsflChaincompanyDO;
import com.yiling.bi.company.service.DimLsflChaincompanyService;
import com.yiling.bi.resource.dao.UploadResourceLogMapper;
import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.dto.UploadResourceLogDTO;
import com.yiling.bi.resource.dto.request.UpdateResourceLogRequest;
import com.yiling.bi.resource.entity.InputLsflCoverDO;
import com.yiling.bi.resource.entity.InputLsflRecordDO;
import com.yiling.bi.resource.entity.UploadResourceDO;
import com.yiling.bi.resource.entity.UploadResourceLogDO;
import com.yiling.bi.resource.excel.InputLsflCoverExcel;
import com.yiling.bi.resource.excel.InputLsflRecordExcel;
import com.yiling.bi.resource.handler.UploadCoverResourceReaderHandler;
import com.yiling.bi.resource.handler.UploadRecordResourceReaderHandler;
import com.yiling.bi.resource.service.InputLsflCoverService;
import com.yiling.bi.resource.service.InputLsflRecordService;
import com.yiling.bi.resource.service.UploadResourceLogService;
import com.yiling.bi.resource.service.UploadResourceService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
@Slf4j
@Service
public class UploadResourceLogServiceImpl extends BaseServiceImpl<UploadResourceLogMapper, UploadResourceLogDO> implements UploadResourceLogService {

    @Autowired
    private UploadResourceService      uploadResourceService;
    @Autowired
    private InputLsflRecordService     inputLsflRecordService;
    @Autowired
    private InputLsflCoverService      inputLsflCoverService;
    @Autowired
    private DimLsflChaincompanyService dimLsflChaincompanyService;

    @Lazy
    @Autowired
    private UploadResourceLogService uploadResourceLogService;


    @Override
    public boolean updateUploadResourceLog(UpdateResourceLogRequest request) {
        return this.updateById(PojoUtils.map(request,UploadResourceLogDO.class));
    }

    @Override
    @Async("asyncExecutor")
    public String importInputLsflRecord(String id) {
        QueryWrapper<UploadResourceLogDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UploadResourceLogDO::getId, id);
        UploadResourceLogDO uploadResourceLogDO = this.getOne(queryWrapper);
        if (uploadResourceLogDO == null) {
            return "数据ID不存在";
        }
        if (!uploadResourceLogDO.getStatus().equals("0")) {
            return "数据已经处理";
        }
        UploadResourceDTO uploadResourceDO = uploadResourceService.getUploadResourceByDataId(uploadResourceLogDO.getId());
        if (uploadResourceLogDO.getDataSource().equals("record")) {
            handleInputLsflRecord(uploadResourceDO, uploadResourceLogDO);
        } else if (uploadResourceLogDO.getDataSource().equals("cover")) {
            handleInputLsflCover(uploadResourceDO, uploadResourceLogDO);
        }
        return "处理成功";
    }

    @Override
    public UploadResourceLogDTO getUploadResourceLogById(String id) {
        QueryWrapper<UploadResourceLogDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UploadResourceLogDO::getId, id);
        UploadResourceLogDO uploadResourceLogDO = this.getOne(queryWrapper);
        return PojoUtils.map(uploadResourceLogDO,UploadResourceLogDTO.class);
    }

    @Override
    public boolean handleInputLsflRecord(UploadResourceDTO uploadResourceDO, UploadResourceLogDO uploadResourceLogDO) {
        uploadResourceLogDO.setStatus("3");
        if (this.updateById(uploadResourceLogDO)) {
            Set<String> stringSet=new HashSet<>();
            InputStream input = new ByteArrayInputStream(uploadResourceDO.getFileStream());
            ImportParams params = new ImportParams();
            params.setNeedVerify(true);
            params.setHeadRows(1);
            List<InputLsflRecordExcel> list = new ArrayList<>();
            UploadRecordResourceReaderHandler handler = new UploadRecordResourceReaderHandler(list);
            try {
                ExcelUtil.readBySax(input, 0, handler);
                List<InputLsflRecordDO> inputLsflRecordDOList = PojoUtils.map(list, InputLsflRecordDO.class);
                Map<String, InputLsflRecordDO> inputLsflRecordDOMap = new HashMap<>();
                for (InputLsflRecordDO inputLsflRecordDO : inputLsflRecordDOList) {
                    inputLsflRecordDOMap.put(inputLsflRecordDO.getBzCode() + "-" + inputLsflRecordDO.getWlCode() + "" + inputLsflRecordDO.getDyear(), inputLsflRecordDO);
                }
                inputLsflRecordDOList = new ArrayList<>(inputLsflRecordDOMap.values());
                List<Future<Set<String>>> futures = new ArrayList<>();
                List<List<InputLsflRecordDO>> successLists = ListUtils.partition(inputLsflRecordDOList, 2000);
                successLists.forEach(successList -> {
                    //异步执行execute
                    Future<Set<String>> future = uploadResourceLogService.executeRecordDataByThread(successList, uploadResourceLogDO);
                    futures.add(future);
                });

                for (Future<Set<String>> future : futures) {
                    stringSet.addAll(future.get());
                }
                uploadResourceService.deleteUploadResourceById(uploadResourceDO.getId());
                uploadResourceLogDO.setStatus("4");
                if(stringSet.size()<=0) {
                    uploadResourceLogDO.setMsg("同步成功");
                }else{
                    StringBuffer sb=new StringBuffer("");
                    for(String string:stringSet){
                        sb.append(string).append(";");
                    }
                    sb.append("不在导入范围内");
                    uploadResourceLogDO.setMsg(sb.toString());
                }
                return this.updateById(uploadResourceLogDO);
            } catch (Exception e) {
                log.error("解析文件失败", e);
                uploadResourceLogDO.setMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
                uploadResourceLogDO.setStatus("5");
            }
        }
        return this.updateById(uploadResourceLogDO);
    }

    @Override
    public boolean handleInputLsflCover(UploadResourceDTO uploadResourceDO, UploadResourceLogDO uploadResourceLogDO) {
        uploadResourceLogDO.setStatus("3");
        if (this.updateById(uploadResourceLogDO)) {
            Set<String> stringSet=new HashSet<>();
            InputStream input = new ByteArrayInputStream(uploadResourceDO.getFileStream());
            ImportParams params = new ImportParams();
            params.setNeedVerify(true);
            params.setHeadRows(1);
            List<InputLsflCoverExcel> list = new ArrayList<>();
            UploadCoverResourceReaderHandler handler = new UploadCoverResourceReaderHandler(list);
            try {
                ExcelUtil.readBySax(input, 0, handler);
                List<InputLsflCoverDO> inputLsflCoverDOList = PojoUtils.map(list, InputLsflCoverDO.class);
                Map<String, InputLsflCoverDO> inputLsflCoverDOMap = new HashMap<>();
                for (InputLsflCoverDO inputLsflCoverDO : inputLsflCoverDOList) {
                    inputLsflCoverDOMap.put(inputLsflCoverDO.getBzCode() + "-" + inputLsflCoverDO.getWlBreed() + "" + inputLsflCoverDO.getDyear(), inputLsflCoverDO);
                }
                inputLsflCoverDOList = new ArrayList<>(inputLsflCoverDOMap.values());
                List<Future<Set<String>>> futures = new ArrayList<>();
                List<List<InputLsflCoverDO>> successLists = ListUtils.partition(inputLsflCoverDOList, 2000);
                successLists.forEach(successList -> {
                    //异步执行execute
                    Future<Set<String>> future = uploadResourceLogService.executeCoverDataByThread(successList, uploadResourceLogDO);
                    futures.add(future);
                });

                for (Future<Set<String>> future : futures) {
                    stringSet.addAll(future.get());
                }
                uploadResourceService.deleteUploadResourceById(uploadResourceDO.getId());
                uploadResourceLogDO.setStatus("4");
                if(stringSet.size()<=0) {
                    uploadResourceLogDO.setMsg("同步成功");
                }else{
                    StringBuffer sb=new StringBuffer("");
                    for(String string:stringSet){
                        sb.append(string).append(";");
                    }
                    sb.append("不在导入范围内");
                    uploadResourceLogDO.setMsg(sb.toString());
                }
                return this.updateById(uploadResourceLogDO);
            } catch (Exception e) {
                log.error("解析文件失败", e);
                uploadResourceLogDO.setMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
                uploadResourceLogDO.setStatus("5");
            }
        }
        return this.updateById(uploadResourceLogDO);
    }

    @Override
    @Async("excelExecutor")
    public <T> Future<Set<String>> executeRecordDataByThread(List<InputLsflRecordDO> successList, UploadResourceLogDO uploadResourceLogDO) {
        long start = System.currentTimeMillis();
        //由于executeData方法单条失败数据存在remove操作，多线程执行保证list操作独立性使用CopyOnWriteArrayList
        Set<String> bool = insertRecord(successList, uploadResourceLogDO);
        log.info("{}异步执行完成，时间为：{}", Thread.currentThread().getName(), System.currentTimeMillis() - start);
        return new AsyncResult<>(bool);
    }

    @Override
    @Async("excelExecutor")
    public <T> Future<Set<String>> executeCoverDataByThread(List<InputLsflCoverDO> successList, UploadResourceLogDO uploadResourceLogDO) {
        long start = System.currentTimeMillis();
        //由于executeData方法单条失败数据存在remove操作，多线程执行保证list操作独立性使用CopyOnWriteArrayList
        Set<String> bool = insertCover(successList, uploadResourceLogDO);
        log.info("{}异步执行完成，时间为：{}", Thread.currentThread().getName(), System.currentTimeMillis() - start);
        return new AsyncResult<>(bool);
    }

    public Set<String> insertRecord(List<InputLsflRecordDO> inputLsflRecordDOList, UploadResourceLogDO uploadResourceLogDO) {
        Set<String> stringSet = new HashSet<>();
        List<InputLsflRecordDO> insertList = new ArrayList<>();
        for (InputLsflRecordDO inputLsflRecordDO : inputLsflRecordDOList) {
            QueryWrapper<DimLsflChaincompanyDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DimLsflChaincompanyDO::getDbCode, uploadResourceLogDO.getCreateId());
            queryWrapper.lambda().ne(DimLsflChaincompanyDO::getDelLevel, 1);
            List<DimLsflChaincompanyDO> dimLsflChaincompanyDOList = dimLsflChaincompanyService.list(queryWrapper);
            List<String> chainCodeList = dimLsflChaincompanyDOList.stream().map(e -> e.getChainCode()).collect(Collectors.toList());
            if (chainCodeList.contains(inputLsflRecordDO.getBzCode())) {
                inputLsflRecordDO.setDyear(uploadResourceLogDO.getDyear());
                inputLsflRecordDO.setDataName(uploadResourceLogDO.getCreateId());
                inputLsflRecordDO.setXyType(uploadResourceLogDO.getXyType());
                inputLsflRecordDO.setQdType(uploadResourceLogDO.getQdType());
                InputLsflRecordDO inputLsflRecordOld = inputLsflRecordService.getByNameAndGoodsAndYear(inputLsflRecordDO.getBzCode(), inputLsflRecordDO.getWlCode(), inputLsflRecordDO.getDyear());
                if (inputLsflRecordOld != null) {
                    inputLsflRecordDO.setId(inputLsflRecordOld.getId());
                }
                insertList.add(inputLsflRecordDO);
            } else {
                stringSet.add(inputLsflRecordDO.getBzName());
            }
        }
        inputLsflRecordService.saveOrUpdateBatch(insertList, 2000);
        return stringSet;
    }

    public Set<String> insertCover(List<InputLsflCoverDO> inputLsflCoverDOList, UploadResourceLogDO uploadResourceLogDO) {
        List<InputLsflCoverDO> insertList = new ArrayList<>();
        Set<String> stringSet = new HashSet<>();
        for (InputLsflCoverDO inputLsflCoverDO : inputLsflCoverDOList) {
            QueryWrapper<DimLsflChaincompanyDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DimLsflChaincompanyDO::getDbCode, uploadResourceLogDO.getCreateId());
            queryWrapper.lambda().ne(DimLsflChaincompanyDO::getDelLevel, 1);
            List<DimLsflChaincompanyDO> dimLsflChaincompanyDOList = dimLsflChaincompanyService.list(queryWrapper);
            List<String> chainCodeList = dimLsflChaincompanyDOList.stream().map(e -> e.getChainCode()).collect(Collectors.toList());
            if (chainCodeList.contains(inputLsflCoverDO.getBzCode())) {
                inputLsflCoverDO.setDyear(uploadResourceLogDO.getDyear());
                inputLsflCoverDO.setDataName(uploadResourceLogDO.getCreateId());
                inputLsflCoverDO.setXyType(uploadResourceLogDO.getXyType());
                inputLsflCoverDO.setQdType(uploadResourceLogDO.getQdType());
                InputLsflCoverDO inputLsflCoverOld = inputLsflCoverService.getByNameAndGoodsAndYear(inputLsflCoverDO.getBzCode(), inputLsflCoverDO.getWlBreed(), inputLsflCoverDO.getDyear());
                if (inputLsflCoverOld != null) {
                    inputLsflCoverDO.setId(inputLsflCoverOld.getId());
                }
                insertList.add(inputLsflCoverDO);
            }else{
                stringSet.add(inputLsflCoverDO.getBzName());
            }
        }
         inputLsflCoverService.saveOrUpdateBatch(insertList, 2000);
        return stringSet;
    }

    /**
     * byte[] 转文件
     *
     * @param data
     * @param file
     */
    public static void genFile(byte[] data, File file) {
        if (!file.exists()) {
            FileUtil.newFile(file.getPath());
        }
        try (FileOutputStream fio = new FileOutputStream(file)) {
            fio.write(data, 0, data.length);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
