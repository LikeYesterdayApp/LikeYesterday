package com.example.likeyesterday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.likeyesterday.LoginSignup.StartActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.likeyesterday.LoginSignup.LoginWithEmail.mAuth;

public class HomeScreenActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.drawer_layout);

        NavigationView navigationview = findViewById(R.id.navigation);
        navigationview.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            navigationview.setCheckedItem(R.id.nav_profile);
        }


    }

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

//            if(getSupportFragmentManager().findFragmentById(R.id.fragment_container).getParentFragment()==R.layout.fragment_profile){
            if (doubleBackToExitPressedOnce) {
                    this.finishAffinity();
                    return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please press back again to exit", Toast.LENGTH_SHORT).show();

            mHandler.postDelayed(mRunnable, 2000);


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
//                Intent intent= new Intent(this,play_my_playlist.class);
//                startActivity(intent);
                break;
            case R.id.nav_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FriendsListFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

                break;
            case R.id.nav_my_places:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyPlacesMapsFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case R.id.nav_email_us:
                Intent emailIntent= new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                //mail to protocol that lets you mail using client installed on your device
                String[] to={"neetibisht919@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Like Yesterday App");
                emailIntent.setType("message/rfc822");
                //specification for email
                Intent chooser=Intent.createChooser(emailIntent,"Send Email");
                startActivity(chooser);
                break;
            case R.id.nav_share:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new music_library()).commit();
                break;
            case R.id.nav_profile:
                 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Log out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(HomeScreenActivity2.this," Successfully Logged Out",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent= new Intent(HomeScreenActivity2.this,StartActivity.class);
                                Log.i("Item selected","Log out");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}