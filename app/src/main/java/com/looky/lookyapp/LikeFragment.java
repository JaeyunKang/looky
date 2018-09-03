package com.looky.lookyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.looky.lookyapp.MainActivity.activity;
import static com.looky.lookyapp.MainActivity.currentCategory;
import static com.looky.lookyapp.MainActivity.currentSubCategory;
import static com.looky.lookyapp.MainActivity.db;
import static com.looky.lookyapp.MainActivity.fab;
import static com.looky.lookyapp.MainActivity.likeTutorial;
import static com.looky.lookyapp.MainActivity.menuDelete;
import static com.looky.lookyapp.MainActivity.menuFilter;
import static com.looky.lookyapp.MainActivity.previousCategory;
import static com.looky.lookyapp.MainActivity.previousSubCategory;
import static com.looky.lookyapp.MainActivity.tutorial;
import static com.looky.lookyapp.MainActivity.user;
import static com.looky.utils.Utils.categoryNameMapping;
import static com.looky.utils.Utils.ratingDB;
import static com.looky.utils.Utils.subcategoryNameMapping;


public class LikeFragment extends Fragment {

    RelativeLayout firstTutorial;
    RelativeLayout secondTutorial;
    RelativeLayout thirdTutorial;
    Button completeTutorial;

    ArrayList<String> idList;
    ArrayList<String> nameList;
    ArrayList<String> linkList;

    LikeAdapter likeAdapter;
    GridView gridView;

    String currentId;
    RelativeLayout selectedImageLayout;
    ImageView selectedImageView;
    TextView nameView;
    TextView likeView;

    LinearLayout categoryVIew;
    TextView categoryText;
    TextView subcategoryText;

    LinearLayout descriptionView;

    public LikeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_like, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        likeTutorial = tutorial.get("tutorial_like");

        firstTutorial = (RelativeLayout) getView().findViewById(R.id.firstTutorial);
        secondTutorial = (RelativeLayout) getView().findViewById(R.id.secondTutorial);
        thirdTutorial = (RelativeLayout) getView().findViewById(R.id.thirdTutorial);
        completeTutorial = (Button) getView().findViewById(R.id.completeTutorial);

        if(likeTutorial.equals("0")) {
            firstTutorial.setVisibility(View.VISIBLE);
        }

        completeTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thirdTutorial.setVisibility(View.INVISIBLE);
                db.setTutorialLikeCompleted();
                likeTutorial = "1";
            }
        });

        categoryVIew = (LinearLayout) getView().findViewById(R.id.categoryView);
        categoryText = (TextView) getView().findViewById(R.id.categoryText);
        subcategoryText = (TextView) getView().findViewById(R.id.subcategoryText);

        categoryText.setText(currentCategory);
        subcategoryText.setText(currentSubCategory);

        descriptionView = (LinearLayout) getView().findViewById(R.id.descriptionView);

        setFilter(user.get("gender"), categoryNameMapping(currentCategory), subcategoryNameMapping(currentSubCategory));

        idList = new ArrayList<String>();
        nameList = new ArrayList<String>();
        linkList = new ArrayList<String>();

        gridView = (GridView) getView().findViewById(R.id.gridView);
        likeView = (TextView) getView().findViewById(R.id.likeView);
        nameView = (TextView) getView().findViewById(R.id.nameView);
        selectedImageLayout = (RelativeLayout) getView().findViewById(R.id.selectedImageLayout);
        selectedImageView = (ImageView) getView().findViewById(R.id.selectedImageView);


        selectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(likeTutorial.equals("0")) {
                    secondTutorial.setVisibility(View.INVISIBLE);
                    thirdTutorial.setVisibility(View.VISIBLE);
                }

                gridView.setVisibility(View.VISIBLE);
                categoryVIew.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);

                selectedImageLayout.setVisibility(View.INVISIBLE);
                nameView.setText("");
                likeView.setText("");
                fab.setVisibility(View.INVISIBLE);
                menuFilter.setVisible(true);
                menuDelete.setVisible(false);
            }
        });

    }


    private void setFilter(final String gender, final String category, final String subcategory) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FILTER_LIKE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        JSONArray jsonArray = jObj.getJSONArray("itemList");

                        if(jsonArray.length() == 0)
                        {
                            Toast.makeText(activity, "상품이 없어요!", Toast.LENGTH_LONG).show();
                            currentCategory = previousCategory;
                            currentSubCategory = previousSubCategory;
                            categoryText.setText(currentCategory);
                            subcategoryText.setText(currentSubCategory);
                            return;
                        }

                        idList.clear();
                        nameList.clear();
                        linkList.clear();

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            idList.add(jsonObject.getString("id"));
                            nameList.add(jsonObject.getString("name"));
                            linkList.add(jsonObject.getString("link"));
                        }

                        gridView.setVisibility(View.VISIBLE);
                        categoryVIew.setVisibility(View.VISIBLE);
                        descriptionView.setVisibility(View.VISIBLE);
                        selectedImageLayout.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.INVISIBLE);
                        menuDelete.setVisible(false);
                        menuFilter.setVisible(true);

                        nameView.setText("");
                        likeView.setText("");

                        likeAdapter = new LikeAdapter();

                        for(int i = 0 ; i < idList.size(); i++){
                            likeAdapter.addItem(new LikeItem(idList.get(i), nameList.get(i), linkList.get(i)));
                        }

                        gridView.setAdapter(likeAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                if(likeTutorial.equals("0")) {
                                    firstTutorial.setVisibility(View.INVISIBLE);
                                    secondTutorial.setVisibility(View.VISIBLE);
                                    thirdTutorial.setVisibility(View.INVISIBLE);
                                }

                                gridView.setVisibility(View.INVISIBLE);
                                categoryVIew.setVisibility(View.INVISIBLE);
                                descriptionView.setVisibility(View.INVISIBLE);
                                selectedImageLayout.setVisibility(View.VISIBLE);
                                fab.setVisibility(View.VISIBLE);
                                menuDelete.setVisible(true);
                                menuFilter.setVisible(false);

                                final LikeItem item = (LikeItem) likeAdapter.getItem(i);
                                currentId = item.getResId();
                                Glide.with(activity).load("https://s3.ap-northeast-2.amazonaws.com/looky/" +
                                        String.format("%09d", Integer.parseInt(item.getResId())) + ".jpg").centerCrop().into(selectedImageView);

                                nameView.setText(item.getName());
                                getLikeNum(item.getResId());


                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ratingDB(user.get("id"), item.getResId(), 2, "1");
                                        Intent shoppingIntent = new Intent(getApplicationContext(), WebviewActivity.class);
                                        shoppingIntent.putExtra("url", item.getLink());
                                        startActivity(shoppingIntent);
                                    }
                                });

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
                Log.e("", "Filtering Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("gender", gender);
                params.put("category", category);
                params.put("userId", user.get("id"));
                params.put("subcategory", subcategory);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "");
    }


    class LikeAdapter extends BaseAdapter {
        ArrayList<LikeItem> items = new ArrayList<LikeItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(LikeItem item) {
            items.add(item);
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup){

            LikeItemView view = new LikeItemView(getApplicationContext());
            LikeItem item = items.get(position);
            view.setImage(item.getResId());
            return view;
        }
    }

    private void getLikeNum(final String itemId) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_LIKE_NUM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String likeNum = jObj.getString("likeNum");
                        likeView.setText(likeNum);

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
                Log.e("", "Getting Error: " + error.getMessage());
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


    private void deleteLike(final String userId, final String itemId) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        setFilter(user.get("gender"), categoryNameMapping(currentCategory), subcategoryNameMapping(currentSubCategory));

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
                Log.e("", "Deleting Error: " + error.getMessage());
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
            startActivity(filterIntent);

            setFilter(user.get("gender"), categoryNameMapping(currentCategory), subcategoryNameMapping(currentSubCategory));
        }

        else if(id == R.id.menu_delete)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("삭제");
            builder.setMessage("아이템을 루키박스에서 삭제합니다");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteLike(user.get("id"), currentId);
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


}
