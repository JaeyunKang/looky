package com.looky.lookyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.looky.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.looky.lookyapp.MainActivity.activity;
import static com.looky.lookyapp.MainActivity.chartTutorial;
import static com.looky.lookyapp.MainActivity.currentCategory;
import static com.looky.lookyapp.MainActivity.currentSubCategory;
import static com.looky.lookyapp.MainActivity.db;
import static com.looky.lookyapp.MainActivity.fab;
import static com.looky.lookyapp.MainActivity.menuFilter;
import static com.looky.lookyapp.MainActivity.previousCategory;
import static com.looky.lookyapp.MainActivity.previousSubCategory;
import static com.looky.lookyapp.MainActivity.tutorial;
import static com.looky.lookyapp.MainActivity.user;
import static com.looky.utils.Utils.categoryNameMapping;
import static com.looky.utils.Utils.ratingDB;
import static com.looky.utils.Utils.subcategoryNameMapping;


public class ChartFragment extends Fragment {

    RelativeLayout firstTutorial;
    RelativeLayout secondTutorial;
    RelativeLayout thirdTutorial;
    Button completeTutorial;

    ArrayList<String> idList;
    ArrayList<String> nameList;
    ArrayList<String> priceList;
    ArrayList<String> likeList;
    ArrayList<String> linkList;

    ChartAdapter chartAdapter;
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


    public ChartFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        db = new SQLiteHandler(getApplicationContext());

        user = db.getUserDetails();
        tutorial = db.getTutorialDetails();

        fab.setVisibility(View.INVISIBLE);

        chartTutorial = tutorial.get("tutorial_chart");

        firstTutorial = (RelativeLayout) getView().findViewById(R.id.firstTutorial);
        secondTutorial = (RelativeLayout) getView().findViewById(R.id.secondTutorial);
        thirdTutorial = (RelativeLayout) getView().findViewById(R.id.thirdTutorial);
        completeTutorial = (Button) getView().findViewById(R.id.completeTutorial);

        if(chartTutorial.equals("0")) {
            firstTutorial.setVisibility(View.VISIBLE);
        }

        completeTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thirdTutorial.setVisibility(View.INVISIBLE);
                db.setTutorialChartCompleted();
                chartTutorial = "1";
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
        priceList = new ArrayList<String>();
        linkList = new ArrayList<String>();
        likeList = new ArrayList<String>();


        gridView = (GridView) getView().findViewById(R.id.gridView);
        nameView = (TextView) getView().findViewById(R.id.nameView);
        likeView = (TextView) getView().findViewById(R.id.likeView);
        selectedImageLayout = (RelativeLayout) getView().findViewById(R.id.selectedImageLayout);
        selectedImageView = (ImageView) getView().findViewById(R.id.selectedImageView);



        selectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chartTutorial.equals("0")) {
                    secondTutorial.setVisibility(View.INVISIBLE);
                    thirdTutorial.setVisibility(View.VISIBLE);
                }

                gridView.setVisibility(View.VISIBLE);
                categoryVIew.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.VISIBLE);
                selectedImageLayout.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                nameView.setText("");
                likeView.setText("");

                menuFilter.setVisible(true);
            }
        });
    }

    private void setFilter(final String gender, final String category, final String subcategory) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FILTER_CHART, new Response.Listener<String>() {

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
                        priceList.clear();
                        linkList.clear();
                        likeList.clear();

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            idList.add(jsonObject.getString("id"));
                            nameList.add(jsonObject.getString("name"));
                            priceList.add(jsonObject.getString("price") + "원");
                            linkList.add(jsonObject.getString("link"));
                            likeList.add(jsonObject.getString("like"));
                        }

                        gridView.setVisibility(View.VISIBLE);
                        categoryVIew.setVisibility(View.VISIBLE);
                        descriptionView.setVisibility(View.VISIBLE);
                        selectedImageLayout.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.INVISIBLE);

                        nameView.setText("");
                        likeView.setText("");

                        chartAdapter = new ChartAdapter();


                        for(int i = 0 ; i < idList.size(); i++){
                            chartAdapter.addItem(new ChartItem(idList.get(i), nameList.get(i), priceList.get(i), linkList.get(i), likeList.get(i)));
                        }

                        gridView.setAdapter(chartAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                if(chartTutorial.equals("0")) {
                                    firstTutorial.setVisibility(View.INVISIBLE);
                                    secondTutorial.setVisibility(View.VISIBLE);
                                    thirdTutorial.setVisibility(View.INVISIBLE);
                                }

                                gridView.setVisibility(View.INVISIBLE);
                                categoryVIew.setVisibility(View.INVISIBLE);
                                descriptionView.setVisibility(View.INVISIBLE);
                                selectedImageLayout.setVisibility(View.VISIBLE);
                                fab.setVisibility(View.VISIBLE);
                                menuFilter.setVisible(false);

                                final ChartItem item = (ChartItem) chartAdapter.getItem(i);
                                currentId = item.getResId();
                                Glide.with(activity).load("https://s3.ap-northeast-2.amazonaws.com/looky/" +
                                        String.format("%09d", Integer.parseInt(item.getResId()))  + ".jpg").centerCrop().into(selectedImageView);

                                nameView.setText(item.getName());
                                likeView.setText(item.getLike());

                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ratingDB(user.get("id"), item.getResId(), 2, "2");
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
                params.put("subcategory", subcategory);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "");
    }


    class ChartAdapter extends BaseAdapter {
        ArrayList<ChartItem> items = new ArrayList<ChartItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ChartItem item) {
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

            ChartItemView view = new ChartItemView(getApplicationContext());
            ChartItem item = items.get(position);
            view.setImage(item.getResId());
            view.setRank(position + 1);
            view.setName(item.getName());
            view.setPrice(item.getPrice());
            return view;
        }
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

        return super.onOptionsItemSelected(item);
    }


}
