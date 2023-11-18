import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.crm.api.CrmManorRepresentativeApi;
import com.yiling.dataflow.crm.dao.CrmEnterpriseMapper;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorRepresentativeRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.backup.api.AgencyBackupApi;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.statistics.api.FLowAnalyseCommonApi;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockForecastApi;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockWarnApi;
import com.yiling.dataflow.statistics.dao.FlowAnalyseCalculationResultMapper;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDaySaleQuantityMapper;
import com.yiling.dataflow.statistics.dao.FlowAnalyseDayStockQuantityMapper;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.StockForecastInfoDTO;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseGoodsRequest;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayDO;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDaySaleQuantityDO;
import com.yiling.dataflow.statistics.entity.FlowAnalyseDayStockQuantityDO;
import com.yiling.dataflow.statistics.service.FlowAnalyseCalculationResultService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayStockQuantityService;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
public class FlowAnalyseCommonApiTest  extends BaseTest{
    @Resource
    private FLowAnalyseCommonApi fLowAnalyseCommonApi;
    @Resource
    private FlowAnalyseDayService flowAnalyseDayService;

    @Resource
    private FlowAnalyseStockForecastApi flowAnalyseStockForecastApi;


    @Resource
    private FlowAnalyseStockWarnApi            flowAnalyseStockWarnApi;
    @Resource
    private FlowAnalyseCalculationResultMapper flowAnalyseCalculationResultMapper;
    @Resource
    private FlowDistributionEnterpriseService  flowDistributionEnterpriseService;
    @Autowired
    private FlowAnalyseDaySaleQuantityMapper   flowAnalyseDaySaleQuantityMapper;
    @Autowired
    private FlowAnalyseDayStockQuantityMapper  flowAnalyseDayStockQuantityMapper;

    @Autowired
    private FlowAnalyseDayStockQuantityService flowAnalyseDayStockQuantityService;
    @Autowired
    private FlowAnalyseCalculationResultService flowAnalyseCalculationResultService;
    @Autowired
    private CrmSupplierService crmSupplierService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Resource
    CrmHospitalService hospitalService;


