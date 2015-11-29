package com.example.myapplication;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.util.Calendar;
import java.util.GregorianCalendar;


import android.app.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import android.widget.DatePicker;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends Activity {


    int mYear, mMonth, mDay;

    TextView mTxtDate;

    EditText diary;

    Button btn;

    String fname;


    void UpdateNow() {

        mTxtDate.setText(String.format("%d/%d/%d", mYear,

                mMonth + 1, mDay));


    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        setTitle("간단일기장");

        mTxtDate = (TextView) findViewById(R.id.txtdate);
        diary = (EditText) findViewById(R.id.diary);
        btn = (Button) findViewById(R.id.btn);

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        UpdateNow();
    }

    public void mOnClick(View v) {

        switch (v.getId()) {

            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌

            case R.id.txtdate:

                //여기서 리스너도 등록함

                new DatePickerDialog(MainActivity.this, mDateSetListener, mYear,

                        mMonth, mDay).show();

                break;


        }

    }

    DatePickerDialog.OnDateSetListener mDateSetListener =

            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,

                                      int dayOfMonth) {

                    fname = Integer.toString(year) + "-" +

                            Integer.toString(monthOfYear) + "-" +

                            Integer.toString(dayOfMonth) + ".txt";

                    String str = readDiary(fname);

                    diary.setText(str);

                    btn.setEnabled(true);

                }

            };


    String readDiary(String fname) {

        String diarystr = null;

        FileInputStream inFs;

        try {

            inFs = openFileInput(fname);

            byte[] txt = new byte[500];

            inFs.read(txt);

            inFs.close();

            diarystr = (new String(txt)).trim();

            btn.setText("수정하기");

        } catch (IOException e) {

            diary.setHint("일기없음");

            btn.setText("새로 저장");

        }

        return diarystr;

    }

    btn.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {

            try {
                FileOutputStream outFs= new FileOutputStream();
                outFs = openFileOutput(fname,MODE_WORLD_WRITEABLE);
                String str = diary.getText().toString();
                outFs.write(str.getBytes());
                outFs.close();
                Toast.makeText(getApplicationContext(), fname+"이 저장됨", 0).show();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }





        }

    });

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.iReread) {

        } else if (id == R.id.iDelete) {
            openOptionsDialog();
        }
        else if (id == R.id.fLarge) {
            diary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
        }else if (id == R.id.fMid) {
            diary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        }else if (id == R.id.fSmall) {
            diary.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openOptionsDialog() {
        new AlertDialog.Builder(this).setTitle("Deleting?")
                .setMessage(.getText() + "삭제하시겠습니까?")
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "삭제가 취소되었습니다", Toast.LENGTH_SHORT).show();
                return;
            }
        })
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       File nowFile = new File(sdPath + "/diary" );
                        nowFile.delete();
                        diary.setText("");
                    }
                }).show();
    }



}
