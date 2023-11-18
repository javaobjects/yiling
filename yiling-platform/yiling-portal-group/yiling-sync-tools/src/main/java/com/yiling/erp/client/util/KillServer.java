package com.yiling.erp.client.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KillServer
{


    private Set<Integer> ports;

    public KillServer(int port)
    {
        Set ports = new HashSet();
        ports.add(Integer.valueOf(port));
        this.ports = ports;
    }

    public void start(int port)
    {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process p = runtime.exec("cmd /c netstat -ano | findstr \"" + port + "\"");
            InputStream inputStream = p.getInputStream();
            List<String> read = read(inputStream, "UTF-8");
            if (read.size() == 0) {
//                loger.info("找不到该端口的进程");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                for (String string : read) {
//                    loger.info(string);
                }
//                loger.info("找到" + read.size() + "个进程，正在准备清理");
                kill(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validPort(String str)
    {
        String dd = "^ *[a-zA-Z]+ +\\S+";
        Pattern pattern = Pattern.compile(dd);
        Matcher matcher = pattern.matcher(str);

        matcher.find();
        String find = matcher.group();
        int spstart = find.lastIndexOf(":");
        find = find.substring(spstart + 1);

        int port = 0;
        try {
            port = Integer.parseInt(find);
        } catch (NumberFormatException e) {
//            loger.info("查找到错误的端口:" + find);
            return false;
        }
        if (this.ports.contains(Integer.valueOf(port))) {
            return true;
        }
        return false;
    }

    public void kill(List<String> data)
    {
        Set pids = new HashSet();
        for (String line : data) {
            int offset = line.lastIndexOf(" ");
            String spid = line.substring(offset);
            spid = spid.replaceAll(" ", "");
            int pid = 0;
            try {
                pid = Integer.parseInt(spid);
            } catch (NumberFormatException e) {
//                loger.info("获取的进程号错误:" + spid);
            }
            pids.add(Integer.valueOf(pid));
        }
        killWithPid(pids);
    }

    public void killWithPid(Set<Integer> pids)
    {
        for (Integer pid : pids) {
            try {
                Process process = Runtime.getRuntime().exec("taskkill /F /pid " + pid + "");
                InputStream inputStream = process.getInputStream();
                String txt = readTxt(inputStream, "GBK");
//                loger.info(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> read(InputStream in, String charset)
            throws IOException
    {
        List data = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        String line;
        while ((line = reader.readLine()) != null) {
            boolean validPort = validPort(line);
            if (validPort) {
                data.add(line);
            }
        }
        reader.close();
        return data;
    }
    public String readTxt(InputStream in, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}