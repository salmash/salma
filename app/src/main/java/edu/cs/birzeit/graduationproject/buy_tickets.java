package edu.cs.birzeit.graduationproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class buy_tickets extends AppCompatActivity {
    Passenger passenger;
    TextView score;
    NumberPicker bags_num ;
    Button bags_price;
    Button know_total;
    TextView total_cost;
    int bagsPr;
    int total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buy_tickets);




        System.out.println(new AllPassengers().Passengers);
        System.out.println(new AllPassengers().getPassengers());

        getBags_number();

        score = findViewById(R.id.score);
        bags_price = findViewById(R.id.bags_price);
        know_total = findViewById(R.id.know_total);
        total_cost = findViewById(R.id.total_cost);


        passenger = new Passenger(12, "Salma Alsharif","Salma_AlSharif", "salma123", "0599837889", 300, new Trip());
        score.setText(passenger.getScore() +"");



    }


    private void getBags_number() {
        bags_num = (NumberPicker)findViewById(R.id.bags_num);
        bags_num.setMinValue(1);
        bags_num.setMaxValue(20);
        bags_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
                bagsPr = bags_num.getValue()*6;
                bags_price.setText(bagsPr + " نقطة ");
            }
        });
    }


    public void knowTotal(View view) {
        total = 192 + bagsPr;
        System.out.println(total);
        total_cost.setText("التكلفة الإجمالية : " + total + " نقطة ");

    }

    public void buyOnClick(View view) {
        if (total <= passenger.getScore()) {
            passenger.setScore(passenger.getScore() - total);
            Intent intent = new Intent(this, chargePoints.class);
            intent.putExtra("score",passenger.getScore()+"");
            System.out.println(passenger.getScore());
            startActivity(intent);
        }

        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("خطأ")
                    .setMessage("ليس لديك نقاط كافية")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    })
                    .show();
        }
    }
}