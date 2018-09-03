package com.looky.lookyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.looky.app.AppConfig;
import com.looky.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.looky.lookyapp.MainActivity.WEBVIEW_REQUEST;
import static com.looky.lookyapp.MainActivity.activity;
import static com.looky.lookyapp.MainActivity.currentCategory;
import static com.looky.lookyapp.MainActivity.currentSubCategory;
import static com.looky.lookyapp.MainActivity.db;
import static com.looky.lookyapp.MainActivity.fab;
import static com.looky.lookyapp.MainActivity.mainTutorial;
import static com.looky.lookyapp.MainActivity.previousCategory;
import static com.looky.lookyapp.MainActivity.previousSubCategory;
import static com.looky.lookyapp.MainActivity.user;
import static com.looky.utils.Utils.categoryNameMapping;
import static com.looky.utils.Utils.ratingDB;
import static com.looky.utils.Utils.shuffleJsonArray;
import static com.looky.utils.Utils.subcategoryNameMapping;


public class MainFragment extends Fragment {

    RelativeLayout firstTutorial;
    RelativeLayout secondTutorial;
    Button nextTutorial;
    Button completeTutorial;

    ArrayList<String> itemList;
    ArrayList<String> scoreList;
    ArrayList<String> savedAtList;

    int item1_count;
    int item2_count;
    String item1_string;
    String item2_string;
    int rating;
    int index;

    RelativeLayout card2;
    ImageView likeIcon2;
    ImageView dislikeIcon2;
    ImageView image2;
    TextView text2;
    TextView scoreText2;
    TextView scoreView2;

    RelativeLayout card1;
    ImageView likeIcon1;
    ImageView dislikeIcon1;
    ImageView image1;
    TextView text1;
    TextView scoreText1;
    TextView scoreView1;

    float dX;
    float dY;

    public MainFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        itemList = new ArrayList<String>();
        scoreList = new ArrayList<String>();
        savedAtList = new ArrayList<String>();

        prediction(user.get("id"), user.get("gender"), categoryNameMapping(currentCategory), subcategoryNameMapping(currentSubCategory));

