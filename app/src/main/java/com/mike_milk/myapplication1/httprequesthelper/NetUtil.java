package com.mike_milk.myapplication1.httprequesthelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetUtil {

    //使用静态内部类的单例模式来创建网络请求专用的线程池,线程安全,同时达到了懒加载效果

    private static class Holder{
        private final static NetUtil INSTANCE = new NetUtil();

    }

    public static NetUtil getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * ThreadPoolExecutor的构造方法中的参数含义
     */
    //核心线程数量,同时能够执行的线程数量
    private int corePoolSize;
    //最大线程的数量,能够容纳的最大排队数
    private int maxPoolSize;
    //正在排队的任务的最长的排队时间
    private long keepAliveTime = 30;
    //正在排队的任务的最长排队时间的单位
    private TimeUnit timeUnit = TimeUnit.MINUTES;
    //线程池对象
    private ThreadPoolExecutor executor;

    //将构造方法设为私有,故无法从其他方式创建对象,构成单例
    NetUtil() {
        //获取当前设备可用的核心处理器核心数*2+1,赋值给核心线程的数量,能使性能达到极致
        corePoolSize = Runtime.getRuntime().availableProcessors()*2+1;
        //30应该够......
        maxPoolSize = 30;
        //初始化线程池
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                timeUnit,
                //缓冲队列,存放排队的任务,类似栈结构
                new LinkedBlockingDeque<Runnable>(),
                //创建线程的工厂(工厂模式?
                Executors.defaultThreadFactory(),
                //处理那些超出最大线程数量的策略
                new ThreadPoolExecutor.AbortPolicy()
                );
    }

    //下面的这个方法的参数我已经去掉,需要自己添加应该需要的参数
    public void execute(Request request,final Callback callback) {
        final String Url=request.getURL();
        final String[]key=request.getKey();
        final String[]value=request.getValue();
        final String Method=request.getMethod();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //这里就是子线程,可以进行IO操作
                HttpURLConnection connection=null;
                try {
                String a="";
                URL url=new URL(Url);
                connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod(Method);
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                //判断是否为POST。
                if (Method.equals("POST")){
                    for (int i=0;i<key.length;i++){
                        a=a+key[i]+"="+value[i];
                        if (i!=key.length-1){
                            a=a+"&";
                        }
                    }
                    byte[] sendData = a.getBytes();
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                    connection.setRequestProperty("Accept-Charset", sendData.length+"");
                    connection.setDoOutput(true);
                    OutputStream os=connection.getOutputStream();
                    os.write(a.getBytes());
                }
                if (connection.getResponseCode() == 200){
                InputStream in=connection.getInputStream();
                String result=new JSONObject(readData(in)).toString();
                callback.onResponse(result);
                }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                    callback.onFailed(e);
                }catch (IOException e){
                    e.printStackTrace();
                    callback.onFailed(e);
                }catch (JSONException e){
                    e.printStackTrace();
                    callback.onFailed(e);
                }finally {
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        });
    }

    public static String readData(InputStream inSream) throws IOException{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while( (len = inSream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inSream.close();
        String data=outStream.toString();
        outStream.close();
        return data;
    }

}
