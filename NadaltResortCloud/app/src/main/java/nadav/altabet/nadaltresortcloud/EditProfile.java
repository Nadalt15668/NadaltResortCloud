package nadav.altabet.nadaltresortcloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class EditProfile extends AppCompatActivity {

    private EditText fstName,lstName,phone;
    private Button imgChoose,DateOfBirth,save,cancel;
    private ImageView profileImg;
    private RadioGroup rdgp;
    private RadioButton maleBtn,femaleBtn;
    private String mypath="gs://nadalt-resort-system.appspot.com";// Image Path
    private Calendar calendar = Calendar.getInstance();
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int month = calendar.get(Calendar.MONTH);
    private int year = calendar.get(Calendar.YEAR);
    private int chosenYear,chosenMonth,chosenDay;
    private Date date = null;

    private Boolean chosen = false;
    private static Uri selectedPic = null;
    private static String picName = "";

    private Task<Void> reference=null;
    User newUser = new User();

    //--------------------------------------------------------------------------
    private FirebaseDatabase database = null;
    private DatabaseReference databaseReference = null;
    private FirebaseAuth firebaseAuth = null;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    //--------------------------------------------------------------------------
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
                    Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e ) {} catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data!= null)
        {
            chosen = true;
            selectedPic = data.getData();
            profileImg.setImageURI(selectedPic);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedPic,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picName = cursor.getString(columnIndex);
            picName = picName.substring(picName.lastIndexOf('/')+1);
            chosen = true;
        }
    }
    private void addPic()
    {
        StorageReference PicturesStorage = storageReference.child("Pictures/"+picName);
        PicturesStorage.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfile.this, "Profile Picture Has Been Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProfile.this,Login.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        fstName = findViewById(R.id.fstNameEdit);
        lstName = findViewById(R.id.lstNameEdit);
        imgChoose = findViewById(R.id.profileImgEdit);
        DateOfBirth = findViewById(R.id.dateBtnEdit);
        save = findViewById(R.id.OK);
        cancel = findViewById(R.id.Cancel);
        profileImg = findViewById(R.id.imageViewEdit);
        rdgp = findViewById(R.id.rdgpEdit);
        maleBtn = findViewById(R.id.maleBtnEdit);
        femaleBtn = findViewById(R.id.femaleBtnEdit);
        phone = findViewById(R.id.phoneEdit);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        fstName.setText(Client.getCurrentUser().getFirstName());
        lstName.setText(Client.getCurrentUser().getLastName());
        phone.setText(Client.getCurrentUser().getPhone());
        DateOfBirth.setText(Client.getCurrentUser().getDateOfBirth().getDay() + "/" + Client.getCurrentUser().getDateOfBirth().getMonth() + "/" + Client.getCurrentUser().getDateOfBirth().getYear());
        date = Client.getCurrentUser().getDateOfBirth();
        picName = Client.getCurrentUser().getPic();
        showImageFromFirebase();
        if (Client.getCurrentUser().getGender()=="male")
            maleBtn.setChecked(true);
        else
            femaleBtn.setChecked(true);

        imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it,1);
            }
        });
        DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateOfBirth.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenDay = dayOfMonth;
                        chosenMonth = month+1;
                        chosenYear = year;
                        date = new Date(chosenYear,chosenMonth,chosenDay);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(EditProfile.this).create();
                dialog.setTitle("Just Checking...");
                dialog.setMessage("Are You Sure You Want To Cancel Any Changes To Your Account?");
                dialog.setCancelable(false);
                dialog.setButton(dialog.BUTTON_NEUTRAL, "Yes,Im Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(EditProfile.this,WelcomePage.class));
                    }
                });
                dialog.setButton(dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newUser.setFirstName(fstName.getText().toString());
                newUser.setLastName(lstName.getText().toString());
                newUser.setEmail(Client.getCurrentUser().getEmail());
                if (rdgp.getCheckedRadioButtonId()==maleBtn.getId())
                    newUser.setGender("male");
                else
                    newUser.setGender("female");
                newUser.setDateOfBirth(date);
                if (chosen)
                    addPic();
                newUser.setDateOfBirth(date);
                newUser.setPic(picName);
                reference = database.getReference("User").child(Client.getUID()).setValue(newUser);
                Client.setCurrentUser(newUser);
                Toast.makeText(EditProfile.this, "User Information Has Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProfile.this,WelcomePage.class));

            }
        });

    }
}
