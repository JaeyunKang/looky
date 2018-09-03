package com.looky.utils;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.looky.app.AppConfig;
import com.looky.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kangjaeyun on 2017. 6. 16..
 */

public class Utils {

    public static String categoryNameMapping(String category) {

        if(category.equals("아우터")) return "outer";
        else if(category.equals("상의")) return "top";
        else if(category.equals("셔츠")) return "shirts";
        else if(category.equals("바지")) return "pants";
        else if(category.equals("스커트")) return "skirt";
        else if(category.equals("원피스")) return "onepiece";
        else if(category.equals("수트")) return "suit";
        else if(category.equals("비치웨어")) return "beachwear";
        else if(category.equals("피트니스")) return "fitness";
        else if(category.equals("커플룩")) return "couplelook";
        else if(category.equals("슈즈")) return "shoes";
        else if(category.equals("가방")) return "bag";

        else return "all";

    }

    public static String subcategoryNameMapping(String subcategory) {

        if(subcategory.equals("가디건")) return "cardigan";
        else if(subcategory.equals("베스트")) return "vest";
        else if(subcategory.equals("집업")) return "zipup";
        else if(subcategory.equals("자켓")) return "jacket";
        else if(subcategory.equals("점퍼")) return "jumper";
        else if(subcategory.equals("야상/라쿤")) return "militaryJumper";
        else if(subcategory.equals("코트")) return "coat";
        else if(subcategory.equals("패딩")) return "padding";
        else if(subcategory.equals("티셔츠")) return "tee";
        else if(subcategory.equals("맨투맨")) return "mantoman";
        else if(subcategory.equals("후드티")) return "hoodTee";
        else if(subcategory.equals("니트")) return "knitTee";
        else if(subcategory.equals("폴라티")) return "turtleneckTee";
        else if(subcategory.equals("데님/청바지")) return "jeans";
        else if(subcategory.equals("면바지")) return "cottonPants";
        else if(subcategory.equals("슬랙스")) return "slacks";
        else if(subcategory.equals("트레이닝/조거")) return "trainingPants";
        else if(subcategory.equals("카고")) return "cargoPants";

        else if(subcategory.equals("뷔스티에")) return "bustierTee";
        else if(subcategory.equals("셔츠")) return "_shirts";
        else if(subcategory.equals("블라우스")) return "blouse";

        else return "all";

    }

    public static JSONArray shuffleJsonArray (JSONArray array) throws JSONException {
        // Implementing Fisher–Yates shuffle
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            Object object = array.get(j);
            array.put(j, array.get(i));
            array.put(i, object);
        }
        return array;
    }

    public static void ratingDB(final String userId, final String itemId,
                        final int rating, final String whereFrom) {

        String url_config = AppConfig.URL_RATING_DB;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url_config, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) { }
                    else {
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
                Log.e("", "Rating Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("itemId", itemId);
                params.put("rating", String.valueOf(rating));
                if(rating == 2) params.put("whereFrom", whereFrom);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "");
    }

}
