package nadav.altabet.nadaltresortcloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomePage extends AppCompatActivity {
    private User currentUser;
    private String userName;
    private TextView welcomeTitle;
    private CardView newOrder,orderHistory,myProfile,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        welcomeTitle = findViewById(R.id.welcomeTitle);
        newOrder = findViewById(R.id.newOrder);
        orderHistory = findViewById(R.id.orderHistory);
        myProfile = findViewById(R.id.myProfile);
        logout = findViewById(R.id.logout);

        currentUser = Client.getCurrentUser();
        userName = currentUser.getFirstName();
        welcomeTitle.setText("Welcome, " + userName + "!");

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePage.this,MyProfile.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.setCurrentUser(null);
                Client.setUID(null);
                startActivity(new Intent(WelcomePage.this,MainActivity.class));
            }
        });
        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePage.this,NewOrder.class));
            }
        });
        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomePage.this,OrderHistory.class));
            }
        });

    }
}
