package proingsoftware.activities.consumidor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import proingsoftware.firebase.Firebase;

public class DescribirReclamoActivity extends AppCompatActivity {

    Button reclamar;
    Intent enviarReclamo;
    SharedPreferences sharedPreferences;
    private static final int CAMERA_REQUEST = 1888;
    private final static int SELECT_PHOTO = 12345;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    // FirebaseDatabase database = FirebaseDatabase.getInstance();
   //  DatabaseReference myRef = database.getReference("message");
    ImageButton camara;
    //Fer
    //ImageButton galeria;

    //Sergio (galeria imageview)
    ImageView galeria;

    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference mStorageReference;
    public Uri photoURI;

    //Sergio alternativa 2
    //private View imageContainer;

    //Sergio alternativa2
//    private class UploadImageOnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            imageContainer.setDrawingCacheEnabled(true);
//            imageContainer.buildDrawingCache();
//            Bitmap bitmap = imageContainer.getDrawingCache();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
//            imageContainer.setDrawingCacheEnabled(false);
//            byte[] data = baos.toByteArray();
//
//            String path = "reclamosFotos/" + UUID.randomUUID() + ".png";
//            StorageReference reclamosFotosRef = storage.getReference(path);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_describir_reclamo);
        this.imageView = (ImageView) this.findViewById(R.id.imagetoupload);
        sharedPreferences = getSharedPreferences("DatoCheckbox", MODE_PRIVATE);
        ;
        camara = (ImageButton) findViewById(R.id.camaraButton);
        //Fer
        //galeria = (ImageButton) findViewById(R.id.galeriaButton);
        //Sergio (galeria imageview)
        galeria =findViewById(R.id.galeriaImageView);

        reclamar = findViewById(R.id.enviarreclamoButton);

        reclamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageView != null) { //AQUI ENLAZAR LA BASE DE DATOS CON VALIDACIONES y que
                    enviarReclamo = new Intent(DescribirReclamoActivity.this, MenuConsumidorActivity.class);
                    startActivity(enviarReclamo);
                    CharSequence text = "Su reclamo será atendido lo antes posible";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    CharSequence text = "Debe adjuntar una imagen";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        })
        ;

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fer
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                //Sergio
                //choosePhoto();
            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de acceso", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Sergio
        //        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null ){
//            photo = data.getData();
//            imageView.setImageURI(photo);
//            uploadPicture();
//        }

        //Fer
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST ) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photoURI = data.getData();
                imageView.setImageBitmap(photo); //esta photo debe ser guardada en la bdd
                imageView.setImageURI(photoURI);
            }
            if (requestCode == SELECT_PHOTO) {
                Uri pickedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
//                cursor.moveToFirst();
//                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap photo = BitmapFactory.decodeFile(imagePath, options);
//                imageView.setImageBitmap(photo); //IGUAL ESTA FOTO DEBERIA ALMACENARSE
//                cursor.close();

                //TODO carrusel de fotos

                photoURI = pickedImage;
                imageView.setImageURI(photoURI);

                uploadPicture();
            }
        }
    }

    //Sergio
    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Cargando Imagen...");
        pd.show();
        final String randomName = UUID.randomUUID().toString();
        StorageReference riversRef = mStorageReference.child("reclamos/images/" + randomName);

        riversRef.putFile(photoURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Imagen cargada", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                       pd.dismiss();
                       Toast.makeText(getApplicationContext(),  "Error al cargar imagen", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double porcentaje =  (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Porcentaje: " + (int) porcentaje + "%");
                    }
                });
    }

//    private void choosePhoto() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//    }
 }