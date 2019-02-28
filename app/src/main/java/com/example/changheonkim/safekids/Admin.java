package com.example.changheonkim.safekids;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Admin extends AppCompatActivity {

    //value
    int StudentNum = 4;
    String[] StudentName = {"김창헌", "이동수", "김현우", "지호준"};



    //widget
    TextView studentNumber;
    Context context = this;
    TableLayout studentlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userAdminName = intent.getStringExtra("userAdminName");
        String userEmail = intent.getStringExtra("userEmail");
        String userPhoneNum = intent.getStringExtra("userPhoneNum");
        String userPw = intent.getStringExtra("userPw");
        TextView idText = (TextView)findViewById(R.id.userId);
        Button managementButton = (Button)findViewById(R.id.managementButton);

        idText.setText(userAdminName+"("+userID+")"+"님 환영합니다.");
        if(!userID.equals("admin")){
            managementButton.setVisibility(View.GONE);
        }
        //기본 값 생성 : 학생 1
        studentNumber = (TextView)findViewById(R.id.studentNumber);
        context = this;

        String StudentNum_string = "학생 수 : " + Integer.toString(StudentNum);

        studentNumber.setText(StudentNum_string);
        makeAdminRow();

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackkgroundTask().execute();
            }
        });
    }

    class BackkgroundTask extends AsyncTask<Void, Void, String>{
        String target;

        @Override
        protected void onPreExecute(){
            target = "http://diablo827.cafe24.com/List.php";
        }
        @Override
        protected String doInBackground(Void...voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch(Exception e){
                e.getStackTrace();
            }
            return null;
        }
        @Override
        public void onProgressUpdate(Void...values){
            super .onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(Admin.this, ManagementActivity.class);
            intent.putExtra("userList", result);
            Admin.this.startActivity(intent);
        }



    }

    //StudentNum의 값에 따른 StudentRow를 동적생성
    public void makeAdminRow() {
        studentlayout = (TableLayout)findViewById(R.id.studentLayout);
        for (int i = 1; i <= StudentNum; i++) {
            //widget
            TableRow tr = new TableRow(context);
            TextView studentname = new TextView(context);
            Spinner studentSpinner = new Spinner(context);

            //spinner setting
            studentSpinner.setSelection(0);
            ArrayAdapter studentAdapter = ArrayAdapter.createFromResource(this,R.array.admin,android.R.layout.simple_spinner_item);
            studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            studentSpinner.setAdapter(studentAdapter);

            //parameters setting
            TableRow.LayoutParams trlptv = new TableRow.LayoutParams(800,TableRow.LayoutParams.WRAP_CONTENT,2);
            TableRow.LayoutParams trlpsp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1);
            TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            tllp.gravity = Gravity.CENTER;
            final int bottom = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            tllp.bottomMargin = bottom;
            studentname.setText("학 생 이 름 : " + StudentName[i-1]);
            studentname.setGravity(Gravity.CENTER);
            studentname.setTextSize(20);
            studentname.setLayoutParams(tllp);

            //generate
            tr.addView(studentname,trlptv);
            tr.addView(studentSpinner,trlpsp);
            studentlayout.addView(tr,tllp);
        }
    }
}
