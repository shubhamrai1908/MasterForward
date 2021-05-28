package com.shubham.masterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity {
    NavigationView nav;
    View headerView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uid,name,email,phone,link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        name="Name";
        email="Email ID";
        phone="Phone Number";
        link="";
        // Create a Cloud Storage reference from the app



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        uid = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("users").document(uid);




        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);

        nav=findViewById(R.id.navmenu);
        headerView=nav.getHeaderView(0);
        TextView Name=headerView.findViewById(R.id.name);
        TextView Email=headerView.findViewById(R.id.email);
        TextView Phone=headerView.findViewById(R.id.phone);
        ImageView photo=headerView.findViewById(R.id.photo);
        Name.setText(name);
        Email.setText(email);
        Phone.setText(phone);

        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                try {
                    name = documentSnapshot.getString("Name");
                    email = documentSnapshot.getString("Email");
                    phone = documentSnapshot.getString("Phone");
                    link = documentSnapshot.getString("Photo");
                    Name.setText(name);
                    Email.setText(email);
                    Phone.setText(phone);
                    if(link.length()!=0)
                    Picasso.get().load(link).into(photo);


                }catch (Exception e1)
                {
                    //Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
        Fragment fragment=new DashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.start_service:
                        startService();
                        Toast.makeText(getApplicationContext(),"Background Service started",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.stop_service:
                        stopService();
                        Toast.makeText(getApplicationContext(),"Background Service stopped",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;

            }
        });


       nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           Fragment fragment=new DashboardFragment();
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.menu_pending_req:
                        fragment=new FirstFragment();
                        toolbar.setTitle("Pending Request");
                        break;
                    case R.id.menu_ground_rec:
                        fragment=new SecondFragment();
                        toolbar.setTitle("Ground Request");
                        break;
                    case R.id.menu_server_sent:
                        fragment=new ThirdFragment();
                        toolbar.setTitle("Server Request");
                        break;
                    case R.id.menu_server_ack:
                        fragment=new ForthFragment();
                        toolbar.setTitle("Server Reply");
                        break;
                    case R.id.menu_ground_ack:
                        fragment=new FifthFragment();
                        toolbar.setTitle("Ground Reply");
                        break;
                    case R.id.menu_sign_out:
                        logOut();




                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    public void logOut()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.master);
        builder.setMessage("Do you want to Sign Out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Signed out successfully",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "MasterApp is Active in Background");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.master);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}