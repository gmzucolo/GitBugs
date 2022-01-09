package com.example.gitbugs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView nameView, userView, orgView, bioView, fwsView, fwgView, repoView;
    private ImageView avatar;
    private WebView logoutWindow;

    private RequestQueue queue;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        components();
        requestUser();
    }

    private void components() {
        context = this;
        nameView = findViewById(R.id.nameVal);
        userView = findViewById(R.id.userVal);
        orgView = findViewById(R.id.orgVal);
        bioView = findViewById(R.id.bioVal);
        fwsView = findViewById(R.id.fwsVal);
        fwgView = findViewById(R.id.fwgVal);
        repoView = findViewById(R.id.repoVal);
        avatar = findViewById(R.id.avatar);
        logoutWindow = findViewById(R.id.logoutWindow);
        logoutWindow.setVisibility(View.GONE);

        queue = Volley.newRequestQueue(context);

        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = findViewById(R.id.llContainer);
                linearLayout.setVisibility(View.GONE);
                logoutWindow.setVisibility(View.VISIBLE);
                logoutWindow.getSettings().setJavaScriptEnabled(true);
                logoutWindow.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        if (request.getUrl().toString().equals("https://github.com/")) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                });
                logoutWindow.loadUrl("https://github.com/logout");
            }
        });

    }

    private void requestUser() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                U.BASE_URL + "/user",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User gitUser = new User(
                                    U.checkNull(response.getString("name")),
                                    U.checkNull(response.getString("login")),
                                    U.checkNull(response.getString("company")),
                                    U.checkNull(response.getString("bio")),
                                    U.checkNull(response.getString("avatar_url")),
                                    response.getInt("followers"),
                                    response.getInt("following"),
                                    response.getInt("public_repos") +
                                            response.getInt("total_private_repos")
                            );
                            setComponents(gitUser);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                U.HEADERS.put("Content-Type", "application/json");
                return U.HEADERS;
            }
        };
        queue.add(request);
    }

    private void setComponents(User user) {
        nameView.setText(user.getName());
        userView.setText(user.getUsername());
        orgView.setText(user.getOrganization());
        bioView.setText(user.getBio());
        fwsView.setText("" + user.getFollowers());
        fwgView.setText(Integer.toString(user.getFollowing()));
        repoView.setText("" + user.getRepositories());

        Glide.with(context).
                load(user.getAvatarUrl()).
                centerInside().
                into(avatar);
    }
}