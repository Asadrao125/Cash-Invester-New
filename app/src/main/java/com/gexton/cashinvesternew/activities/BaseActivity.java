package com.gexton.cashinvesternew.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gexton.cashinvesternew.R;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class BaseActivity extends AppCompatActivity {
    protected FrameLayout frameLayout;
    protected ListView mDrawerList;
    protected String[] listArray = {"Dashbord", "Find Property", "My Properties", "My Contacts",
            "Messages", "Saved Searches", "My Profile", "Support Tickets", "Bookmarks", "Logout"};
    protected static int position;
    private static boolean isLaunch = true;
    private DrawerLayout mDrawerLayout;
    LinearLayout contentFrame;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        contentFrame = findViewById(R.id.contentFrame);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listArray));
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(position);
            }
        });

        Toolbar toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.Open, R.string.Close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(getString(R.string.app_name));
                getSupportActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                contentFrame.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        if (isLaunch) {
            isLaunch = false;
            openActivity(0);
        }
    }

    protected void openActivity(int position) {
        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position;
        switch (position) {
            case 0:
                startActivity(new Intent(this, DashbordActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, FindPropertyActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, MyPropertiesActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, MyContactsActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, MessagesActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, SavedSearchesActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
            case 7:
                startActivity(new Intent(this, SupportTicketsActivity.class));
                break;
            case 8:
                startActivity(new Intent(this, BookmarkedPropertiesActivity.class));
                break;
            case 9:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.addProperty:
                startActivity(new Intent(this, AddPropertyActivity.class));
                return true;
            case R.id.customerSupport:
                return true;
            case R.id.notification:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /*  @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        } /*else if(drawer_layout2.isDrawerOpen(Gravity.RIGHT)){
            drawer_layout2.closeDrawers();
            return;
        }*/
        super.onBackPressed();
    }
}
