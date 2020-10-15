package nadav.altabet.nadaltresortcloud;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class cardadapter extends BaseAdapter {
    private ArrayList<Date> arrVacationDate = new ArrayList<>();
    private ArrayList<Integer> arrGuestAmount = new ArrayList<>();
    private ArrayList<Integer> arrRoomPrice = new ArrayList<>();
    private ArrayList<String> arrRoomType = new ArrayList<>();

    private Activity ctx;

    public cardadapter(ArrayList<Date> arrVacationDate, ArrayList<Integer> arrGuestAmount, ArrayList<Integer> arrRoomPrice, ArrayList<String> arrRoomType, Activity ctx) {
        this.arrVacationDate = arrVacationDate;
        this.arrGuestAmount = arrGuestAmount;
        this.arrRoomPrice = arrRoomPrice;
        this.arrRoomType = arrRoomType;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return this.arrVacationDate.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater =ctx.getLayoutInflater();

        View myrow= inflater.inflate(R.layout.cardviewdetails,null,true);

        TextView vacationDate = myrow.findViewById(R.id.vacationDateHis);
        vacationDate.setText(this.arrVacationDate.get(position).getDay() + "/" + this.arrVacationDate.get(position).getMonth() + "/" + this.arrVacationDate.get(position).getYear());

        TextView guestAmount = myrow.findViewById(R.id.numOfGuestsHis);
        guestAmount.setText(this.arrGuestAmount.get(position).toString());

        TextView roomPrice=myrow.findViewById(R.id.roomPriceHis);
        roomPrice.setText(this.arrRoomPrice.get(position).toString() + "$");

        TextView roomType=myrow.findViewById(R.id.roomTypeHis);
        roomType.setText(this.arrRoomType.get(position));

        return myrow;
    }
}
