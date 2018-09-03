package com.looky.lookyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.looky.app.AppConfig;
import com.looky.app.AppController;
import com.looky.helper.SQLiteHandler;
import com.looky.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GenderActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;

    HashMap<String, String> user;

    int gender;
    LinearLayout manLayout;
    LinearLayout womanLayout;
    ImageButton manButton;
    ImageButton womanButton;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        user = db.getUserDetails();

        gender = 2;

        manLayout = (LinearLayout) findViewById(R.id.manLayout);
        womanLayout = (LinearLayout) findViewById(R.id.womanLayout);
        manButton = (ImageButton) findViewById(R.id.manButton);
        womanButton = (ImageButton) findViewById(R.id.womanButton);
        button = (Button) findViewById(R.id.button);

        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manLayout.setAlpha(1.0f);
                womanLayout.setAlpha(0.5f);
                button.setAlpha(1.0f);
                gender = 0;
            }
        });

        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                womanLayout.setAlpha(1.0f);
                manLayout.setAlpha(0.5f);
                button.setAlpha(1.0f);
                gender = 1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGender(user.get("id"), gender);
            }
        });

    }

    private void updateGender(final String userId, final int gender) {
        // Tag used to cancel the request
        String tag_string_req = "req_genderUpdate";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GENDER_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        db.updateUserGender(String.valueOf(gender));
                        Intent ageIntent = new Intent(getApplicationContext(), AgeActivity.class);
                        startActivity(ageIntent);
                        finish();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("gender", String.valueOf(gender));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
