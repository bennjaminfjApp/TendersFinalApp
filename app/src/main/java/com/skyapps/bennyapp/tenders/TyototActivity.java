package com.skyapps.bennyapp.tenders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.Objects.Item;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.support.ManageActivity;
import com.skyapps.bennyapp.support.SupportedActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TyototActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListTyotaAdapter listAdapter;
    ExpandableListView listTyotot;
    List<Tender> listDataHeader;
    HashMap<Tender, List<Item>> listDataChild;

    private ProgressDialog mProgressDialog;

    private int lastPosition = -1;
    private DrawerLayout drawer;
    private TextView name_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyotot);

        listTyotot = findViewById(R.id.listTyotot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_foreground);
        setSupportActionBar(toolbar);

        if (getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("activity" , "").equals("tabs")){
            finish();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(Gravity.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        name_user = navigationView.getHeaderView(0).findViewById(R.id.name_user);
        name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));


        listDataHeader = new ArrayList<Tender>();
        listDataChild = new HashMap<Tender, List<Item>>();

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final String username = getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "");
        mProgressDialog = new ProgressDialog(TyototActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listDataChild.clear();
                listDataHeader.clear();

                        int i = 0;
                        DataSnapshot dataSnapshot2 = snapshot.child("users").child(username).child("Tyotot");


                        for(DataSnapshot postSnapshot : dataSnapshot2.getChildren()){

                            listDataHeader.add(new Tender(snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(postSnapshot.getKey()).child("mqt").getValue()+"",
                                    postSnapshot.getKey(), snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(postSnapshot.getKey()).child("name").getValue()+"",
                                    (long)snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(postSnapshot.getKey()).child("Info").child("timer").getValue()));
                            List<Item> list = new ArrayList<Item>();
                            list.add(new Item(postSnapshot.getKey()+"",
                                    snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(postSnapshot.getKey()).child("contact").getValue()+"",
                                    snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(postSnapshot.getKey()).child("phone").getValue()+"" ,
                                    snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(postSnapshot.getKey()).child("mail").getValue()+""));
                            listDataChild.put(listDataHeader.get(i), list); // Header, Child data
                            i++;
                        }

                        listAdapter = new ExpandableListTyotaAdapter(TyototActivity.this, listDataHeader, listDataChild);
                        listTyotot.setAdapter(listAdapter);


                        mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        listTyotot.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastPosition != -1
                        && groupPosition != lastPosition) {
                    listTyotot.collapseGroup(lastPosition);
                }
                lastPosition = groupPosition;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        drawer.openDrawer(Gravity.START);
        return true;

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_tenders) {
            startActivity(new Intent(this , TendersActivity.class));finish();
            getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putString("activity" , "").commit();
        } else if (id == R.id.tyotot) {
            drawer.closeDrawer(Gravity.START);
        } else if (id == R.id.my_tenders) {
            startActivity(new Intent(this , MyTendersActivity.class)); finish();

        } else if (id == R.id.win_tenders) {
            startActivity(new Intent(this , WinTendersActivity.class)); finish();

        } else if (id == R.id.support) {
            startActivity(new Intent(this , SupportedActivity.class)); finish();

        } else if (id == R.id.manage) {
            startActivity(new Intent(this , ManageActivity.class)); finish();

        } else if (id == R.id.disconnect){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("התנתקות מהמשתמש");
            builder.setMessage("האם אתה בטוח שברצונך להתנתק מהמשתמש?");
            builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(TyototActivity.this, "התנתקת בהצלחה", Toast.LENGTH_LONG).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(TyototActivity.this , MainActivity.class)); finish();
                }
            });
            builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
