package com.looky.lookyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.looky.helper.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static com.looky.lookyapp.MainActivity.currentCategory;
import static com.looky.lookyapp.MainActivity.currentSubCategory;
import static com.looky.lookyapp.MainActivity.previousCategory;
import static com.looky.lookyapp.MainActivity.previousSubCategory;


public class FilterActivity extends AppCompatActivity {

    private SQLiteHandler db;

    HashMap<String, String> user;

    ImageButton closeButton;
    Button confirmButton;

    LinearLayout manCategory;
    LinearLayout womanCategory;

    LinearLayout manSubCategory;
    LinearLayout womanSubCategory;

    ArrayList<ToggleButton> manCategoryList;
    ArrayList<ToggleButton> womanCategoryList;

    ArrayList<LinearLayout> manSubCategoryList;
    ArrayList<LinearLayout> womanSubCategoryList;

    int currentGender;
    ArrayList<Integer> currentCategoryList;
    ArrayList<Integer> currentSubCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        closeButton = (ImageButton)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = new SQLiteHandler(getApplicationContext());

        user = db.getUserDetails();

        currentGender = Integer.parseInt(user.get("gender"));

        manCategory = (LinearLayout) findViewById(R.id.manCategory);
        womanCategory = (LinearLayout) findViewById(R.id.womanCategory);

        manSubCategory = (LinearLayout) findViewById(R.id.manSubCategory);
        womanSubCategory = (LinearLayout) findViewById(R.id.womanSubCategory);

        currentCategoryList = new ArrayList<Integer>();
        currentSubCategoryList = new ArrayList<Integer>();

        if(currentGender == 0) {
            manCategory.setVisibility(View.VISIBLE);
            womanCategory.setVisibility(View.GONE);
            manSubCategory.setVisibility(View.VISIBLE);
            womanSubCategory.setVisibility(View.GONE);
        }

        else {
            womanCategory.setVisibility(View.VISIBLE);
            manCategory.setVisibility(View.GONE);
            womanSubCategory.setVisibility(View.VISIBLE);
            manSubCategory.setVisibility(View.GONE);
        }

        manCategoryList = new ArrayList<ToggleButton>();
        womanCategoryList = new ArrayList<ToggleButton>();

        manSubCategoryList = new ArrayList<LinearLayout>();
        womanSubCategoryList = new ArrayList<LinearLayout>();

        manCategoryList.add((ToggleButton) findViewById(R.id.manOuter));
        manCategoryList.add((ToggleButton) findViewById(R.id.manTop));
        manCategoryList.add((ToggleButton) findViewById(R.id.manPants));
        manCategoryList.add((ToggleButton) findViewById(R.id.manShoes));
        manCategoryList.add((ToggleButton) findViewById(R.id.manBag));
        manCategoryList.add((ToggleButton) findViewById(R.id.manShirts));
        manCategoryList.add((ToggleButton) findViewById(R.id.manSuit));
        manCategoryList.add((ToggleButton) findViewById(R.id.manBeach));
        manCategoryList.add((ToggleButton) findViewById(R.id.manCouple));

