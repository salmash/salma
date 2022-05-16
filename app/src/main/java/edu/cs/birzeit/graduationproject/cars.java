package edu.cs.birzeit.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class cars extends AppCompatActivity {

    private static ArrayList<carOOB> carsArray = new ArrayList<carOOB>();


    ArrayAdapter<carOOB> itemsAdapter;

//    ArrayList <carOOB> carsArray = new ArrayList<carOOB>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cars);

        getCars();


    }


    private void getCars() {
            String url = "http://10.0.2.2/graduationProject/getAllCars.php";
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        123);

            } else {
                DownloadCarInformation runner = new DownloadCarInformation();
                runner.execute(url);
            }
    }


    private InputStream OpenHttpConnection(String urlString) throws IOException {
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
            in = OpenHttpConnection(URL);
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


    private class DownloadCarInformation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
         protected void onPostExecute(String result) {
            String[] array = result.split("@");
            ArrayList <carOOB> savedCars = new ArrayList<>();

            for (int i = 0; i < array.length; i++) {
                if (i != array.length - 1) {
                    String[] info = array[i].split(",");
                    carOOB car = new carOOB();


//                    // Decompress the bytes
//                    Inflater inflater = new Inflater();
//                    inflater.setInput(new String(compressed_string).getBytes());
//                    byte[] result5 = new byte[1024];
//                    int resultLength = inflater.inflate(result5);
//                    inflater.end();
//
//                    // Decode the bytes into a String
//                    String message = new String(result5, 0, resultLength, "UTF-8");
//
//

                    car.setCarImage(info[0]);
                    car.setCar_type(info[1].trim());
                    car.setCar_price(Integer.parseInt(info[2].trim()));
                    car.setCar_provider(info[3].trim());
                    carsArray.add(car);

                }

            }

            RecyclerView recycler = (RecyclerView) findViewById(R.id.cars_recycler);

            String[] cars_Images = new String[carsArray.size()];
            String[] cars_types = new String[carsArray.size()];
            int[] cars_prices = new int[carsArray.size()];
            String [] cars_providers = new String [carsArray.size()];

            for(int i = 0; i<carsArray.size();i++){
                cars_Images[i] = carsArray.get(i).getCarImage();
                cars_types[i] = carsArray.get(i).getCar_type();
                cars_prices[i] = carsArray.get(i).getCar_price();
                cars_providers[i] = carsArray.get(i).getCar_provider();
            }

            recycler.setLayoutManager(new LinearLayoutManager(cars.this));
            carsAdapter adapter = new carsAdapter(cars_Images, cars_types, cars_prices,cars_providers);
            recycler.setAdapter(adapter);





//            System.out.println("adapter attached"+cars_types[1]);


        }


    }
}