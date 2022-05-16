package edu.cs.birzeit.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class chargePoints extends AppCompatActivity {

    EditText passengerID;
    EditText pointsToCharge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_points);

        passengerID = findViewById(R.id.passengerID);
        pointsToCharge = findViewById(R.id.pointsToCharge);



    }


    public void chargePoints(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String restUrl = "http://10.0.2.2/graduationProject/updatePassengerScore.php";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    123);

        } else {
            SendUpdateRequest runner = new SendUpdateRequest();
            runner.execute(restUrl);
        }
    }

//    Passenger getPassengerData (){
//        Passenger passenger = new Passenger();
//        passenger.setID(Integer.parseInt(passengerID.getText().toString().trim()));
//        int score = passenger.getScore()+Integer.parseInt(pointsToCharge.getText().toString().trim());
//        System.out.println(score);
//        passenger.setScore(score);
//
//        return passenger;
//    }

    private String processRequest(String restUrl) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("ID", "UTF-8") + "="
                + URLEncoder.encode(passengerID.getText().toString().trim(), "UTF-8");

        data += "&" + URLEncoder.encode("score", "UTF-8") + "="
                + URLEncoder.encode(pointsToCharge.getText().toString().trim(), "UTF-8");


        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(restUrl);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        // Show response on activity
        return text;

    }

    private class SendUpdateRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return processRequest(urls[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("result : "+result);
            if (result.trim().equals("failed")) {
                Toast.makeText(chargePoints.this, "رقم الهوية غير موجود", Toast.LENGTH_LONG).show();
                passengerID.setError("الرجاء إدخال رقم هوية صحيح");

            } else {
                Toast.makeText(chargePoints.this, "تم إضافة النقاط الى رصيد المستخدم بنجاح", Toast.LENGTH_LONG).show();
            }

        }
    }

}