        return inflater.inflate(R.layout.fragment_main, container, false);


    }


    @Override
    public void onStart() {

        super.onStart();

        fab.setVisibility(View.VISIBLE);

        firstTutorial = (RelativeLayout) getView().findViewById(R.id.firstTutorial);
        secondTutorial = (RelativeLayout) getView().findViewById(R.id.secondTutorial);
        nextTutorial = (Button) getView().findViewById(R.id.nextTutorial);
        completeTutorial = (Button) getView().findViewById(R.id.completeTutorial);

        if(mainTutorial.equals("0")) {
            firstTutorial.setVisibility(View.VISIBLE);
        }

        nextTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstTutorial.setVisibility(View.INVISIBLE);
                secondTutorial.setVisibility(View.VISIBLE);
            }
        });

        completeTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondTutorial.setVisibility(View.INVISIBLE);
                db.setTutorialMainCompleted();
                mainTutorial = "1";
            }
        });


        card2 = (RelativeLayout) getView().findViewById(R.id.card2);
        likeIcon2 = (ImageView) getView().findViewById(R.id.like2);
        dislikeIcon2 = (ImageView) getView().findViewById(R.id.dislike2);
        image2 = (ImageView) getView().findViewById(R.id.image2);
        text2 = (TextView) getView().findViewById(R.id.text2);
        scoreView2 = (TextView) getView().findViewById(R.id.scoreView2);
        scoreText2 = (TextView) getView().findViewById(R.id.scoreText2);

        card1 = (RelativeLayout) getView().findViewById(R.id.card1);
        likeIcon1 = (ImageView) getView().findViewById(R.id.like1);
        dislikeIcon1 = (ImageView) getView().findViewById(R.id.dislike1);
        image1 = (ImageView) getView().findViewById(R.id.image1);
        text1 = (TextView) getView().findViewById(R.id.text1);
        scoreView1 = (TextView) getView().findViewById(R.id.scoreView1);
        scoreText1 = (TextView) getView().findViewById(R.id.scoreText1);

        card2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent event) {


                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:


                        if(event.getRawX() + dX > 80.0)
                        {
                            likeIcon2.setVisibility(View.VISIBLE);
                        }
                        else if(event.getRawX() + dX < -80.0)
                        {
                            dislikeIcon2.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            likeIcon2.setVisibility(View.INVISIBLE);
                            dislikeIcon2.setVisibility(View.INVISIBLE);
                        }

                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .rotation((event.getRawX() + dX)/(float)150.0)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:

                        if(event.getRawX() + dX > 400.0 || event.getRawX() + dX < -400.0)
                        {
                            if(event.getRawX() + dX > 400.0)
                            {

                                rating = 0;
                                view.animate()
                                        .x(1000.0f)
                                        .y(1000.0f)
                                        .rotation(60.0f)
                                        .alphaBy(1.0f)
                                        .alpha(0.0f)
                                        .setDuration(500)
                                        .start();
                            }
                            else{

                                rating = 1;
                                view.animate()
                                        .x(-1000.0f)
                                        .y(1000.0f)
                                        .alphaBy(1.0f)
                                        .alpha(0.0f)
                                        .rotation(-60.0f)
                                        .setDuration(500)
                                        .start();

                            }

                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    card1.bringToFront();
                                    view.animate()
                                            .x(0.0f)
                                            .y(0.0f)
                                            .rotation(0.0f)
                                            .alphaBy(0.0f)
                                            .alpha(1.0f)
                                            .setDuration(0)
                                            .start();
                                    likeIcon2.setVisibility(View.INVISIBLE);
                                    dislikeIcon2.setVisibility(View.INVISIBLE);

                                    ratingDB(user.get("id"), item2_string, rating, "0");
                                    item2_count += 2;
                                    item2_string = itemList.get(item2_count);
                                    setItemName(item2_string, 2);
                                    scoreText2.setText(currentCategory + ", 업데이트: ");
                                    String year = savedAtList.get(item2_count).substring(2,4);
                                    String month = savedAtList.get(item2_count).substring(5,7);
                                    String day = savedAtList.get(item2_count).substring(8,10);
                                    scoreView2.setText(year + "년 " + month + "월 " + day + "일");

                                    Glide.with(activity).load("https://s3.ap-northeast-2.amazonaws.com/looky/" +
                                            String.format("%09d", Integer.parseInt(item2_string)) + ".jpg").centerCrop().into(image2);
                                    index = 1;
                                    setItemLink(item1_string);


                                }
                            }, 500);

                        }

                        else
                        {
                            view.animate()
                                    .x((float)0.0)
                                    .y((float)0.0)
                                    .rotation((float)0.0)
                                    .setDuration(500)
                                    .start();

                            likeIcon2.setVisibility(View.INVISIBLE);
                            dislikeIcon2.setVisibility(View.INVISIBLE);
                        }

                        break;

                    default:
                        return false;
                }

                return true;
            }
        });

        card1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent event) {


                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:


                        if(event.getRawX() + dX > 80.0)
                        {
                            likeIcon1.setVisibility(View.VISIBLE);
                        }
                        else if(event.getRawX() + dX < -80.0)
                        {
                            dislikeIcon1.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            likeIcon1.setVisibility(View.INVISIBLE);
                            dislikeIcon1.setVisibility(View.INVISIBLE);
                        }

                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .rotation((event.getRawX() + dX)/(float)150.0)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:

                        if(event.getRawX() + dX > 400.0 || event.getRawX() + dX < -400.0)
                        {
                            if(event.getRawX() + dX > 400.0)
                            {
                                rating = 0;

                                view.animate()
                                        .x(1000.0f)
                                        .y(1000.0f)
                                        .rotation(60.0f)
                                        .alphaBy(1.0f)
                                        .alpha(0.0f)
                                        .setDuration(500)
                                        .start();
                            }

                            else
                            {
                                rating = 1;

                                view.animate()
                                        .x(-1000.0f)
                                        .y(1000.0f)
                                        .alphaBy(1.0f)
                                        .alpha(0.0f)
                                        .rotation(-60.0f)
                                        .setDuration(500)
                                        .start();
                            }

                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    card2.bringToFront();
                                    view.animate()
                                            .x(0.0f)
                                            .y(0.0f)
                                            .rotation(0.0f)
                                            .alphaBy(0.0f)
                                            .alpha(1.0f)
                                            .setDuration(0)
                                            .start();
                                    likeIcon1.setVisibility(View.INVISIBLE);
                                    dislikeIcon1.setVisibility(View.INVISIBLE);

                                    ratingDB(user.get("id"), item1_string, rating, "0");
                                    item1_count += 2;
                                    item1_string = itemList.get(item1_count);
                                    setItemName(item1_string, 1);
                                    scoreText1.setText(currentCategory + ", 업데이트: ");
                                    String year = savedAtList.get(item1_count).substring(2,4);
                                    String month = savedAtList.get(item1_count).substring(5,7);
                                    String day = savedAtList.get(item1_count).substring(8,10);
                                    scoreView1.setText(year + "년 " + month + "월 " + day + "일");

                                    Glide.with(activity).load("https://s3.ap-northeast-2.amazonaws.com/looky/" +
                                            String.format("%09d", Integer.parseInt(item1_string)) + ".jpg").centerCrop().into(image1);
                                    index = 2;
                                    setItemLink(item2_string);
                                }
                            }, 500);
                        }


                        else
                        {
                            view.animate()
                                    .x((float)0.0)
                                    .y((float)0.0)
                                    .rotation((float)0.0)
                                    .setDuration(500)
                                    .start();

                            likeIcon1.setVisibility(View.INVISIBLE);
                            dislikeIcon1.setVisibility(View.INVISIBLE);
                        }


                        break;

                    default:
                        return false;
                }

                return true;
            }
        });
    }

    private void setItemName(final String itemId, final int cardIndex) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ITEM_NAME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String itemName = jObj.getString("itemName");

                        if (cardIndex == 1) {
                            text1.setText(itemName);
                        }
                        if (cardIndex == 2) {
                            text2.setText(itemName);
                        }


                    } else {
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
                Log.e("", "Getting Name Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("itemId", itemId);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "");
    }

    private void setItemLink(final String itemId) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ITEM_LINK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        final String itemLink = jObj.getString("itemLink");


                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(index == 1)
                                {
                                    ratingDB(user.get("id"), item1_string, 2, "0");
                                }
                                else if(index == 2)
                                {
                                    ratingDB(user.get("id"), item2_string, 2, "0");
                                }

                                Intent shoppingIntent = new Intent(activity, WebviewActivity.class);
                                shoppingIntent.putExtra("url", itemLink);
                                startActivity(shoppingIntent);
                            }
                        });



                    } else {
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
                Log.e("", "Getting Link Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("itemId", itemId);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "");
    }

    private void prediction(final String userId, final String gender, final String category, final String subcategory) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PREDICTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray jsonArray = jObj.getJSONArray("res");

                        if(jsonArray.length() == 0)
                        {
                            Toast.makeText(activity, "상품이 없어요!", Toast.LENGTH_LONG).show();
                            currentCategory = previousCategory;
                            currentSubCategory = previousSubCategory;
                            return;
                        }



                        itemList.clear();
                        scoreList.clear();
                        savedAtList.clear();

                        jsonArray = shuffleJsonArray(jsonArray);

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject itemAndScore = jsonArray.getJSONObject(i);
                            itemList.add(itemAndScore.getString("item"));
                            scoreList.add(itemAndScore.getString("score"));
                            savedAtList.add(itemAndScore.getString("savedAt"));
                        }

                        item2_count = 0;
                        item1_count = 1;
                        item2_string = itemList.get(item2_count);
                        item1_string = itemList.get(item1_count);

                        index = 2;

                        Glide.with(activity).load("https://s3.ap-northeast-2.amazonaws.com/looky/" +
                                String.format("%09d", Integer.parseInt(item2_string)) + ".jpg").centerCrop().into(image2);
                        Glide.with(activity).load("https://s3.ap-northeast-2.amazonaws.com/looky/" +
                                String.format("%09d", Integer.parseInt(item1_string)) + ".jpg").centerCrop().into(image1);

                        scoreText2.setText(currentCategory + ", 업데이트: ");
                        String year = savedAtList.get(item2_count).substring(2,4);
                        String month = savedAtList.get(item2_count).substring(5,7);
                        String day = savedAtList.get(item2_count).substring(8,10);
                        scoreView2.setText(year + "년 " + month + "월 " + day + "일");
                        scoreText1.setText(currentCategory + ", 업데이트: ");
                        year = savedAtList.get(item1_count).substring(2,4);
                        month = savedAtList.get(item1_count).substring(5,7);
                        day = savedAtList.get(item1_count).substring(8,10);
                        scoreView1.setText(year + "년 " + month + "월 " + day + "일");
                        setItemName(item2_string, 2);
                        setItemName(item1_string, 1);
                        setItemLink(item2_string);

                    } else {

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
                Log.e("", "Predicting Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("gender", gender);
                params.put("category", category);
                params.put("subcategory", subcategory);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_filter)
        {
            Intent filterIntent = new Intent(getApplicationContext(), FilterActivity.class);
            startActivityForResult(filterIntent, WEBVIEW_REQUEST);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WEBVIEW_REQUEST) {
            if(resultCode == RESULT_OK) {
                prediction(user.get("id"), user.get("gender"), categoryNameMapping(currentCategory), subcategoryNameMapping(currentSubCategory));
            }
        }
    }


}
