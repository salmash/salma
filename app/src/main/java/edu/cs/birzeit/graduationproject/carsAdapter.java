package edu.cs.birzeit.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class carsAdapter extends RecyclerView.Adapter<carsAdapter.ViewHolder>  {

    private String [] carsImages;
    private String[]carsTypes;
    private int[] carsPrices;
    private String[]carsProviders;


    public carsAdapter( String []  carsImages,String[] carsTypes, int[] carsPrices, String[] carsProviders) {
        this.carsImages = carsImages;
        this.carsTypes = carsTypes;
        this.carsPrices = carsPrices;
        this.carsProviders = carsProviders;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.car,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
//        carOOB car = new carOOB();

//        for(int i = 0; i< items.length ; i++){
//            if(items[i].getImageID()==imageIds[position]){
//                item = items[i];
//            }
//        }
//
//        ImageView imageView = (ImageView) cardView.findViewById(R.id.image);
//        Drawable dr = ContextCompat.getDrawable(cardView.getContext(), car.getCarImage());
//        imageView.setImageDrawable(dr);


        ImageView car_image = (ImageView)cardView.findViewById(R.id.car_image);
        // decode base64 string
        byte[] bytes= Base64.decode(carsImages[position],Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        // set bitmap on imageView
        car_image.setImageBitmap(bitmap);

        TextView txt_carType = (TextView)cardView.findViewById(R.id.car_type);
        txt_carType.setText( carsTypes[position]);

        TextView txt_carPrice = (TextView)cardView.findViewById(R.id.car_price);
        txt_carPrice.setText(" السعر : "+carsPrices[position] +" دينار أردني لليوم الواحد");

        TextView txt_carProvider = (TextView)cardView.findViewById(R.id.car_provider);
        txt_carProvider.setText("اسم المزود : "+carsProviders[position] );

//        carOOB finalCar = car;
    }

    @Override
    public int getItemCount() {
        return carsTypes.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }

}
