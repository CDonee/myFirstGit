package com.lide.mygit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lide.mygit.bean.Configs;
import com.lide.mygit.bean.RequestLookProduct;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBt_req;
    private TextView mTv_rece;
    private String mRequestJsonData;
    private String mPostUrl1;
    private String mPostUrl;
    private String mRequestJsonData1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBt_req = (Button) findViewById(R.id.bt_req);
        mTv_rece = (TextView) findViewById(R.id.tv_receive);
        mBt_req.setOnClickListener(this);
       init();
    }

    private void init() {

//        String apiMethod = "ai/security/login";
//        mPostUrl1 = Configs.apiUrl + apiMethod;
//        UserToJson userToJson = new UserToJson();
//        userToJson.accountType = "EMPLOYEE";
//        userToJson.businessModuleCode = "HDW";
//        userToJson.username = "admin";
//        userToJson.password = "admin";
//        //userToJson.warehouseCode = map.get("warehouseCode");
//        mRequestJsonData = new Gson().toJson(userToJson);

        String apiMethod = "api/seeding-position/wave-number/confirm";
        mPostUrl = Configs.apiUrl + apiMethod;

        RequestLookProduct requestLookProduct = new RequestLookProduct();
        requestLookProduct.waveNumber = "222";

        mRequestJsonData1 = new Gson().toJson(requestLookProduct);



    }


    @Override
    public void onClick(View v) {
        OkGo.post(mPostUrl).tag(this).upJson(mRequestJsonData1).headers("authorization", "APP_KEYS "+Configs.appKey).execute(new MyCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                Toast.makeText(MainActivity.this,"before",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                mTv_rece.setText(s);
                Log.d("test",s);
            }

            @Override
            public void MyOnError(Call call, Response response, Exception e,String errorMsg) {
                  mTv_rece.setText(errorMsg);
                 Toast.makeText(MainActivity.this,"error"+e.getMessage()+"__"+errorMsg,Toast.LENGTH_SHORT).show();

        }

        });
    }


    public  abstract  class MyCallback extends AbsCallback<String> implements reqErr {

        private String mS;

        @Override
        public  abstract void onSuccess(String s, Call call, Response response) ;

        @Override
        public String convertSuccess(Response response) throws Exception {
            mS = StringConvert.create().convertSuccess(response);

            response.close();
            return mS;
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            MyOnError(call, response, e,mS);
        }

        @Override
        public abstract void MyOnError(Call call, Response response, Exception e,String errorMsg) ;
    }

    interface  reqErr{
        void MyOnError(Call call, Response response, Exception e,String errorMsg);
    }
}
