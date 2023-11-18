package com.yiling.hmc.insurance;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.config.RedisKeyExpirationListener;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.hmc.settlement.service.InsuranceSettlementService;
import com.yiling.hmc.wechat.dto.request.InsuranceJoinNotifyContext;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import com.yiling.ih.patient.dto.request.SavePatientRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author: gxl
 * @date: 2022/5/18
 */
public class InsuranceRecordServiceTest extends BaseTest {

    @Autowired
    private InsuranceSettlementService insuranceSettlementService;

    @Autowired
    private RedisKeyExpirationListener keyExpirationListener;

    @Autowired
    private InsuranceRecordService insuranceRecordService;

    @Test
    public void savePatient() {
        SavePatientRequest issue = new SavePatientRequest();
        issue.setPatientName("张三");
        issue.setIdCard("123");

        SavePatientRequest holder = new SavePatientRequest();
        issue.setPatientName("张三");
        issue.setIdCard("123");

        List<SavePatientRequest> savePatientRequests = Arrays.asList(issue, holder);
        List<SavePatientRequest> result = savePatientRequests.stream().distinct().filter(item-> StrUtil.isNotBlank(item.getPatientName())).collect(toList());
        System.out.println(result);
    }

    @Test
    public void joinNotify() {

        String s= "{\"payRequest\":{\"minSequence\":\"1\",\"amount\":301800,\"payTime\":1662446590000,\"policyNo\":\"H220906015625920137297\",\"opTime\":1662446599060,\"maxSequence\":\"1\",\"startTime\":1662446590000,\"endTime\":1693982581000,\"payStatus\":1},\"fetchPlanDetailRequestList\":[{\"eid\":40,\"marketPrice\":35,\"terminalSettlePrice\":120,\"specificInfo\":\"0.4g*36粒\",\"goodsId\":43682,\"settlePrice\":23.81,\"hmcGoodsId\":1,\"insurancePrice\":26,\"opTime\":1662446599069,\"sellSpecificationsId\":1104,\"perMonthCount\":10,\"goodsName\":\"参松养心胶囊\",\"insuranceGoodsCode\":\"035366\"}],\"insuranceRecordRequest\":{\"relationType\":1,\"eid\":40,\"issueName\":\"我二哥\",\"issueTime\":1662446590000,\"effectiveTime\":1662446590000,\"policyNo\":\"H220906015625920137297\",\"policyStatus\":1,\"policyUrl\":\"http://www-tk-cn-test-wh-tkc.tk.cn/sp-greedy/sit?api_s=document.property&api_m=policy.download&applicantName=70DE27DF68A109C116DE116B51C33CF105CF32DF71B87C91BF102CE65B50&policyNo=E47BF32C101C68BE73CF117BE8BE97D100C44BE118AE34BE115A60DE71C5DE121AE44A5C77B91A106CF93D106AF52BE119D84AF76D71B106BF1C26\",\"issueCredentialType\":\"01\",\"proposalTime\":1662446582000,\"insuranceId\":1,\"currentEndTime\":1693982581000,\"holderName\":\"我二哥\",\"comboName\":\"以岭药品险（年交）\",\"billType\":2,\"holderPhone\":\"13161895316\",\"userId\":17126,\"holderCredentialType\":\"01\",\"expiredTime\":1693982581000,\"proposalNo\":\"P10500510N02022781322698944297\",\"opTime\":1662446599060,\"insuranceCompanyId\":1,\"issueCredentialNo\":\"110101197303077152\",\"issuePhone\":\"13161895316\",\"holderCredentialNo\":\"110101197303077152\"},\"payPlanRequestList\":[],\"issueRequest\":{\"patientName\":\"我二哥\",\"fromUserId\":17126,\"idCard\":\"110101197303077152\",\"mobile\":\"13161895316\",\"opUserId\":17126,\"opTime\":1662446599074},\"opTime\":1662446599074,\"holderRequest\":{\"patientName\":\"我二哥\",\"fromUserId\":17126,\"idCard\":\"110101197303077152\",\"mobile\":\"13161895316\",\"opUserId\":17126,\"opTime\":1662446599074},\"fetchPlanRequestList\":[{\"fetchStatus\":2,\"initFetchTime\":1662446590000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1665038590000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1667716990000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1670308990000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1672987390000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1675665790000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1678084990000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1680763390000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1683355390000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1686033790000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1688625790000,\"opTime\":1662446599060},{\"fetchStatus\":2,\"initFetchTime\":1691304190000,\"opTime\":1662446599060}]}";
        InsuranceJoinNotifyContext context = JSONUtil.toBean(s, InsuranceJoinNotifyContext.class);
        Long id = insuranceRecordService.joinNotify(context);
        System.out.println(id);
    }


    @Test
    public void syncOrder() {
        SyncOrderRequest request = new SyncOrderRequest();
        request.setId(323L);
        boolean result = insuranceSettlementService.syncOrder(request);
        System.out.println(result);
    }

    @Test
    public void onMessage() {
        DefaultMessage message = new DefaultMessage("null".getBytes(StandardCharsets.UTF_8), "hmc:gzh_subscribe:oD2xR6HoWyESQxMLr12M7-BEmcIw".getBytes(StandardCharsets.UTF_8));
        keyExpirationListener.onMessage(message, null);

    }

    @Test
    public void downloadFile() {
        String url = "http://infntlb.core.online.taikang.com/sp-greedy/api/v2/?api_s=document.property&api_m=policy.download&applicantName=1CF66DE121B124D117C89DF120BE65D63D36AF5BE84D108CF84CE55C73&policyNo=E39B117D59B6DF98DE43CE126B37A104C78C93A12D77D27A85AF68D63A15B63CE33AF81AE31CF103C16B83AE40B10C5AF117BE3C107DF90";
        String dest = "/Users/shenfan/Downloads";
        HttpUtil.downloadFile(url, dest);
    }

    @Test
    public void zipFile() {
        try {
            File sourceFile = new File("/Users/shenfan/Pictures/发票1.jpeg");
            BufferedImage templateImage = ImageIO.read(sourceFile);
            int height = templateImage.getHeight();
            int width = templateImage.getWidth();
            float scale = 0.4f;
            int withHeight = (int) (scale * height);
            int withWidth = (int) (scale * width);
            BufferedImage finalImage = new BufferedImage(withWidth, withHeight, BufferedImage.TYPE_INT_RGB);
            finalImage.getGraphics().drawImage(templateImage.getScaledInstance(withWidth, withHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            File newFile = File.createTempFile("tk_temp_", sourceFile.getName().substring(sourceFile.getName().lastIndexOf(".")));
            System.out.println(newFile.getAbsolutePath());
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            ImageIO.write(finalImage, sourceFile.getName().substring(sourceFile.getName().lastIndexOf(".") + 1), fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}