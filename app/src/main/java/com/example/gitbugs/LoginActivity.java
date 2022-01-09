package com.example.gitbugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    private WebView loginWindow;
    private String url;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        components();
    }

    private void components() {
        queue = Volley.newRequestQueue(this);
        loginWindow = findViewById(R.id.loginWindow);
        loginWindow.getSettings().setJavaScriptEnabled(true);
        loginWindow.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                getCode(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        loginWindow.loadUrl(U.LOGIN_URL + "/authorize" +
                "?client_id=" + U.CLIENT_ID +
                "&scope=" + U.SCOPE +
                "&state=" + U.STATE +
                "&login=gmzucolo");
    }

    private void getCode(String codeAndScope) {
        url = U.LOGIN_URL + "/access_token=";
        if (codeAndScope.contains("?code=")) {
            codeAndScope = codeAndScope.split(".com/")[1].substring(1);

            url += "/?client_id=" + U.CLIENT_ID +
                    "&client_secret" + U.CLIENT_SECRET +
                    "&" + codeAndScope;
            tokenRequest();
        }
    }

    private void tokenRequest() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        U.TOKEN = response.split("&")[0].substring(13);
                        U.HEADERS.put("Authorization", "Bearer " + U.TOKEN);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                },
                null
        );
        queue.add(request);
    }
}