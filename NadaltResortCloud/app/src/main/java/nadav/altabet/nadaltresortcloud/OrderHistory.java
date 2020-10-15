package nadav.altabet.nadaltresortcloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    private ListView lst;
    private final ArrayList<Date> arrVacationDate = new ArrayList<>();
    private final ArrayList<Integer> arrGuestAmount = new ArrayList<>();
    private final ArrayList<Integer> arrRoomPrice = new ArrayList<>();
    private final ArrayList<String> arrRoomType = new ArrayList<>();
    public final static ArrayList<Order> arrTotOrder = new ArrayList<>();
    public static int Position = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Order");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        lst=findViewById(R.id.cardListView);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child: children) {
                            Order order = child.getValue(Order.class);
                            if (order.getClientUID().compareTo(Client.getUID()) == 0)
                    {
                        arrTotOrder.add(order);
                        arrVacationDate.add(order.getVacationDate());
                        arrGuestAmount.add(order.getGuestsNumber());
                        arrRoomPrice.add(order.getRoomPrice());
                        arrRoomType.add(order.getRoomType());
                    }
                }
                cardadapter adapter = new cardadapter(arrVacationDate,arrGuestAmount,arrRoomPrice,arrRoomType,OrderHistory.this);
                lst.setAdapter(adapter);

                lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Position = position;
                        startActivity(new Intent(OrderHistory.this,ShowOrder.class));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
