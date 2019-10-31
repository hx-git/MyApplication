package com.cali.libcore.http.interceptor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.cali.libcore.base.ContextHolder;
import com.cali.libcore.util.SystemUtils;
import com.cali.libcore.util.rsa.Md5Utils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import okhttp3.*;


/**
 * Created by mwh on 2017/10/12.
 * 这里要进行的一个操作添加公共参数
 */

public class ParamsInterceptor implements Interceptor {

    public static final String CONFIG="config";

    public static final String COOKIE="Cookie";


    public Context mContext;

    public ParamsInterceptor(Context context) {
        mContext = context;
    }


    public void showContent(Context context){
        String device_id= SystemUtils.INSTANCE.getIMEI(context);
        String device_platform="android";
        String device_type= SystemUtils.INSTANCE.getSystemModel();
        String device_brand= SystemUtils.INSTANCE.getDeviceBrand();
        String os_api= SystemUtils.INSTANCE.getSystemApi();
        String os_version= SystemUtils.INSTANCE.getSystemVersion();
        String uuid= SystemUtils.INSTANCE.getIMEI(context);   //这里和device_id有什么区别；
        String openudid= SystemUtils.INSTANCE.getOpenUid(context);     //uuid
        String resolution= SystemUtils.INSTANCE.getResolution(context);   //分辨率
        String dpi= SystemUtils.INSTANCE.getDensity(context);   //dip


        System.out.println("device_id: "+device_id);
        System.out.println("device_platform: "+device_platform);
        System.out.println("device_type: "+device_type);
        System.out.println("device_brand: "+device_brand);
        System.out.println("os_api: "+os_api);
        System.out.println("os_version: "+os_version);
        System.out.println("uuid: "+uuid);
        System.out.println("openudid: "+openudid);
        System.out.println("resolution: "+resolution);
        System.out.println("dpi: "+dpi);
    }



    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request(); // 112.124.22.238:8081/course_api/cniaoplay/featured?p={'page':0}
            String method = request.method();
            HashMap<String,String> paramsMap = new HashMap<>();
            //这里要配置的是sign；
            //commomParamsMap.put("")
            //这里的话，我最好将token带上；
            if(method.equals("GET")){
                HttpUrl httpUrl =  request.url();
                //这里封装是这样的情况，但是还有一个中可能是别人只是一个get请求，但是，http://www.baidu.com?page=6这样的形式呢？并没有参数p的话，该怎么办呢？
                if (httpUrl.queryParameterNames() != null) {
                    for (String s : httpUrl.queryParameterNames()) {
                        paramsMap.put(s,httpUrl.queryParameterValues(s).get(0));
                    }
                }
                paramsMap= formatParamsmap(paramsMap);
                //移除之有的参数；
                if (httpUrl.queryParameterNames() != null) {
                    for (String s : httpUrl.queryParameterNames()) {
                        paramsMap.remove(s);
                    }
                }

                String paramsUrl="";
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    paramsUrl+=entry.getKey()+"="+entry.getValue();
                    paramsUrl+="&";
                }
                paramsUrl=paramsUrl.substring(0,paramsUrl.length()-1);

                String url = httpUrl.toString();
                if(url.indexOf("?")>0){
                    //表示有参数，
                    url=url+"&"+paramsUrl;
                }else {
                    //这里我们要执行的一个逻辑就是将下面封装成下面的这样的形式：
                    url+="?"+paramsUrl;
                }

                request = request.newBuilder().url(url).build();
            }
            else  if(method.equals("POST")){
                RequestBody body = request.body();
                if(body instanceof FormBody){ // form 表单
                    for (int i = 0; i<((FormBody) body).size(); i++){
                        //拿到之前的已经存在的Gson数据；
                        paramsMap.put(((FormBody) body).encodedName(i),((FormBody) body).encodedValue(i));
                    }
                    paramsMap= formatParamsmap(paramsMap);
                    FormBody.Builder builder = new FormBody.Builder();
                    for (Map.Entry<String, String> stringObjectEntry : paramsMap.entrySet()) {
                        builder.add(stringObjectEntry.getKey(),stringObjectEntry.getValue());
                    }
                    //这里的话，我们要做的一个操作就是将重新构建一个请求；
                    request= request.newBuilder().method("POST", builder.build()).build();
                }
            }
        return chain.proceed(request);
    }



    /**
     * 有自己的token-->必须带自己的uid；
     * 使用默认的token--->不能带这个字段；
     * 这里我们将所有的请求参数进行一个封装的操作；
     * @return
     */
    public HashMap<String, String> formatParamsmap(HashMap<String, String> paramsMap){
        HashMap<String, String> params=paramsMap;
        String timesc=System.currentTimeMillis()+"";
        params.put("timesc", timesc);
        params.put("phonetype", "2");
        params.put("deviceid", SystemUtils.INSTANCE.getIMEI(mContext));
        params.put("osversion", SystemUtils.INSTANCE.getSystemVersion());
        params.put("phonemodel", SystemUtils.INSTANCE.getDeviceBrand());

        if(!TextUtils.isEmpty(ContextHolder.getChannel())){
            //将渠道号码作为公共参数传递出去；
            params.put("channel", ContextHolder.getChannel());
        }

        if(!TextUtils.isEmpty(ContextHolder.getVersionCode())){
            //将渠道号码作为公共参数传递出去；
            params.put("adCode", ContextHolder.getVersionCode());
            params.put("versionCode", ContextHolder.getVersionCode());
        }

        String from_uid = params.get("from_uid");
        if (TextUtils.isEmpty(from_uid)) {
            //这里表示没有uid；
            String uid = ContextHolder.getUid();
            if(!TextUtils.isEmpty(uid)){
                //添加参数；
                from_uid=uid;
            }
        }

        if(!TextUtils.isEmpty(from_uid)){
            params.put("from_uid", from_uid);
        }

        String sign = getSign(params);
        params.put("sign", sign);
        Log.i("info" , "sign==="+sign+",params=="+params.toString());
        return params;
    }



    /**
     * 这里是获取请求参数并且得到签名字符串；
     * 这里通过额外添加一个token生成一个sign
     * @param paramsMap
     * @return
     */
    public String getSign(HashMap<String, String> paramsMap){
        //通过这个我们去拿到sign;
        HashMap<String, String> clone = (HashMap<String, String>)paramsMap.clone();
        String token= ContextHolder.getToken();
        if (TextUtils.isEmpty(token)) {
            token = "hezuo2018";
        }
        clone.put("token", token);
        return sign(clone);
    }


    /**
     * 这里进行的一个操作就是对数据进行一个加密
     */
    public String sign(HashMap<String, String> paramsMap) {
        // 凭借字符串；
        Set<Map.Entry<String, String>> entrySet = paramsMap.entrySet();
        List<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : entrySet) {
            String va = entry.getKey() + ":" + entry.getValue();
            params.add(va);
        }
        System.out.println("Retrofit:list:" + params);
        // 进行一个排序；
        Collections.sort(params);
        // 取出来进行一个字符的凭借；
        System.out.println("Retrofit:listSorted:" + params);
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            strBuilder.append(params.get(i));
            if (i != params.size() - 1) {
                strBuilder.append(",");
            }
        }
        String string = strBuilder.toString();
        System.out.println("Retrofit:sorted: " + string);
        // 进行一个md5加密；
        String md5 = Md5Utils.INSTANCE.getMd5(string);
        System.out.println("Retrofit:sign: " + md5);
        return md5;
    }
}
