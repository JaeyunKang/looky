package com.looky.lookyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.looky.helper.SQLiteHandler;
import com.looky.helper.SessionManager;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.looky.lookyapp.MainActivity.activity;
import static com.looky.lookyapp.MainActivity.db;
import static com.looky.lookyapp.MainActivity.fab;
import static com.looky.lookyapp.MainActivity.user;


public class SettingFragment extends Fragment {

    private SessionManager session;

    private TextView txtName;
    private TextView logout;
    private TextView send;


    public void onStart() {

        super.onStart();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        user = db.getUserDetails();
        String name = user.get("name");
        txtName = (TextView) activity.findViewById(R.id.nameView);
        txtName.setText(name);

        fab.setVisibility(View.INVISIBLE);

        logout = (TextView) activity.findViewById(R.id.logout);
        send = (TextView) activity.findViewById(R.id.send);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SendActivity.class);
                startActivity(intent);
            }
        });
    }


    public SettingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    public void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        LoginManager.getInstance().logOut();

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

            }
        });

        // Launching the login activity
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
    }


}


