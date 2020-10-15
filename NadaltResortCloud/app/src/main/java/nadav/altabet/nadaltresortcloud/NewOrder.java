package nadav.altabet.nadaltresortcloud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class NewOrder extends AppCompatActivity {

    private Button orderDate, finish, plus, minus;
    private TextView amount, roomInfo,roomPriceTxt;
    private ImageView roomImage;
    private Spinner roomSpn;
    private Integer amountInt = 1;
    private Calendar calendar = Calendar.getInstance();
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int month = calendar.get(Calendar.MONTH);
    private int year = calendar.get(Calendar.YEAR);
    private int hour;
    private int minute;
    private int roomPrice = 10000;
    private Date chosenDate, currentDate = new Date(year,month,day);
    private ArrayList<String> roomName = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("Order");
    private Task<Void> reference = null;

    private void amountChecker() {
        if (roomSpn.getSelectedItemId() == 0) {
            if (amountInt < 7) {
                amount.setText(amountInt.toString());
            } else {
                Toast.makeText(NewOrder.this, "There's no room for more then 7 people in the Presidential Suite!", Toast.LENGTH_SHORT).show();
                amountInt = 7;
                amount.setText("7");
            }
        }
        if (roomSpn.getSelectedItemId() == 1) {
            if (amountInt < 25) {
                amount.setText(amountInt.toString());
            } else {
                Toast.makeText(NewOrder.this, "There's no room for more then 25 people in the Loft Room!", Toast.LENGTH_SHORT).show();
                amountInt = 25;
                amount.setText("25");
            }
        }
        if (roomSpn.getSelectedItemId() == 2) {
            if (amountInt < 4) {
                amount.setText(amountInt.toString());
            } else {
                Toast.makeText(NewOrder.this, "There's no room for more then 4 people in the Large Room!", Toast.LENGTH_SHORT).show();
                amountInt = 4;
                amount.setText("4");
            }
        }
        if (roomSpn.getSelectedItemId() == 3) {
            if (amountInt < 2) {
                amount.setText(amountInt.toString());
            } else {
                Toast.makeText(NewOrder.this, "There's no room for more then 2 people in the Small Room!", Toast.LENGTH_SHORT).show();
                amountInt = 2;
                amount.setText("2");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        orderDate = findViewById(R.id.orderDateBtn);
        finish = findViewById(R.id.orderFinish);
        plus = findViewById(R.id.plusButton);
        minus = findViewById(R.id.minusButton);
        amount = findViewById(R.id.amountText);
        roomImage = findViewById(R.id.roomImage);
        roomSpn = findViewById(R.id.roomSpn);
        roomInfo = findViewById(R.id.roomInfo);
        roomPriceTxt = findViewById(R.id.roomPrice);
        amount.setText(amountInt.toString());
        roomName.add("Presidetial Suite");
        roomName.add("Loft Room");
        roomName.add("Large Room");
        roomName.add("Small Room");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrder.this, android.R.layout.simple_list_item_1, roomName);
        roomSpn.setAdapter(adapter);
        roomSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        roomImage.setImageResource(R.drawable.presidential_suite);
                        roomPrice = 10000;
                        roomPriceTxt.setText("10,000$");
                        roomInfo.setText("This is the Presidential Suite - it features a master bedroom with upscale furnishing and a luxurious bathroom equipped with a Jacuzzi bathtub, separate Jet shower cabin and WC, a second bedroom with joined twin beds, a sitting area and a spacious and comfortable living room with an impressive décor.");
                        amountChecker();
                        break;
                    case 1:
                        roomImage.setImageResource(R.drawable.loft_room);
                        roomPrice = 1000;
                        roomPriceTxt.setText("1,000$");
                        roomInfo.setText("This is the Loft Room - Here you can enjoy with a bunch of friends in almost every event, a bachelor party, birthday or just any big party you want to have, you name it and we'll give you the best experience you'll have! ");
                        amountChecker();
                        break;
                    case 2:
                        roomImage.setImageResource(R.drawable.large_room);
                        roomPrice = 100;
                        roomPriceTxt.setText("100$");
                        roomInfo.setText("This is the Large Room - A room for a max of 4 people. Here you pass the night in a small apartment with a small kitchen, a toilet and a bathroom");
                        amountChecker();
                        break;
                    case 3:
                        roomImage.setImageResource(R.drawable.small_room);
                        roomPrice = 50;
                        roomPriceTxt.setText("50$");
                        roomInfo.setText("This is the Small Room - Here you can pass the night with another person with the simplest of conditions - two small beds, a kitchen and a kettle");
                        amountChecker();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomSpn.getSelectedItemId() == 0) {
                    if (amountInt < 7) {
                        amountInt++;
                        amount.setText(amountInt.toString());
                    } else {
                        Toast.makeText(NewOrder.this, "There's no room for more then 7 people in the Presidential Suite!", Toast.LENGTH_SHORT).show();
                        amountInt = 7;
                        amount.setText("7");
                    }
                }
                if (roomSpn.getSelectedItemId() == 1) {
                    if (amountInt < 25) {
                        amountInt++;
                        amount.setText(amountInt.toString());
                    } else {
                        Toast.makeText(NewOrder.this, "There's no room for more then 25 people in the Loft Room!", Toast.LENGTH_SHORT).show();
                        amountInt = 25;
                        amount.setText("25");
                    }
                }
                if (roomSpn.getSelectedItemId() == 2) {
                    if (amountInt < 4) {
                        amountInt++;
                        amount.setText(amountInt.toString());
                    } else {
                        Toast.makeText(NewOrder.this, "There's no room for more then 4 people in the Large Room!", Toast.LENGTH_SHORT).show();
                        amountInt = 4;
                        amount.setText("4");
                    }
                }
                if (roomSpn.getSelectedItemId() == 3) {
                    if (amountInt < 2) {
                        amountInt++;
                        amount.setText(amountInt.toString());
                    } else {
                        Toast.makeText(NewOrder.this, "There's no room for more then 2 people in the Small Room!", Toast.LENGTH_SHORT).show();
                        amountInt = 2;
                        amount.setText("2");
                    }
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountInt > 1) {
                    amountInt--;
                    amount.setText(amountInt.toString());
                } else {
                    Toast.makeText(NewOrder.this, "You Cannot Change The Guests Amount To Less Then One", Toast.LENGTH_LONG).show();
                }
            }
        });
        orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewOrder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        orderDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        chosenDate = new Date(year, month, dayOfMonth);
                        if (chosenDate.isPast(currentDate))
                            Toast.makeText(NewOrder.this, "Please Re-enter The Chosen Date For A Future Date", Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenDate == null) {
                    AlertDialog dialog = new AlertDialog.Builder(NewOrder.this).create();
                    dialog.setTitle("new Information");
                    dialog.setMessage("You Are Missing a Date, Please Fill The Missing Information");
                    dialog.setCancelable(false);
                    dialog.setButton(dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    if (chosenDate.isPast(currentDate))
                        Toast.makeText(NewOrder.this, "Please Re-enter The Chosen Date For A Future Date", Toast.LENGTH_SHORT).show();
                    else {
                        Order order = new Order();
                        hour = calendar.get(Calendar.HOUR_OF_DAY);
                        minute = calendar.get(Calendar.MINUTE);
                        order.setDateOfOrder(new Date(year,month,day,hour,minute));
                        order.setGuestsNumber(amountInt);
                        order.setRoomPrice(roomPrice);
                        order.setRoomType(roomSpn.getSelectedItem().toString());
                        order.setVacationDate(chosenDate);
                        order.setClientUID(Client.getUID());
                        DatabaseReference newRef = ref.push();
                        reference = newRef.setValue(order);
                        startActivity(new Intent(NewOrder.this, WelcomePage.class));
                        Toast.makeText(NewOrder.this, "Order Confirmed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
