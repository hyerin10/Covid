package com.swu.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static RequestQueue requestQueue;

    Button button = null;
    TextView textView = null;
    String url = "http://sc1.swu.ac.kr/~moo/test.jsp";
    String gubun="";
    String startDate="";
    String endDate="";
    String resData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.volley_btn);
        TextView textView = (TextView) findViewById(R.id.textView);
        EditText localEditText = (EditText) findViewById(R.id.localEditText);
        EditText dateEditText = (EditText) findViewById(R.id.dateEditText);
        EditText dateEditText2 = (EditText) findViewById(R.id.dateEditText2);

        // web application으로 전송
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              gubun = localEditText.getText().toString();
//              startDate= dateEditText.getText().toString();
//              endDate = dateEditText2.getText().toString();
                gubun = "서울";
                startDate= "2022-06-01";
                endDate = "2022-06-01";

                // json 만들기
                JSONObject requestJson = new JSONObject();

                try {
                    requestJson.put("gubun", gubun);
                    requestJson.put("startDate", startDate);
                    requestJson.put("endDate", endDate);
                    makeRequest(requestJson);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //딜레이 후 시작할 코드 작성
                            resData = getResponse();
                            textView.setText(resData);
                        }
                    }, 4000);// 3초 정도 딜레이를 준 후 시작


                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.e("Test", "1");
        // 응답 받아서 textView에 보여주기
        try {
            Log.e("try"," 들어옴");
            resData = getResponse();
            JSONObject jsonObject = new JSONObject(resData);
            String innerStr = jsonObject.getString("response");
            JSONObject innerData = new JSONObject(innerStr);

            Log.e("try"," 2");
            Log.e("localOccCnt", innerData.getString("localOccCnt"));
            Log.e("try"," 3");
            String temp = innerData.getString("localOccCnt");
            textView.setText("test");
        } catch (JSONException e) {
            e.printStackTrace();
        }




    if (requestQueue == null)
    {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    }

    public void setResponse(String res)
    {
        resData = res;
    }
    public String getResponse()
    {
        return resData;
    }
    public void makeRequest(JSONObject jobj) throws UnsupportedEncodingException {
        String strUrl = url + "?searchparams="+ URLEncoder.encode(jobj.toString(), "UTF-8");
        System.out.println(strUrl);
        StringRequest request = new StringRequest(Request.Method.GET, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("응답->", response);
                setResponse(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("에러->", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        request.setShouldCache(false);
        requestQueue.add(request);
        Log.e("요청 보", "냄");

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //딜레이 후 시작할 코드 작성
                Log.e("getResponse()", getResponse());
                resData = getResponse();
            }
        }, 3000);// 4초 정도 딜레이를 준 후 시작

        //textView.setText(jsonParsing(getResponse()).toString());
    }


}