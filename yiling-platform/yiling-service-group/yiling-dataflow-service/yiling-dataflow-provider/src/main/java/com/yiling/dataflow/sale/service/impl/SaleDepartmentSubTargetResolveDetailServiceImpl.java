package com.yiling.dataflow.sale.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetResolveDetailBO;
import com.yiling.dataflow.sale.dao.SaleDepartmentSubTargetResolveDetailMapper;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetResolveDetailDTO;
import com.yiling.dataflow.sale.dto.request.ImportSubTargetResolveDetailRequest;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetResolveDetailDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentTargetDO;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.enums.ResolveStatusEnum;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetResolveDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 部门销售指标子项配置分解详情 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Service
@Slf4j
public class SaleDepartmentSubTargetResolveDetailServiceImpl extends BaseServiceImpl<SaleDepartmentSubTargetResolveDetailMapper, SaleDepartmentSubTargetResolveDetailDO> implements SaleDepartmentSubTargetResolveDetailService {
    @Autowired
    private SaleTargetService saleTargetService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SaleDepartmentTargetService saleDepartmentTargetService;


    @Override
    public List<SaleDepartmentSubTargetResolveDetailDTO> queryListByTargetId(Long targetId,Long deptId) {
        if (ObjectUtil.isNull(targetId) || ObjectUtil.equal(targetId, 0L)) {
            return ListUtil.toList();
        }
        if (ObjectUtil.isNull(deptId) || ObjectUtil.equal(deptId, 0L)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<SaleDepartmentSubTargetResolveDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getSaleTargetId, targetId);
        wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartId, deptId);
        List<SaleDepartmentSubTargetResolveDetailDO> list = list(wrapper);
        return PojoUtils.map(list, SaleDepartmentSubTargetResolveDetailDTO.class);
    }

    @Override
    public Page<SaleDepartmentSubTargetResolveDetailBO> queryPage(QueryResolveDetailPageRequest request) {
        LambdaQueryWrapper<SaleDepartmentSubTargetResolveDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getSaleTargetId, request.getSaleTargetId());
        wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartId, request.getDepartId());
        if(Objects.nonNull(request.getDepartProvinceId()) && request.getDepartProvinceId()>0){
           wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartProvinceId,request.getDepartProvinceId());
        }
        if(Objects.nonNull(request.getDepartRegionId()) && request.getDepartRegionId()>0){
            wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartRegionId,request.getDepartRegionId());
        }
        if(Objects.nonNull(request.getCategoryId()) && request.getCategoryId()>0){
            wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getCategoryId,request.getCategoryId());
        }
        if(StrUtil.isNotEmpty(request.getCategoryName())){
            wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getCategoryName,request.getCategoryName());
        }
        if(StrUtil.isNotEmpty(request.getDepartRegionName())){
            wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartRegionName,request.getDepartRegionName());
        }
        if(StrUtil.isNotEmpty(request.getDepartProvinceName())){
            wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartProvinceName,request.getDepartProvinceName());
        }
        if(Objects.nonNull(request.getResolveStatus()) && request.getResolveStatus()>0){
            wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getResolveStatus,request.getResolveStatus());
        }
        Page<SaleDepartmentSubTargetResolveDetailDO> page = this.page(request.getPage(), wrapper);
        if(page.getTotal()==0){
            return request.getPage();
        }
        SaleTargetDO saleTargetDO = saleTargetService.getById(request.getSaleTargetId());
        Page<SaleDepartmentSubTargetResolveDetailBO> resolveDetailBOPage = PojoUtils.map(page,SaleDepartmentSubTargetResolveDetailBO.class);
        resolveDetailBOPage.getRecords().forEach(saleDepartmentSubTargetResolveDetailBO -> {
            saleDepartmentSubTargetResolveDetailBO.setTargetNo(saleTargetDO.getTargetNo()).setTargetYear(saleTargetDO.getTargetYear()).setName(saleTargetDO.getName());
        });
        return resolveDetailBOPage;
    }



    @Override
    @Transactional
    public Boolean importSubTargetResolveDetail(List<ImportSubTargetResolveDetailRequest> request) {
        log.debug("ImportSubTargetResolveDetailRequest={}",request.toString());
        SaleDepartmentSubTargetResolveDetailDO resolveDetailDO = this.getById(request.get(0).getId());
        LambdaQueryWrapper<SaleDepartmentTargetDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentTargetDO::getSaleTargetId, resolveDetailDO.getSaleTargetId());
        wrapper.eq( SaleDepartmentTargetDO::getDepartId, resolveDetailDO.getDepartId());
        SaleDepartmentTargetDO saleDepartmentTargetDO = saleDepartmentTargetService.getOne(wrapper );
        List<SaleDepartmentSubTargetResolveDetailDO> list = PojoUtils.map(request,SaleDepartmentSubTargetResolveDetailDO.class);
        //AtomicInteger resovledCount = new AtomicInteger(saleDepartmentTargetDO.getResolved());
        list.forEach(target->{
            if(null==target.getTargetApr() || null==target.getTargetAug() ||null==target.getTargetDec() ||null==target.getTargetFeb() ||null==target.getTargetJan() ||
                    null==target.getTargetJul() ||null==target.getTargetJun() ||null==target.getTargetMar() ||null==target.getTargetMay() ||null==target.getTargetNov() ||
                    null==target.getTargetOct() ||null==target.getTargetSep() ||null==target.getTargetApr()){
                target.setResolveStatus(ResolveStatusEnum.TO_RESOVLE.getCode());
            }else{
                target.setResolveStatus(ResolveStatusEnum.RESOVLED.getCode());
                //resovledCount.getAndIncrement();
            }

        });

        boolean b = this.updateBatchById(list);
       /* saleDepartmentTargetDO.setResolveTime(new Date());
        saleDepartmentTargetService.updateById(saleDepartmentTargetDO);*/
        this.validTarget(resolveDetailDO.getSaleTargetId(),resolveDetailDO.getDepartId(),saleDepartmentTargetDO);
        return  b;
    }

    private void validTarget(Long targetId,Long deptId,SaleDepartmentTargetDO saleDepartmentTargetDO){


       LambdaQueryWrapper<SaleDepartmentSubTargetResolveDetailDO> wrapper = Wrappers.lambdaQuery();
       wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getSaleTargetId,targetId).eq(SaleDepartmentSubTargetResolveDetailDO::getDepartId,deptId).eq(SaleDepartmentSubTargetResolveDetailDO::getResolveStatus,ResolveStatusEnum.RESOVLED.getCode());
        List<SaleDepartmentSubTargetResolveDetailDO> resolveDetailDOS = this.list(wrapper);
        saleDepartmentTargetDO.setResolved(resolveDetailDOS.size()).setResolveTime(new Date());
       // saleDepartmentTargetService.updateById(saleDepartmentTargetDO);

        //校验明细和部门总目标是否一致
        if(saleDepartmentTargetDO.getResolved().equals(saleDepartmentTargetDO.getGoal())){
            BigDecimal total = BigDecimal.ZERO;
            for (SaleDepartmentSubTargetResolveDetailDO resolveDetailDO : resolveDetailDOS) {
                total = total.add(resolveDetailDO.getTargetApr()).add(resolveDetailDO.getTargetAug()).add(resolveDetailDO.getTargetDec()).add(resolveDetailDO.getTargetFeb()).add(resolveDetailDO.getTargetJan()).add(resolveDetailDO.getTargetJul()).add(resolveDetailDO.getTargetJun()).add(resolveDetailDO.getTargetMar()).add(resolveDetailDO.getTargetMay()).add(resolveDetailDO.getTargetNov()).add(resolveDetailDO.getTargetOct()).add(resolveDetailDO.getTargetSep());
            }
            saleDepartmentTargetDO.setRealTarget(total);
        }
        saleDepartmentTargetService.updateById(saleDepartmentTargetDO);
    }

    /*@Override
    @SneakyThrows
    public String genTemplate(QueryResolveDetailPageRequest request) {
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        exportParams.setCreateHeadRows(true);
        SaleTargetDO targetDO = saleTargetService.getById(request.getSaleTargetId());
        Workbook workbook = ExcelExportUtil.exportBigExcel(exportParams, ExportSubTargetResolveDetailBO.class,new IExcelExportServer(){
            @Override
            public List<Object> selectListForExcelExport(Object queryParams, int page) {
                QueryResolveDetailPageRequest req = (QueryResolveDetailPageRequest) queryParams;
                req.setCurrent(page);
                req.setSize(100);
                Page<SaleDepartmentSubTargetResolveDetailBO> resolveDetailBOPage = SpringUtils.getBean(SaleDepartmentSubTargetResolveDetailService.class).queryPage(req);
                if (resolveDetailBOPage == null || CollectionUtil.isEmpty(resolveDetailBOPage.getRecords())) {
                    return Collections.emptyList();
                }
                List collect = resolveDetailBOPage.getRecords().stream().map(r -> {
                    ExportSubTargetResolveDetailBO subTargetResolveDetailBO = PojoUtils.map(r, ExportSubTargetResolveDetailBO.class);
                    subTargetResolveDetailBO.setTargetApr(null).setTargetAug(null).setTargetDec(null).setTargetFeb(null).setTargetJan(null).setTargetJul(null).setTargetJun(null)
                            .setTargetMar(null).setTargetMay(null).setTargetNo(null).setTargetNov(null).setTargetOct(null).setTargetSep(null);
                    // 状态和供应链角色字段处理
                    if(r.getSupplyChainRole()>0){
                        subTargetResolveDetailBO.setSupplyChainRoleStr(AgencySupplyChainRoleEnum.getByCode(r.getSupplyChainRole()).getName());
                    }
                    subTargetResolveDetailBO.setTargetNo(targetDO.getTargetNo());
                    return subTargetResolveDetailBO;
                }).collect(Collectors.toList());
                return collect;
            }
        },request);

        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "saleTargetResolve";
        log.debug("销售指标模板位置tmpDirPath={}",tmpDirPath);
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "分解明细模板");

        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }
        //SaleDepartmentTargetDTO saleDepartmentTargetDTO = saleDepartmentTargetService.queryListByTargetId(request.getSaleTargetId(), request.getDepartId());
       // String fileName = saleDepartmentTargetDTO.getId() + Constants.SEPARATOR_UNDERLINE +  DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + ".xlsx";
        String fileName ="分解明细模板.xlsx";

        File excelDir = FileUtil.newFile(tmpExcelDir.getPath() + File.separator + fileName);

        if (excelDir.exists()) {
            FileUtil.del(excelDir);
        }

        excelDir.createNewFile();
        FileOutputStream fileOutputStream  = new FileOutputStream(excelDir);
        workbook.write(fileOutputStream);

        try {
            File zipFile = ZipUtil.zip(tmpExcelDir);
            if (ObjectUtil.isNotNull(zipFile)) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                String orginalFileName = URLEncoder.encode("分解明细模板.zip" , "UTF-8");
                objectMetadata.setContentDisposition("inline;filename*=utf-8'zh_cn'" + orginalFileName );
                objectMetadata.setHeader("Content-disposition", "filename*=utf-8'zh_cn'" + orginalFileName );
                FileInfo upload = fileService.upload(FileUtil.getInputStream(zipFile),orginalFileName, FileTypeEnum.SALE_TARGET_RESOLVE_FILE,objectMetadata);
                log.info("生成销售指标分解模板osskey={}targetid={}deptid={}",request.getSaleTargetId(),request.getDepartId(),upload.getKey());
                return upload.getKey();
            }

        } catch (Exception e){
            log.error("上传分解模板出错msg={}",e.getMessage());
        } finally {
            IoUtil.close(fileOutputStream);
            FileUtil.del(tmpDirPath);
        }
        return "";
    }*/
}
