package nadav.altabet.nadaltresortcloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowOrder extends AppCompatActivity {

    private ImageView roomImage;
    private TextView roomType,roomPrice,guestAmount,dateOfOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        roomImage= findViewById(R.id.roomShow);
        roomType = findViewById(R.id.roomTypeShow);
        guestAmount = findViewById(R.id.guestAmountShow);
        roomPrice = findViewById(R.id.roomPriceShow);
        dateOfOrder = findViewById(R.id.dateShow);
        Order order = OrderHistory.arrTotOrder.get(OrderHistory.Position);
        order.setRoomImage(roomImage);
        roomType.setText(order.getRoomType());
        roomPrice.setText(Integer.toString(order.getRoomPrice()) + "$");
        guestAmount.setText(Integer.toString(order.getGuestsNumber()));
        dateOfOrder.setText(order.getDateOfOrder().getDay() + "/" + order.getDateOfOrder().getMonth() + "/" + order.getDateOfOrder().getYear());
    }
}
