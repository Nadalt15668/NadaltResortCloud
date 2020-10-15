package nadav.altabet.nadaltresortcloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class MyProfile extends AppCompatActivity {

    private TextView name,gender,dateOfBirth,email,phone;
    private ImageView profileImg;
    private Button editProfile;
    private String mypath="gs://nadalt-resort-system.appspot.com";  // Image Path

    private void showImageFromFirebase()
    {

        User myuser=Client.getCurrentUser();
        String picname=myuser.getPic();
        String suffix=picname.substring(picname.lastIndexOf(".")+1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("Pictures").child(picname);
        try
        {
            final File localFile = File.createTempFile(picname, suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profileImg.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    String message=exception.getMessage();
                    Toast.makeText(MyProfile.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e ) {} catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        name = findViewById(R.id.NameProfile);
        gender = findViewById(R.id.GenderProfile);
        dateOfBirth = findViewById(R.id.DateOfBirthProfile);
        profileImg = findViewById(R.id.myProfileImg);
        editProfile = findViewById(R.id.editProfile);
        email = findViewById(R.id.EmailProfile);
        phone = findViewById(R.id.phoneProfile);
        User myUser = Client.getCurrentUser();
        name.setText(myUser.getFirstName() + " " + myUser.getLastName());
        email.setText(myUser.getEmail());
        gender.setText(myUser.getGender());
        dateOfBirth.setText(myUser.getDateOfBirth().getDay()+"/"+myUser.getDateOfBirth().getMonth()+"/"+myUser.getDateOfBirth().getYear());
        phone.setText(Client.getCurrentUser().getPhone());
        showImageFromFirebase();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this,EditProfile.class));
            }
        });
    }
}
