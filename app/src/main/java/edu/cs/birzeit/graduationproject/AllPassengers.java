package edu.cs.birzeit.graduationproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class AllPassengers  {
    public ArrayList<Passenger> Passengers =new ArrayList<Passenger>();

    public AllPassengers() {
        getData();


    }

    public ArrayList<Passenger> getPassengers() {
        return Passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        Passengers = passengers;
    }

    @Override
    public String toString() {
        return "AllPassengers{}";
    }


    public void getData (){
        String url = "http://10.0.2.2/graduationProject/getPassengers.php";

        DownloadPassengerInformation runner = new DownloadPassengerInformation();
            runner.execute(url);

    }

    private InputStream OpenHttpConnectionGet(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private String DownloadText(String URL) {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnectionGet(URL);
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }
        return str;
    }


    public class DownloadPassengerInformation extends AsyncTask<String, Void, String> {


        @Override
        public String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        public void onPostExecute(String result) {
            String[] array = result.split("/");

            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1) {
                    String[] info = array[i].split(",");
                    Passenger passenger = new Passenger();
                    passenger.setID(Integer.parseInt(info[0].trim()));
                    passenger.setFull_name(info[1].trim());
                    passenger.setUsername(info[2].trim());
                    passenger.setPassword(info[3].trim());
                    passenger.setPhoneNumber(info[4].trim());
                    passenger.setScore(Integer.parseInt(info[5].trim()));
                    Passengers.add(passenger);
                }
            }






        }
    }



}