        womanCategoryList.add((ToggleButton) findViewById(R.id.womanOuter));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanTop));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanShirts));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanPants));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanShoes));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanBag));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanSkirt));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanOnepiece));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanBeach));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanFitness));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanPajama));
        womanCategoryList.add((ToggleButton) findViewById(R.id.womanCouple));

        manSubCategoryList.add((LinearLayout) findViewById(R.id.manSubOuter));
        manSubCategoryList.add((LinearLayout) findViewById(R.id.manSubTop));
        manSubCategoryList.add((LinearLayout) findViewById(R.id.manSubPants));
        manSubCategoryList.add((LinearLayout) findViewById(R.id.manSubShoes));
        manSubCategoryList.add((LinearLayout) findViewById(R.id.manSubBag));

        womanSubCategoryList.add((LinearLayout) findViewById(R.id.womanSubOuter));
        womanSubCategoryList.add((LinearLayout) findViewById(R.id.womanSubTop));
        womanSubCategoryList.add((LinearLayout) findViewById(R.id.womanSubShirts));
        womanSubCategoryList.add((LinearLayout) findViewById(R.id.womanSubPants));
        womanSubCategoryList.add((LinearLayout) findViewById(R.id.womanSubShoes));
        womanSubCategoryList.add((LinearLayout) findViewById(R.id.womanSubBag));


        if(currentGender == 0) {
            for(int i=0; i < manCategoryList.size(); i++) {
                if(((String)manCategoryList.get(i).getText()).equals(currentCategory)) {
                    manCategoryList.get(i).setChecked(true);
                    if(i < 5) manSubCategoryList.get(i).setVisibility(View.VISIBLE);
                }
            }
        }
        else {
            for(int i=0; i < womanCategoryList.size(); i++) {
                if(((String)womanCategoryList.get(i).getText()).equals(currentCategory)) {
                    womanCategoryList.get(i).setChecked(true);
                    if(i < 6) womanSubCategoryList.get(i).setVisibility(View.VISIBLE);
                }
            }
        }

        final ArrayList<ToggleButton> toggleButtonList = new ArrayList<ToggleButton>();

        for(int i = 0; i < manSubCategoryList.size(); i++) {
            for (int j = 0; j < manSubCategoryList.get(i).getChildCount(); j++) {
                LinearLayout layout = (LinearLayout) manSubCategoryList.get(i).getChildAt(j);
                for (int k = 0; k < layout.getChildCount(); k++) {
                    View v = layout.getChildAt(k);
                    if (v instanceof ToggleButton) {
                        ToggleButton b = (ToggleButton) v;
                        toggleButtonList.add(b);
                    }
                }
            }
        }

        for(int i = 0; i < womanSubCategoryList.size(); i++) {
            for (int j = 0; j < womanSubCategoryList.get(i).getChildCount(); j++) {
                LinearLayout layout = (LinearLayout) womanSubCategoryList.get(i).getChildAt(j);
                for (int k = 0; k < layout.getChildCount(); k++) {
                    View v = layout.getChildAt(k);
                    if (v instanceof ToggleButton) {
                        ToggleButton b = (ToggleButton) v;
                        toggleButtonList.add(b);
                    }
                }
            }
        }

        for(int i=0; i < toggleButtonList.size(); i++) {
            if(((String)toggleButtonList.get(i).getText()).equals(currentCategory)) {
                toggleButtonList.get(i).setChecked(true);
            }
        }

        for(int i = 0 ; i < toggleButtonList.size(); i++) {
            final int categoryIndex = i;
            toggleButtonList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int j = 0; j < toggleButtonList.size(); j++) {
                        if (j != categoryIndex) {
                            toggleButtonList.get(j).setChecked(false);
                        }
                    }
                }
            });

            toggleButtonList.get(categoryIndex).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        currentSubCategoryList.add(categoryIndex);
                    }
                    else {
                        currentSubCategoryList.remove(new Integer(categoryIndex));
                    }
                }
            });
        }

        // ManCategory list
        for(int i = 0 ; i < manCategoryList.size(); i++) {
            final int categoryIndex = i;
            manCategoryList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int j = 0; j < manCategoryList.size(); j++) {
                        if (j != categoryIndex) {
                            manCategoryList.get(j).setChecked(false);

                            for(int k = 0 ; k < toggleButtonList.size(); k++) {
                               toggleButtonList.get(k).setChecked(false);
                            }
                        }
                    }
                }
            });

            // Showing Subcategories
            if(categoryIndex < 5) { // Outer, Top, Pants, Shoes, Bag

                manCategoryList.get(categoryIndex).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            manSubCategoryList.get(categoryIndex).setVisibility(View.VISIBLE);
                            currentCategoryList.add(categoryIndex);
                        }
                        else {
                            manSubCategoryList.get(categoryIndex).setVisibility(View.GONE);
                            currentCategoryList.remove(new Integer(categoryIndex));
                        }
                    }
                });
            }

            else { // Shirts, Suit, Beach, Couple
                manCategoryList.get(categoryIndex).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            currentCategoryList.add(categoryIndex);
                        }
                        else {
                            currentCategoryList.remove(new Integer(categoryIndex));
                        }
                    }
                });
            }
        }

        // Woman category list
        for(int i = 0 ; i < womanCategoryList.size(); i++) {
            final int categoryIndex = i;
            womanCategoryList.get(categoryIndex).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int j = 0; j < womanCategoryList.size(); j++) {
                        if (j != categoryIndex) {
                            womanCategoryList.get(j).setChecked(false);

                            for(int k = 0 ; k < toggleButtonList.size(); k++) {
                                toggleButtonList.get(k).setChecked(false);
                            }
                        }
                    }
                }
            });

            // Showing Subcategory
            if(categoryIndex < 6) { // Outer, Top, Shirts, Pants, Shoes, Bag

                womanCategoryList.get(categoryIndex).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            womanSubCategoryList.get(categoryIndex).setVisibility(View.VISIBLE);
                            currentCategoryList.add(categoryIndex);
                        }
                        else {
                            womanSubCategoryList.get(categoryIndex).setVisibility(View.GONE);
                            currentCategoryList.remove(new Integer(categoryIndex));
                        }
                    }
                });

            }

            else { // Skirt, Onepiece, Beach, Fitness, Pajama, Couple
                womanCategoryList.get(categoryIndex).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            currentCategoryList.add(categoryIndex);
                        }
                        else {
                            currentCategoryList.remove(new Integer(categoryIndex));
                        }
                    }
                });
            }

        }

        confirmButton = (Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                previousCategory = currentCategory;
                previousSubCategory = currentSubCategory;

                if(currentCategoryList.size() == 0) {
                    currentCategory = "모두보기";
                } else {
                    if(currentGender == 0) {
                        currentCategory = (String) manCategoryList.get(currentCategoryList.get(0)).getText();
                    }
                    else {
                        currentCategory = (String) womanCategoryList.get(currentCategoryList.get(0)).getText();
                    }

                }
                if(currentSubCategoryList.size() == 0) {
                    currentSubCategory = "모두보기";
                } else {
                    currentSubCategory = (String) toggleButtonList.get(currentSubCategoryList.get(0)).getText();
                }
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}
