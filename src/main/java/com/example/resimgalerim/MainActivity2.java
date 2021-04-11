package com.example.resimgalerim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    Bitmap selectedImage;
    EditText nameText, painterNameText, yearText;
    Button button,geributton;
    ImageView imageView, silmeikonu;
    TextView isimText,ressamTexr,yilText;
    SQLiteDatabase database;
    int sanatId;
    Boolean fotosecildi=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nameText = findViewById(R.id.nameId);
        painterNameText = findViewById(R.id.painterNameId);
        yearText = findViewById(R.id.yearId);
        button = findViewById(R.id.button);
        imageView=findViewById(R.id.imageView);
        isimText = findViewById(R.id.textViewisimId);
        ressamTexr = findViewById(R.id.textViewRessamId);
        yilText = findViewById(R.id.textViewyearId);
        geributton = findViewById(R.id.geriButon);
        silmeikonu = findViewById(R.id.silIconId);


        database = this.openOrCreateDatabase("sanatlar",MODE_PRIVATE,null);


        Intent intent =getIntent();
        String infoText = intent.getStringExtra("info");

        if (infoText.matches("old")){
            button.setVisibility(View.INVISIBLE);
            nameText.setVisibility(View.INVISIBLE);
            painterNameText.setVisibility(View.INVISIBLE);
            yearText.setVisibility(View.INVISIBLE);

            silmeikonu.setVisibility(View.VISIBLE);
            geributton.setVisibility(View.VISIBLE);
            isimText.setVisibility(View.VISIBLE);
            ressamTexr.setVisibility(View.VISIBLE);
            yilText.setVisibility(View.VISIBLE);
            imageView.setEnabled(false);

            sanatId = intent.getIntExtra("artid",1);

            try {

                Cursor cursor = database.rawQuery("SELECT * FROM sanat WHERE id= ?" , new String[] {String.valueOf(sanatId)});

                int isimIndex = cursor.getColumnIndex("isim");
                int ressamIndex = cursor.getColumnIndex("ressam");
                int yilIndex = cursor.getColumnIndex("yıl");
                int resimIndex = cursor.getColumnIndex("resim");

                while (cursor.moveToNext()){

                    isimText.setText(cursor.getString(isimIndex));
                    ressamTexr.setText(cursor.getString(ressamIndex));
                    yilText.setText(cursor.getString(yilIndex));
                    ///////BYTE I RESİME DÖNÜŞTÜRÜP IMAGEVİEWE AKTARDIK.
                    byte[] bytes = cursor.getBlob(resimIndex);
                    Bitmap bitmapResim = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                    imageView.setImageBitmap(bitmapResim);


                }

            }
            catch (Exception e){

            }

        }
        else {
            button.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
            painterNameText.setVisibility(View.VISIBLE);
            yearText.setVisibility(View.VISIBLE);

            silmeikonu.setVisibility(View.INVISIBLE);
            geributton.setVisibility(View.INVISIBLE);
            isimText.setVisibility(View.INVISIBLE);
            ressamTexr.setVisibility(View.INVISIBLE);
            yilText.setVisibility(View.INVISIBLE);

            Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.selectimage);   // Bizim varsayılan select image yazan fotoyu koyduk.
            imageView.setImageBitmap(selectImage);
        }

    }

    public void selectimageF(View view){
   ///////////////////////////////////////////////////GALERİYE GİTME İZNİNİ 'AndroidManifest.xml' dosyasında ALDIK O İZNİ KODA DÖKTÜK///////////////////////7

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // EĞER İZİN ALINMADIYSA
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);             //İZİN AL
        }
        else {  ///İZİN ZATEN VERİLİYSE GALERİYE GİDİYORUZ.
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {    //İZİN İÇİN CEVAP VERİNCE NOLCAK

        if (requestCode==1){     /// İZİNE EVET DERSE NOLCAK. GALERİYE YOLLADIK YİNE.
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode== RESULT_OK && data != null ){ // EĞER Bİ FOTO SEÇERSE
            Uri imageData = data.getData();
            fotosecildi = true;

            try {           ///// FOTOYU DÖNÜŞTÜRDÜK FALAN SONRA İMAGEVİEWE ATADIK.

                if (Build.VERSION.SDK_INT >= 28){
                    System.out.println("aaa");

                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver() , imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                }
                else {
                    System.out.println("bbb");

                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    System.out.println("bbb");

                    imageView.setImageBitmap(selectedImage);
                    System.out.println("bbb");


                }



            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    public void saveF(View view){

        if (nameText.getText().toString().matches("") || painterNameText.getText().toString().matches("") || yearText.getText().toString().matches("") || fotosecildi == false){
            Toast.makeText(this ,"Kayıt yapılamadı, eksik bilgi girdiniz." , Toast.LENGTH_LONG).show();
        }
        else{

            String tabloName = nameText.getText().toString();
            String painterName = painterNameText.getText().toString();
            String yearString = yearText.getText().toString();

            //////RESİMİ SQLİTE YE KAYDETMEK İÇİN BİTMAP'DEN VERİYE DÖNÜŞTÜRMEMİZ GEREK ONU YAPIYORUZ .
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

            Bitmap smallImage = makeSmallerPhoto(selectedImage , 300); /// yazdığımız metodu kullanarak fotuyu küçültüp başka bir değişkene attık.

            smallImage.compress(Bitmap.CompressFormat.PNG , 50 , outputStream);
            byte[] byteList= outputStream.toByteArray();


            ////////////SQLiteye seçilen resmi ve bilgileri kaydediyoruz.
            try {


                database = this.openOrCreateDatabase("sanatlar",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS sanat (id INTEGER PRIMARY KEY ,isim VARCHAR,ressam VARCHAR ,yıl VARCHAR ,resim BLOB) ");

                String sqlString = "INSERT INTO sanat (isim , ressam , yıl , resim) VALUES (? , ? , ? , ?)";    ////// kullanıcıdan aldığımız veriyi kullanmak için böyle yapıyoruz.
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);   ///Normal Stringi Sqlite Strinigne çeviriyor.
                sqLiteStatement.bindString(1,tabloName);    /// burada ve aşağıda sırayla girdiğimiz bilgileri soru işareti bıraktığımız yerlere ekliyor.
                sqLiteStatement.bindString(2,painterName);
                sqLiteStatement.bindString(3,yearString);
                sqLiteStatement.bindBlob(4,byteList);
                sqLiteStatement.execute();
                System.out.println("aaaa");

            }
            catch (Exception e){

            }

            System.out.println("bbbb");
            //finish();      /////// activiteyi kapatıyor.
            Intent intent = new Intent(MainActivity2.this , MainActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP); // Diğer tüm activiteleri kapatır.
            startActivity(intent);

        }


    }


    public Bitmap makeSmallerPhoto(Bitmap image , int maximumSize){    /// fotonun boyutlarını ayarladık.

        int width = image.getWidth();
        int height = image.getHeight();

        float bolum = (float) width / (float) height;

        if (bolum > 1){
            width = maximumSize;
            height = (int) (width / bolum);
        }
        else {
            height = maximumSize;
            width = (int) (height * bolum);
        }

        return Bitmap.createScaledBitmap(image , width , height , true);

    }

    public void geriF(View view){
        Intent intent = new Intent(MainActivity2.this,MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void silF(View view){

        System.out.println("alerttt");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Kaydı Sil");
        alert.setMessage("Kaydı silmek istediğine emin misin?");

        alert.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                database = MainActivity2.this.openOrCreateDatabase("sanatlar",MODE_PRIVATE,null);

                String sqlString = "DELETE FROM sanat WHERE id = ?";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1 , String.valueOf(sanatId));
                sqLiteStatement.execute();

                Toast.makeText(MainActivity2.this , "Kayıt Silindi..." , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity2.this , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();

    }

}