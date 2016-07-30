package com.xuan.paydemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unionpay.UPPayAssistEx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String serverMode = "01";
            Toast.makeText(MainActivity.this,""+msg.obj,Toast.LENGTH_LONG).show();
            UPPayAssistEx.startPay(MainActivity.this, null, null, msg.obj+"", serverMode);
//            doStartUnionPayPlugin(this, tn, 01);
//            UPPayAssistEx.startPay(activity, null, null, tn, mode);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pay(View view) {
        Toast.makeText(this, "点击支付", Toast.LENGTH_LONG).show();
        //在调用支付控件的代码按以下方式调用支付控件
//比如onclick或者handler等等...
/*参数说明：
activity —— 用于启动支付控件的活动对象
spId —— 保留使用，这里输入null
sysProvider —— 保留使用，这里输入null
orderInfo —— 订单信息为交易流水号，即TN，为商户后台从银联后台获取。
mode —— 银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起交易
返回值：
UPPayAssistEx.PLUGIN_VALID —— 该终端已经安装控件，并启动控件
UPPayAssistEx.PLUGIN_NOT_FOUND — 手机终端尚未安装支付控件，需要先安装支付控件
//*/
        new Thread(

        ){
            @Override
            public void run() {
                String tn = null;
                InputStream is;
                try {

                    String url = TN_URL_01;

                    URL myURL = new URL(url);
                    URLConnection ucon = myURL.openConnection();
                    ucon.setConnectTimeout(120000);
                    is = ucon.getInputStream();
                    int i = -1;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((i = is.read()) != -1) {
                        baos.write(i);
                    }

                    tn = baos.toString();
                    is.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = mHandler.obtainMessage();
                msg.obj = tn;
                mHandler.sendMessage(msg);
            }
        }.start();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
//        String str = data.getExtras().getString("pay_result");
//        if (str.equalsIgnoreCase(R_SUCCESS)) {
//            // 支付成功后，extra中如果存在result_data，取出校验
//// result_data结构见c）result_data参数说明
//            if (data.hasExtra("result_data")) {
//                String sign = data.getExtras().getString("result_data");
//// 验签证书同后台验签证书
//// 此处的verify，商户需送去商户后台做验签
//                if (verify(sign)) {
//                    //验证通过后，显示支付结果
//                    showResultDialog(" 支付成功！ ");
//                } else {
//// 验证不通过后的处理
//// 建议通过商户后台查询支付结果
//                }
//            } else {
//// 未收到签名信息
//// 建议通过商户后台查询支付结果
//            }
//        } else if (str.equalsIgnoreCase(R_FAIL)) {
//            showResultDialog(" 支付失败！ ");
//        } else if (str.equalsIgnoreCase(R_CANCEL)) {
//            showResultDialog(" 你已取消了本次订单的支付！ ");
//        }
    }
}