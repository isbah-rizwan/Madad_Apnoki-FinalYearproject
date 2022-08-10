package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalyearproject.fragment.ChiefHome;
import com.example.finalyearproject.fragment.NeedyHome;
import com.example.finalyearproject.fragment.NeedyPost;
import com.example.finalyearproject.fragment.NeedyProfile;
import com.example.finalyearproject.fragment.NeedyProfileUpdate;
import com.example.finalyearproject.fragment.NeedyShowDocuments;
import com.example.finalyearproject.fragment.RechargeWalletFragment;
import com.example.finalyearproject.fragment.SubHome;
import com.example.finalyearproject.fragment.SubPost;
import com.example.finalyearproject.fragment.SubProfile;
import com.example.finalyearproject.fragment.SubProfileUpdate;
import com.example.finalyearproject.fragment.SubShowCategory;
import com.example.finalyearproject.fragment.TransactionsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NeedyWall extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DrawerLayout drawer;
    BottomNavigationView btmNav;
    Toolbar toolbar;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needy_wall);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        btmNav = findViewById(R.id.bottom_nav);
        btmNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NeedyHome()).commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NeedyHome()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NeedyHome()).commit();
                toolbar.setVisibility(View.VISIBLE);
                btmNav.setVisibility(View.VISIBLE);
                break;

            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(NeedyWall.this, ChooseUser.class));
                Toast.makeText(NeedyWall.this, "Logout from profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete:
                deleteuser();
                break;

            case R.id.update:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NeedyProfileUpdate()).commit();
                toolbar.setVisibility(View.GONE);
                btmNav.setVisibility(View.GONE);
                break;

            case R.id.transactions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransactionsFragment()).commit();
                toolbar.setVisibility(View.GONE);
                btmNav.setVisibility(View.GONE);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteuser() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(NeedyWall.this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("This will delete your account from application");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NeedyWall.this, "Account Successfully deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NeedyWall.this, ChooseUser.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(NeedyWall.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.navigation_home:
                    selectedFragment = new NeedyHome();
                    toolbar.setVisibility(View.VISIBLE);
                    btmNav.setVisibility(View.VISIBLE);
                    break;

                case R.id.navigation_post:
                    selectedFragment = new NeedyPost();
                    toolbar.setVisibility(View.GONE);
                    btmNav.setVisibility(View.GONE);
                    break;

                case R.id.navigation_profile:
                    selectedFragment = new NeedyProfile();
                    toolbar.setVisibility(View.GONE);
                    btmNav.setVisibility(View.VISIBLE);
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                //    ChiefHome chiefHome=new ChiefHome();
                NeedyHome fragment = (NeedyHome) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragment.searchuser(s);
                //Querytext=s;
                return false;
            }

        });

        getMenuInflater().inflate( R.menu.radiobutton_menu, menu);
        MenuItem menu_ = menu.findItem( R.id.action_filter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.byname:
                Constants.Selected_filter="0";
                item.setChecked(true);
                break;

            case R.id.bycategory:
                Constants.Selected_filter="1";
                item.setChecked(true);
                break;

            case R.id.byamount:
                Constants.Selected_filter="2";
                item.setChecked(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}