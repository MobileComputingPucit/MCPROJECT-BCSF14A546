package com.example.deeju.stopwatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> listItems=new ArrayList<String>();
    ListView lv;
    int hours,minutes,secs;
    Button b1;
    StopWatchDatabaseHelper database;


    //Number of seconds displayed on the stopwatch.
    private int seconds = 0;
    //Is the stopwatch running?
    private boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=new StopWatchDatabaseHelper(this);
        setContentView(R.layout.activity_main);
        runTimer();
        lv = (ListView)findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        b1 = (Button)findViewById(R.id.start_button);
        b1.setText("Start");
        lv.setAdapter(adapter);
    }
    //Start the stopwatch running when the Start button is clicked.
    public void onClickStart(View view) {
        if (b1.getText()=="Start") {
            running = true;
            b1.setText("Pause");
        }
        else
        {
            running = false;
            b1.setText("Start");
        }
    }
    //Reset the stopwatch when the Reset button is clicked.
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        b1.setText("Start");
        listItems.clear();
        adapter.notifyDataSetChanged();
    }
    public void onClickLap(View view) {
        if(running) {
            adapter.insert(hours + ":" + minutes + ":" + secs, 0);
        }
        else {
            running = false;
        }
    }
    public void onClickSave(View view) {
        running=false;
        b1.setText("Start");
        //database.removeRows();
        database.insertTime(listItems);
        Collections.reverse(listItems);
    }

    public void onClickShow(View view) {
        running=false;
        b1.setText("Start");
        Cursor res = database.showRows();
        if (res.getCount()==0)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("No data stored.");
            showMessage("Data", buffer.toString());
        }
        else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                if (res.getInt(3) == 0) {
                    buffer.append("\nDate :" + res.getString(2) + "\n\n");
                    buffer.append(res.getString(1) + "\n");
                } else
                    buffer.append(res.getString(1) + "\n");
            }
            // Show all data
            showMessage("Data", buffer.toString());
        }

    }

    public void  onClickDelete(View view)
    {
        running=false;
        b1.setText("Start");
        boolean check=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete all data?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                database.removeRows();
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    //Sets the number of seconds on the timer.
    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                hours = seconds/3600;
                minutes = (seconds%3600)/60;
                secs = seconds%60;
                String time = String.format("%d:%02d:%02d",
                        hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}
