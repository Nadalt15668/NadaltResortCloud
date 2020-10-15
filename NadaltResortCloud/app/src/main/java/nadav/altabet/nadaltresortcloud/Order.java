package nadav.altabet.nadaltresortcloud;

import android.widget.ImageView;

public class Order {

    private String clientUID;
    private Date dateOfOrder;
    private Date vacationDate;
    private String roomType;
    private Integer roomPrice;
    private Integer guestsNumber;
    //---------------------------------------------
    public Order(Date dateOfOrder, Date vacationDate, String roomType, int roomPrice, int guestsNumber) {
        this.clientUID = Client.getUID();
        this.dateOfOrder = dateOfOrder;
        this.vacationDate = vacationDate;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.guestsNumber = guestsNumber;
    }

    public Order() {

    }
    //---------------------------------------------
    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public Date getVacationDate() {
        return vacationDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomPrice() {
        return roomPrice;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public String getClientUID() {
        return clientUID;
    }
    //-------------------------------------------------
    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public void setVacationDate(Date vacationDate) {
        this.vacationDate = vacationDate;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public void setGuestsNumber(int guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public void setClientUID(String clientUID) {
        this.clientUID = clientUID;
    }
    //----------------------------------------------
    public void setRoomImage(ImageView imgView)
    {
        if (this.roomType == "Presidential Suite")
            imgView.setImageResource(R.drawable.presidential_suite);
        else if (this.roomType == "Loft Room")
            imgView.setImageResource(R.drawable.loft_room);
        else if (this.roomType == "Large Room")
            imgView.setImageResource(R.drawable.large_room);
        else
            imgView.setImageResource(R.drawable.small_room);
    }


}
