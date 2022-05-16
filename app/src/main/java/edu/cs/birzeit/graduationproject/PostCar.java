package edu.cs.birzeit.graduationproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.zip.GZIPOutputStream;
import java.util.zip.*;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.google.zxing.client.android.Intents.Scan.RESULT;

public class PostCar extends AppCompatActivity {
    byte compressed_string[]  = new byte[1024];
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageToUpload ;
    Button uploadPhoto;
    EditText car_type;
    EditText car_price;
    EditText car_provider;
    Button post_car;
    Bitmap bitmap;
    String sImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_car);
        setUpViews();

    }

    private void setUpViews() {
        imageToUpload = findViewById(R.id.imageToUpload);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        car_type = findViewById(R.id.car_type);
        car_price = findViewById(R.id.car_price);
        car_provider = findViewById(R.id.car_provider);
        post_car = findViewById(R.id.post_car);
    }

    public void uploadPhotoOnClick(View view) {
//        Intent intent = new Intent(this, cars.class);
//        startActivity(intent);


        if (ContextCompat.checkSelfPermission(PostCar.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(PostCar.this
                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
        else
        {
            selectImage();
        }








//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);



    }

    private void selectImage() {
        // clear previous data
        imageToUpload.setImageBitmap(null);
        // Initialize intent
        Intent intent=new Intent(Intent.ACTION_PICK);
        // set type
        intent.setType("image/*");
        // start activity result
        startActivityForResult(Intent.createChooser(intent,"Select Image"),100);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check condition
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            // when permission
            // is granted
            // call method
            selectImage();
        }
        else
        {
            // when permission is denied
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check condition
        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            // when result is ok
            // initialize uri
            Uri uri=data.getData();
            // Initialize bitmap
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                imageToUpload.setImageBitmap(bitmap);
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

//                Deflater new_deflater = new Deflater();
//                new_deflater.setInput(sImage.getBytes("UTF-8"));
//                new_deflater.finish();
//
//                // Storing the compressed string data in compressed_string. the size for compressed string will be 13
//                int compressed_size = new_deflater.deflate(compressed_string, 5, 15, Deflater.FULL_FLUSH);
//                // The compressed String
//                System.out.println("The Compressed String Output: " + new String(compressed_string) + "\n Size: " + compressed_size);
//                //The Original String







            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void post_car_onClick(View view) {

        System.out.println(sImage);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String restUrl = "http://10.0.2.2/graduationProject/postCar.php";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    123);

        } else {
            SendPostRequest runner = new SendPostRequest();
            runner.execute(restUrl);
        }
    }

    carOOB getCarData (){
        carOOB car = new carOOB();
        car.setCarImage(sImage);
        car.setCar_type(car_type.getText().toString().trim());
        car.setCar_price(Integer.parseInt(car_price.getText().toString().trim()));
        car.setCar_provider(car_provider.getText().toString().trim());


        return car;
    }

    private String processRequest(String restUrl) throws UnsupportedEncodingException {
        carOOB car = getCarData();

        String data = URLEncoder.encode("car_image", "UTF-8")
                + "=" + URLEncoder.encode(car.getCarImage()+"", "UTF-8");

        data += "&" + URLEncoder.encode("car_type", "UTF-8") + "="
                + URLEncoder.encode(car.getCar_type(), "UTF-8");

        data += "&" + URLEncoder.encode("car_price", "UTF-8") + "="
                + URLEncoder.encode(car.getCar_price() +"", "UTF-8");

        data += "&" + URLEncoder.encode("car_provider", "UTF-8") + "="
                + URLEncoder.encode(car.getCar_provider(), "UTF-8");


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

    private class SendPostRequest extends AsyncTask<String, Void, String> {
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
            Toast.makeText(PostCar.this, "تم اضافة السيارة بنجاح", Toast.LENGTH_LONG).show();
        }
    }

}