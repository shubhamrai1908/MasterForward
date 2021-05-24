package com.shubham.masterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
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
        name="Name";
        email="Email ID";
        phone="Phone Number";
        // Create a Cloud Storage reference from the app



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        uid = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("users").document(uid);




        Toolbar toolbar=findViewById(R.id.toolbar);

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
                    Picasso.get().load(link).into(photo);


                }catch (Exception e1)
                {
                    Toast.makeText(getApplicationContext(),e1.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

       nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_pending_req:
                        Toast.makeText(getApplicationContext(),"pending req",Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Pending Request");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_ground_rec:
                        Toast.makeText(getApplicationContext(),"Ground Received",Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Ground Request");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_server_sent:
                        Toast.makeText(getApplicationContext(),"Server Sent",Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Server Request");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_server_ack:
                        Toast.makeText(getApplicationContext(),"Server Reply",Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Server Reply");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_ground_ack:
                        Toast.makeText(getApplicationContext(),"Ground Ack",Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Ground Reply");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_sign_out:
                        Toast.makeText(getApplicationContext(),"Signed out successfully",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                        break;

                }
                return true;
            }
        });

    }
    public void getPhoto()
    {

    }
}