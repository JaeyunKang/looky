package com.looky.lookyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class AgeActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;

    HashMap<String, String> user;

    TextView year1;
    TextView year2;
    TextView month1;
    TextView month2;
    TextView day1;
    TextView day2;

    Button num1;
    Button num2;
    Button num3;
    Button num4;
    Button num5;
    Button num6;
    Button num7;
    Button num8;
    Button num9;
    Button num0;
    Button numBack;

    int cursor;

    TextView dialogText;
    Button positiveButton;
    Button negativeButton;

    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        db = new SQLiteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        user = db.getUserDetails();

        year1 = (TextView)findViewById(R.id.year1);
        year2 = (TextView)findViewById(R.id.year2);
        month1 = (TextView)findViewById(R.id.month1);
        month2 = (TextView)findViewById(R.id.month2);
        day1 = (TextView)findViewById(R.id.day1);
        day2 = (TextView)findViewById(R.id.day2);

        num1 = (Button)findViewById(R.id.num1);
        num2 = (Button)findViewById(R.id.num2);
        num3 = (Button)findViewById(R.id.num3);
        num4 = (Button)findViewById(R.id.num4);
        num5 = (Button)findViewById(R.id.num5);
        num6 = (Button)findViewById(R.id.num6);
        num7 = (Button)findViewById(R.id.num7);
        num8 = (Button)findViewById(R.id.num8);
        num9 = (Button)findViewById(R.id.num9);
        num0 = (Button)findViewById(R.id.num0);
        numBack = (Button)findViewById(R.id.numBack);

        cursor = 1;

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("1");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("2");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("3");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("4");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("5");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("6");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("7");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("8");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("9");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentTextView(cursor).setText("0");
                cursor += 1;
                if(cursor == 7) {
                    check();
                }
            }
        });
        numBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursor > 1) {cursor -= 1;}
                getCurrentTextView(cursor).setText("_");
            }
        });


    }

    public TextView getCurrentTextView(int cursor) {
        switch (cursor) {
            case 1: return year1;
            case 2: return year2;
            case 3: return month1;
            case 4: return month2;
            case 5: return day1;
            default: return day2;
        }
    }

    public void check() {
        year = Integer.parseInt(year1.getText().toString()) * 10 + Integer.parseInt(year2.getText().toString());
        month = Integer.parseInt(month1.getText().toString()) * 10 + Integer.parseInt(month2.getText().toString());
        day = Integer.parseInt(day1.getText().toString()) * 10 + Integer.parseInt(day2.getText().toString());

        if(month > 12 || day > 31) {
            Toast.makeText(getApplicationContext(), "생년월일을 다시 입력해주세요!", Toast.LENGTH_LONG).show();
            cursor = 1;
            year1.setText("_");
            year2.setText("_");
            month1.setText("_");
            month2.setText("_");
            day1.setText("_");
            day2.setText("_");
        }

        else {

            if(year > 17) { year += 1900; }
            else { year += 2000; }

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_age, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();
            dialog.show();

            dialogText = (TextView)dialogView.findViewById(R.id.dialogText);
            dialogText.setText(String.valueOf(year) + "년 " + String.format("%02d", month) + "월 " + String.format("%02d", day) + "일이시군요!");

            positiveButton = (Button)dialogView.findViewById(R.id.positiveButton);
            negativeButton = (Button)dialogView.findViewById(R.id.negativeButton);

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    cursor = 1;
                    year1.setText("_");
                    year2.setText("_");
                    month1.setText("_");
                    month2.setText("_");
                    day1.setText("_");
                    day2.setText("_");
                }
            });

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    updateAge(user.get("id"), year, month, day);
                }
            });

        }
    }

    private void updateAge(final String userId, final int year, final int month, final int day) {
        // Tag used to cancel the request
        String tag_string_req = "req_ageUpdate";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_AGE_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
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
                params.put("year", String.valueOf(year));
                params.put("month", String.valueOf(month));
                params.put("day", String.valueOf(day));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
