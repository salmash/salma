package edu.cs.birzeit.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class tickets_information extends AppCompatActivity {

    TextView score ;
    TextView numberText ;
    ImageView barcode;
    LinearLayout info ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets_information);

        score = findViewById(R.id.score);
        numberText = findViewById(R.id.number);
        barcode = findViewById(R.id.barcode);
        info = findViewById(R.id.info);

        Intent intent = getIntent();
        String scoreData = intent.getStringExtra("score");
        System.out.println(scoreData);
        score.setText(" "+ scoreData );

        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        numberText.setText(number+"");


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(numberText.getText()+"", BarcodeFormat.CODABAR,235,70);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            barcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }




    }

    public void saveOnClick(View view) {
        FileOutputStream fileOutputStream=null;
        File file=getdisc();
        if (!file.exists() && !file.mkdirs())
        {
            Toast.makeText(getApplicationContext(),"sorry can not make dir",Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="barcode"+date+".jpeg";
        String file_name=file.getAbsolutePath()+"/"+name; File new_file=new File(file_name);
        try {
            fileOutputStream =new FileOutputStream(new_file);
            Bitmap bitmap=viewToBitmap(info,info.getWidth(),info.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(getApplicationContext(),"تم حفظ المعلومات في الصور بنجاح", Toast.LENGTH_LONG).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch
        (FileNotFoundException e) {

        } catch (IOException e) {

        } refreshGallary(file);
    } private void refreshGallary(File file)
    { Intent i=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(file)); sendBroadcast(i);
    }
    private File getdisc(){
        File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"My Image");
    }


    private static Bitmap viewToBitmap(View view, int width, int height)
    {
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap); view.draw(canvas);
        return bitmap;
    }

    }


