package com.skyapps.bennyapp.tenders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.skyapps.bennyapp.Objects.Item;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.tabs.TabsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExpandableListTyotaAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Tender> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Tender, List<Item>> _listDataChild;

    private int count = 0 ;
    private int countAll = 5;

    public ExpandableListTyotaAdapter(Context context, List<Tender> listDataHeader,
                                      HashMap<Tender, List<Item>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Item item = (Item) getChild(groupPosition , childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item2, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView email = (TextView) convertView.findViewById(R.id.email);


        name.setText(item.getName());
        phone.setText(item.getPhone());
        email.setText(item.getEmail());

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", item.getPhone(), null));
                _context.startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",item.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "מעוניין במכרז " + item.getName());
                _context.startActivity(Intent.createChooser(emailIntent, "Send email..."));;
            }
        });

        convertView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_context,TabsActivity.class);
                i.putExtra("name" , item.getName());
                _context.getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putString("company" , item.getCompany()).commit();
                _context.startActivity(i);
            }
        });

        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase.setAndroidContext(_context);
                final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/").child("users").child(_context.getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""));

                myFirebaseRef.child("Tyotot").child("Tyotot").child(item.getCompany()).removeValue();
                myFirebaseRef.child("Tyotot").child(item.getCompany()).removeValue();
                myFirebaseRef.child("Tyotot").child(item.getCompany()+"Image").removeValue();
                myFirebaseRef.child(item.getCompany()).removeValue();

                _context.getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putString("activity" , "tabs1").commit();
                Toast.makeText(_context, "הטיוטה נמחקה!", Toast.LENGTH_SHORT).show();

            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Tender tender = (Tender) getGroup(groupPosition);
        Log.e("count" , count+"");
        count++;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView masad = (TextView) convertView.findViewById(R.id.masad);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView project = (TextView) convertView.findViewById(R.id.project);
        final TextView time = (TextView) convertView.findViewById(R.id.time);


        masad.setText(tender.getMasad());
        name.setText(tender.getName());
        project.setText(tender.getProject());

        //Log.e("isExpanded" , isExpanded+"");

        if (time.getText().toString().equals("שעה ותאריך")) {
            new CountDownTimer(tender.getTime(), 1000) {

                public void onTick(long millisUntilFinished) {
                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                    if (days == 0) {
                        if (hours == 0) {
                            time.setText(minutes + ":" + seconds);
                        } else {
                            time.setText(hours + ":" + minutes + ":" + seconds);
                        }
                    } else if (hours == 0) {
                        time.setText(minutes + ":" + seconds);
                    } else {
                        time.setText(days + " ימים , " + hours + ":" + minutes + ":" + seconds);
                    }

                }

                public void onFinish() {
                    //mTextField.setText("done!");
                }

            }.start();
        }





        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
