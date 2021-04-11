package com.example.resimgalerim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayAdapter arrayAdapter;
    SharedPreferences kutu;
    TextView listeismiText, yeniListeGirText;
    EditText editTextYeniListe;
    ImageView ekleButon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listviewId);
        nameArray = new ArrayList<String>();
        idArray =new ArrayList<Integer>();
        kutu = this.getSharedPreferences("com.example.resimgalerim" , Context.MODE_PRIVATE );
        editTextYeniListe = findViewById(R.id.editTextTextPersonName);
        listeismiText = findViewById(R.id.textView);
        ekleButon = findViewById(R.id.eklebutton);
        yeniListeGirText = findViewById(R.id.textView2);

        String kayitliisim = kutu.getString("listeismi" , "İsimsiz");
        listeismiText.setText(kayitliisim);

        if (kutu.getBoolean("ilkmi",true)){
            listView.setVisibility(View.INVISIBLE);
            listeismiText.setVisibility(View.INVISIBLE);
            yeniListeGirText.setVisibility(View.VISIBLE);
            editTextYeniListe.setVisibility(View.VISIBLE);
            ekleButon.setVisibility(View.VISIBLE);
        }
        else {
            listView.setVisibility(View.VISIBLE);
            listeismiText.setVisibility(View.VISIBLE);
            yeniListeGirText.setVisibility(View.INVISIBLE);
            editTextYeniListe.setVisibility(View.INVISIBLE);
            ekleButon.setVisibility(View.INVISIBLE);

        }

        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_expandable_list_item_1,nameArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , MainActivity2.class);
                intent.putExtra("artid", idArray.get(position));
                intent.putExtra("info", "old");

                startActivity(intent);
            }
        });

        getData();
    }


    public void getData(){

        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("sanatlar",MODE_PRIVATE,null);

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM sanat", null);

            int nameIndex = cursor.getColumnIndex("isim");
            int idIndex = cursor.getColumnIndex("id");

            while (cursor.moveToNext()){

                nameArray.add(cursor.getString(nameIndex));
                idArray.add(cursor.getInt(idIndex));

            }
            arrayAdapter.notifyDataSetChanged();   /// DEĞİŞİKLİKLERİ GÖSTER.
            cursor.close();
        }
        catch (Exception e){

        }

    }



    //////////////// res dosyasının altında 'menu' dosyası ve onun içine de menu XML si ekledik. Onu koda eklemek için bunları yapıyoruz: /////////////7
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_add,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  //// Menüye TIKLADNDIĞINDA NOLCAK

        ////////// ÖTEKİ ACTİCİTEYE YOLLADIK
        if(item.getItemId()==R.id.menuID){
            Intent intent =new Intent(getApplicationContext(),MainActivity2.class);
            intent.putExtra("info", "new");

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void ekleF(View view){




        if (editTextYeniListe.getText().toString().matches("") || editTextYeniListe.getText().toString().matches(" ")){
            Toast.makeText(this , "Lütfen liste adını girin" , Toast.LENGTH_SHORT).show();
        }
        else {

            listView.setVisibility(View.VISIBLE);
            listeismiText.setVisibility(View.VISIBLE);
            yeniListeGirText.setVisibility(View.INVISIBLE);
            editTextYeniListe.setVisibility(View.INVISIBLE);
            ekleButon.setVisibility(View.INVISIBLE);

            kutu.edit().putBoolean("ilkmi",false).apply();
            kutu.edit().putString("listeismi",editTextYeniListe.getText().toString()).apply();

            listeismiText.setText(editTextYeniListe.getText().toString());

            Toast.makeText(this,"Liste oluşturuldu",Toast.LENGTH_SHORT).show();
        }

    }

}