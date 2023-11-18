package com.yiling.erp.client.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.yiling.erp.client.common.Constants;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils
{
    private static final long serialVersionUID = 1L;
    private static final String ENCODING_UTF_8 = "UTF-8";

    public String javaToXml(Object o)
    {
        if (o == null) {
            return null;
        }

        StringWriter sw = new StringWriter();
        try
        {
            JAXBContext jAXBContext = JAXBContext.newInstance(new Class[] { o.getClass() });
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.marshal(o, sw);
        } catch (JAXBException e) {
            new ErpException("转换成xml格式出错", e);
        }

        return sw.toString();
    }

    public Object xmlToJava(String str, Object obj)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(new Class[] { Object.class });
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(new StringReader(str));
        } catch (JAXBException e) {
            new ErpException("转换成java对象出错", e);
        }
        return obj;
    }

    public void fileWrite(String filePath, String fileName, String context)
    {
        BufferedWriter writer = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String path = filePath + File.separator + fileName;

            File outFile = new File(path);
            if (outFile.exists()) {
                outFile.delete();
            }

            outFile.createNewFile();

            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            writer = new BufferedWriter(writerStream);
            writer.write(context);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(String path)
    {
        try
        {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        catch (Exception localException)
        {
        }
    }

    public void writeNullFile(String path) {
        try {
            File f = new File(path);
            FileWriter fw = new FileWriter(f, false);
            fw.write("");
            fw.close();
        }
        catch (IOException localIOException)
        {
        }
    }

    public static String readFileByLines(String fileName)
            throws Exception
    {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        if (!file.exists()) {
            return null;
        }
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempString = null;

            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException localIOException2) {
                }
            }
        }
        return sb.toString();
    }

    public static String readFileByNewLines(String fileName)
            throws Exception
    {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        if (!file.exists()) {
            return null;
        }
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String tempString = null;

            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString).append("\r\n");
            }
            reader.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException localIOException2) {
                }
            }
        }
        return sb.toString();
    }

    public boolean checkTime(String start, String end)
            throws Exception
    {
        if ((start == null) || (start.trim().equals(""))) {
            return false;
        }
        if ((end == null) || (end.trim().equals(""))) {
            return false;
        }
        try {
            int stime = Integer.valueOf(start).intValue();
            int etime = Integer.valueOf(end).intValue();
            if (stime < etime) {
                return true;
            }
        }
        catch (NumberFormatException e) {
            new ErpException("时间比较出错，请检查并正确的配置", e);
        }
        return false;
    }

    public long reDelay(String time)
    {
        long delay = 0L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date now1 = sdf.parse(sdf.format(new Date()));

            String s = sdf2.format(new Date()) + " " + time;
            Date now2 = sdf.parse(s);

            delay = now2.getTime() - now1.getTime();

            if (delay < 0L) {
                delay = 0L;
            }
        }
        catch (ParseException e) {
            new ErpException("时间设置有误，请检查并正确的配置", e);
        }

        return delay;
    }

    public boolean compareTime(String time, String min)
    {
        boolean flag = false;
        try {
            Calendar now = Calendar.getInstance();
            int hour = now.get(11);
            int minute = now.get(12);

            if (hour > Integer.valueOf(time).intValue()) {
                return flag;
            }

            if ((hour == Integer.valueOf(time).intValue()) && (minute > Integer.valueOf(min).intValue())) {
                return flag;
            }

            if (hour <= Integer.valueOf(time).intValue())
                return true;
        }
        catch (NumberFormatException e)
        {
            new ErpException("时间比较出错，请检查并正确的配置", e);
        }

        return flag;
    }

    public static String dateFormateToString(Date date, String mode)
    {
        SimpleDateFormat formate = new SimpleDateFormat(mode);
        String returnValue = formate.format(date);

        return returnValue;
    }

    public static String getStringValue(Object obj)
    {
        return obj.toString() + " ";
    }

    public static String defaultFlowStartDate(int offset) {
        Date endDate = new Date();
        Date startDate = cn.hutool.core.date.DateUtil.offset(endDate, DateField.DAY_OF_MONTH, offset);
        String jobTime = cn.hutool.core.date.DateUtil.format(startDate, Constants.FORMATE_DAY_TIME_JOB);
        return jobTime;
    }

    /**
     * 获取日期列表，["yyyy-MM-dd"]
     *
     * @param flowDateCount 天数，从结束日期向前推算的天数
     * @param endDate 结束日期
     * @return
     */
    public static List<String> dateList(Integer flowDateCount, Date endDate){
        Integer count=flowDateCount-1;
        List<String> dateStrList = new ArrayList<>();
        Date startDate = cn.hutool.core.date.DateUtil.offset(endDate, DateField.DAY_OF_MONTH, count * -1);
        List<DateTime> dateTimeList = cn.hutool.core.date.DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_MONTH);
        for (DateTime dateTime : dateTimeList) {
            String dateStr = cn.hutool.core.date.DateUtil.format(dateTime.toJdkDate(), Constants.FORMATE_DAY);
            dateStrList.add(dateStr);
        }
        return dateStrList;
    }

    /**
     * 清空目录
     * @param path 待清空目录地址
     * @return
     */
    public static boolean clearDirectory(String path){
        File file = new File(path);
        // 判断待清空目录是否存在
        if(!file.exists()){
            log.error("The dir is not exists, path -> {}", path);
            return false;
        }
        // 取得当前目录下所有文件和文件夹
        String[] content = file.list();
        for(String name : content){
            File temp = new File(path, name);
            // 判断是否是目录
            if(temp.isDirectory()){
                // 递归调用，删除目录里的内容
                clearDirectory(temp.getAbsolutePath());
                temp.delete();//删除空目录
            }else{
                // 直接删除文件
                if(!temp.delete()){
                    log.error("Failed to delete, name -> {}", name);
                }
            }
        }
        return true;
    }

    public static boolean isSqlserver(String dbType){
        if (dbType.equalsIgnoreCase("sql server") || dbType.equalsIgnoreCase("sql server2000")) {
            return true;
        }
        return false;
    }

    public static boolean isMysql(String dbType){
        if (dbType.equalsIgnoreCase("mysql")) {
            return true;
        }
        return false;
    }

    public static boolean isOracle(String dbType){
        if (dbType.equalsIgnoreCase("oracle")) {
            return true;
        }
        return false;
    }

    public static boolean isOdbc(String dbType){
        if (dbType.equalsIgnoreCase("odbc") || dbType.equalsIgnoreCase("odbc-dbf")) {
            return true;
        }
        return false;
    }

    /**
     * Map 获取value无视 key的大小写
     * @param map
     * @param key
     * @return
     */
    public static Object getObjectIgnoreCase(Map<String, Object> map, String key) {
        for (String k : map.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return map.get(k);
            }
        }
        return null;
    }

    public static boolean checkFlowDateCount(String flowDateCount){
        if(StrUtil.isBlank(flowDateCount)){
            return false;
        }
        String regex = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regex);
        boolean matches = pattern.matcher(flowDateCount).matches();
        if(!matches){
            return false;
        }
        return true;
    }


//    public static void main(String[] args) {
//        String flowDateCount = "21.5";
//        String regex ="^[1-9]\\d*$";
//        Pattern pattern = Pattern.compile(regex);
//        boolean matches = pattern.matcher(flowDateCount).matches();
//        if(!matches){
//            System.out.println(">>>>> 不合法");
//        }
//    }

//    public static void main(String[] args) {
//        String flowDateCount = "15";
//        Date endDate = new Date();
//        Date startDate = cn.hutool.core.date.DateUtil.offset(endDate, DateField.DAY_OF_MONTH, Integer.parseInt(flowDateCount) * -1);
//        List<DateTime> dateTimeList = cn.hutool.core.date.DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_MONTH);
//        List<String> dateStrList = new ArrayList<>();
//        for (DateTime dateTime : dateTimeList) {
//            String dateStr = DateUtil.format(dateTime.toJdkDate(), ErpConstants.FORMATE_DAY);
//            dateStrList.add(dateStr);
//        }
//    }
}