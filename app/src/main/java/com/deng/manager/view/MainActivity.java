package com.deng.manager.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.fragment.AboutFragment;
import com.deng.manager.fragment.BackHandledFragment;
import com.deng.manager.fragment.DatabaseFragment;
import com.deng.manager.fragment.MessageFragment;
import com.deng.manager.fragment.PeopleFragment;
import com.deng.manager.fragment.TaskFragment;
import com.deng.manager.service.MessageService;

public class MainActivity extends AppCompatActivity implements BackHandledFragment.BackHandlerInterface {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private BackHandledFragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);

        //profile Image

        switchToMessage();
        startService(new Intent(this, MessageService.class));
    }

    private void setUpProfileImage(String name) {
        mNavigationView.removeHeaderView(mNavigationView.getHeaderView(0));
        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        TextView textView = (TextView) headerView.findViewById(R.id.tv_name);
        textView.setText(name);
        View profileView = headerView.findViewById(R.id.profile_image);
        if (profileView != null) {
            profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                }
            });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if (isLogin) {
            String name = sharedPreferences.getString("name", null);
            setUpProfileImage(name);
        } else {
            setUnLogin();
        }
    }

    private void setUnLogin() {
        mNavigationView.removeHeaderView(mNavigationView.getHeaderView(0));
        View inflateHeaderView = mNavigationView.inflateHeaderView(R.layout.navigation_header_unlogin);
        Button loginButton = (Button) inflateHeaderView.findViewById(R.id.login);
        Button registerButton = (Button) inflateHeaderView.findViewById(R.id.register);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }

    private void setupDrawerContent(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_message:
                        switchToMessage();
                        break;
                    case R.id.navigation_database:
                        switchToDatabase();
                        break;
                    case R.id.navigation_task:
                        switchToTask();
                        break;
                    case R.id.navigation_people:
                        switchToPeople();
                        break;
                    case R.id.navigation_about:
                        switchToAbout();
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void switchToPeople() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new PeopleFragment()).commit();
        mToolbar.setTitle(R.string.navigation_pepple);
    }

    private void switchToTask() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TaskFragment()).commit();
        mToolbar.setTitle(R.string.navigation_task);
    }

    private void switchToDatabase() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new DatabaseFragment()).commit();
        mToolbar.setTitle(R.string.navigation_database);
    }

    private void switchToMessage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new MessageFragment()).commit();
        mToolbar.setTitle(R.string.navigation_message);
    }

    private void switchToAbout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.selectedFragment = backHandledFragment;
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (MessageFragment.floatingMessageActionMenu.isOpened()) {
            MessageFragment.floatingMessageActionMenu.close(true);
            return;
        }
        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                doExitApp();
            }
        }

    }
}

