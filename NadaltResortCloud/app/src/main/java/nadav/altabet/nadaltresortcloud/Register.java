package nadav.altabet.nadaltresortcloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private ProgressDialog prg;
    private EditText editEmail,editPass,editFst,editLst,editPhone;
    private ImageView imageView;
    private Button btnPick,btnSave,btnDate;
    private RadioGroup rdgp;
    private RadioButton female,male;
    private static Uri selectedPic = null;
    private static String picName = "";
    private static boolean chosen = false;
    private Calendar calendar = null;
    private int day,month,year;
    private Date dateOfBirth;
    //--------------------------------------------------------------------------
    private FirebaseDatabase database = null;
    private DatabaseReference databaseReference = null;
    private FirebaseAuth firebaseAuth = null;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    //--------------------------------------------------------------------------
    private void addPic()
    {
        StorageReference PicturesStorage = storageReference.child("Pictures/"+picName);
        PicturesStorage.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Register.this, "Profile Picture Has Been Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this,Login.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data!= null)
        {
            chosen = true;
            selectedPic = data.getData();
            imageView.setImageURI(selectedPic);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedPic,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picName = cursor.getString(columnIndex);
            picName = picName.substring(picName.lastIndexOf('/')+1);
        }
    }

    //---------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editEmail = findViewById(R.id.emailEditTxt);
        editPass = findViewById(R.id.passwordEditTxt);
        imageView = findViewById(R.id.imageView);
        btnPick = findViewById(R.id.choose);
        rdgp = findViewById(R.id.rdgb);
        btnSave = findViewById(R.id.finish);
        female = findViewById(R.id.femaleRadiob);
        male = findViewById(R.id.maleRadiob);
        editFst = findViewById(R.id.fstNameEditTxt);
        editLst = findViewById(R.id.lstNameEditTxt);
        btnDate = findViewById(R.id.btnDate);
        editPhone = findViewById(R.id.phoneEditTxt);
        imageView.setImageResource(R.drawable.profile);

        prg = new ProgressDialog(Register.this);
        prg.setTitle("Loading Data");
        prg.setMessage("Saving User Information");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        Permission permission = new Permission(Register.this);
        permission.verifyPermissions();


        //----------------------------------------------------------------------
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        //----------------------------------------------------------------------
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        dateOfBirth = new Date(year, month+1, dayOfMonth);
                    }
                },year,month,day);
                datePickerDialog.show();
                datePickerDialog.setCancelable(false);
            }
        });
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it,1);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.show();
                final String email = editEmail.getText().toString();
                final String password = editPass.getText().toString();
                final String fstName = editFst.getText().toString();
                final String lstName = editLst.getText().toString();
                final String phone = editPhone.getText().toString();
                if (dateOfBirth != null && email != null && password != null && fstName != null && lstName != null)
                {
                    if (dateOfBirth.ageInYears(new Date(year, month+1, day)) >= 18)
                    {
                        String pic = "profile.jpg";
                        if (chosen)
                            pic = picName;
                        final String finalPic = pic;
                        String gender = "male";
                        if (female.isChecked())
                            gender = "female";
                        final String finalGender = gender;
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    String UID = currentUser.getUid();
                                    User user = new User(email, finalGender, finalPic, fstName, lstName, dateOfBirth,phone);
                                    databaseReference.child(UID).setValue(user).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                prg.dismiss();
                                                Toast.makeText(Register.this, "User Information Has Been Synced Succussfully", Toast.LENGTH_SHORT).show();
                                                if (chosen)
                                                    addPic();
                                                startActivity(new Intent(Register.this, MainActivity.class));
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(Register.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                prg.dismiss();
                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Register.this, "You Can't Create An Account Under The Age Of 18", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,MainActivity.class));
                    }
                }
                else
                {
                    AlertDialog dialog = new AlertDialog.Builder(Register.this).create();
                    dialog.setTitle("Missing Information");
                    dialog.setMessage("Please Fill All Required Information");
                    dialog.setCancelable(false);
                    dialog.setButton(dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    prg.dismiss();
                }

            }


        });
    }
}