    @Resource
    private CrmManorRepresentativeApi crmManorRepresentativeApi;
    @Test
    public void getGoodsName(){
        FlowAnalyseGoodsRequest request=new FlowAnalyseGoodsRequest();
        request.setGoodsName("莲花");
     Page<FlowAnalyseGoodsDTO> dtoLis= fLowAnalyseCommonApi.getGoodsListByName(request);
    }
    @Test
    public void saveSaveFlowDays(){
//        flowAnalyseDayService.selectByCurDay();
//        if(dayCount==0){
        for (int i = 90; i > 0; i--) {
            FlowAnalyseDayDO flowAnalyseDayDO=new FlowAnalyseDayDO();
            flowAnalyseDayDO.setCreateTime(new Date());
            flowAnalyseDayDO.setDateTime((DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-i))));
            flowAnalyseDayDO.setCreateUser(1L);
            flowAnalyseDayDO.setUpdateTime(new Date());
            flowAnalyseDayService.save(flowAnalyseDayDO);
        }

        //}
    }
    @Test
    public void  stockForecastInfoVo(){
        StockForecastInfoRequest request=new StockForecastInfoRequest();
        request.setEid(612L);
        request.setReplenishDays(15L);
        request.setSpecificationId(1104L);
        request.setReferenceTime1(3L);
        request.setReferenceTime2(30L);
        request.setEnameLevel("一级经销商");
        //request.setEnameGroup("sfssdf");
        StockForecastInfoDTO stockForecastInfoDTO = flowAnalyseStockForecastApi.stockForecastInfoVo(request);

        System.out.println("xxx");
    }
    @Test
    public  void stockWarnTestInfo(){
        StockWarnPageRequest request=new StockWarnPageRequest();
       // request.setEid(612L);
        request.setSpecificationId(41153L);
        //request.setEnameGroup("");
       // request.setEnameLevel("一级经销商");
        request.setCurrent(1);
        request.setSize(10);
      //  request.setEnameLevel("二级经销商");
        //request.setSort("asc");
       // request.setIconTab(4);
        Page<StockWarnDTO> stockWarnPage = flowAnalyseStockWarnApi.getStockWarnPage(request);
        StockWarnIconRequest request1=new StockWarnIconRequest();
       // request1.setEid(383L);
        request1.setSpecificationId(41153l);
        request1.setCurrent(1);
        request1.setSize(10);
//        request1.setEnameLevel("一级经销商");
//        request1.setEid(612L);
        request1.setIconTab(1);
        request1.setSort("asc");
        //Page<StockWarnIconDTO> saleDaysIconWarn = flowAnalyseStockWarnApi.getSaleDaysIconWarn(request1);
        System.out.println("");
    }

    @Test
    public  void initSaleStockData(){
        Random random=new Random();
     int a=   random.nextInt(20);

          int stock=30000;
        for (int i = 90; i > 0; i--) {
            FlowAnalyseDaySaleQuantityDO saleQuantityDO=new FlowAnalyseDaySaleQuantityDO();
            saleQuantityDO.setCreateTime(new Date());
            saleQuantityDO.setDateTime((DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-i))));
            saleQuantityDO.setCreateUser(1L);
            saleQuantityDO.setUpdateTime(new Date());
           // saleQuantityDO.setEid(27l);
            saleQuantityDO.setSoQuantity(61998l);
            saleQuantityDO.setSoQuantity(Long.valueOf(a));
            flowAnalyseDaySaleQuantityMapper.insert(saleQuantityDO);
            FlowAnalyseDayStockQuantityDO stockQuantityDO =new FlowAnalyseDayStockQuantityDO();
            stockQuantityDO.setCreateTime(new Date());
            stockQuantityDO.setDateTime((DateUtil.beginOfDay(DateUtil.offsetDay(new Date(),-i))));
            stockQuantityDO.setCreateUser(1L);
            stockQuantityDO.setUpdateTime(new Date());
            saleQuantityDO.setSoQuantity(61998l);
           // stockQuantityDO.setEid(27l);
            int crstock=stock-a;
            stockQuantityDO.setStockQuantity(Long.valueOf(crstock));
            flowAnalyseDayStockQuantityMapper.insert(stockQuantityDO);
            stock=crstock;
        }
    }
    @Test
    public void  getCurStock(){
        StockForecastInfoRequest request=new StockForecastInfoRequest();
        request.setEid(27L);
        request.setReplenishDays(15L);
        request.setSpecificationId(619981L);
        request.setReferenceTime1(3L);
        request.setReferenceTime2(7L);
        flowAnalyseDayStockQuantityService.getCurStockQuantity(request, Arrays.asList(27L));
    }
    @Test
    public void  deletefrom(){
        flowAnalyseCalculationResultService.deleteFlowAnalyseCalculationResult();
    }
    @Test
    public void testCrmSupplierSuffix(){
        String tableSuffix="wash_202302";
        List<Long> crmEnterIds=Arrays.asList(1L,2L);
        crmSupplierService.listSuffixByCrmEnterpriseIdList(crmEnterIds,tableSuffix);
    }

    @Test
    public void testCrmSuppli1erSuffix(){
        String tableSuffix="wash_202303";
        List<Long> crmEnterIds=Arrays.asList(51L);
        Long aLong = crmEnterpriseRelationShipService.listSuffixByCrmEnterpriseIdList(crmEnterIds, 38053L, tableSuffix);
       /* List<String> longs=Arrays.asList("城乡部新品产品组");
        List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.listSuffixByNameList(longs, 38053L, tableSuffix);
        System.out.println(relationShipDOS);*/
        System.out.println(aLong);
    }
    @Resource
    AgencyBackupApi backupApi;
    @Test
    public void testBackUp(){
        AgencyBackRequest a= new AgencyBackRequest();
        a.setOffsetMonth(0);
        backupApi.agencyInfoBackup(a);
    }

    @Test
    public void listByCrmEnterpriseIdList(){
        String tableSuffix="wash_202303";
        List<Long> crmEnterIds=Arrays.asList(51L);
        Long aLong = crmEnterpriseRelationShipService.listByCrmEnterpriseIdList(crmEnterIds, 38053L);
       /* List<String> longs=Arrays.asList("城乡部新品产品组");
        List<CrmEnterpriseRelationShipDO> relationShipDOS = crmEnterpriseRelationShipService.listSuffixByNameList(longs, 38053L, tableSuffix);
        System.out.println(relationShipDOS);*/
        System.out.println(aLong);
    }
    @Resource
    CrmEnterpriseService crmEnterpriseService;

    @Test
    public  void getCrmEnterpriseListByEidsAndProvinceCodeTest(){
        QueryCrmEnterpriseRelationShipPageListRequest request=new QueryCrmEnterpriseRelationShipPageListRequest();
        SjmsUserDatascopeBO bo=new SjmsUserDatascopeBO();
        bo.setOrgDatascope(2);
        SjmsUserDatascopeBO.OrgPartDatascopeBO part=new SjmsUserDatascopeBO.OrgPartDatascopeBO();
        part.setProvinceCodes(ListUtil.empty());
        part.setCrmEids(Arrays.asList(222905L,909288L));
        bo.setOrgPartDatascopeBO(part);
        request.setSjmsUserDatascopeBO(bo);
        Page<CrmEnterpriseRelationShipDTO> crmRelationPage = hospitalService.getCrmRelationPage(request);
    }
    @Test
    public void testSaveManor(){
        String textJson="{\"id\":0,\"manorId\":1,\"representativePostCode\":29654,\"representativePostName\":\"医药信息沟通专员02\"}";
        SaveCrmManorRepresentativeRequest request=  new SaveCrmManorRepresentativeRequest();
        request= JSON.parseObject(textJson,SaveCrmManorRepresentativeRequest.class);
        crmManorRepresentativeApi.saveOrUpdate(request);
    }
}
