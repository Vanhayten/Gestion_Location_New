package com.example.gestionlocationnew;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.util.ByteBufferUtil;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class mes_clients extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ListView ls;
    ArrayList<list_client> arrayList;
    PageAdapter_client listrep;
    gestion_location db;
    TextView Cin;
    EditText t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_clients);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //-------------------------------------------client

        //-------------------------


        NavigationView navigationView1 = (NavigationView)findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);

        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom =  b.getString("prenom");
        role = ""+b.getString("role");
        username.setText(Nom+" "+Prenom);
        role1.setText(role);
        //charge liste view par les clients

        db= new gestion_location(this);
      //  boolean h=db.insert_client("hadini","mohamed","fes","cn33820","06514665","etudiant");


        ls=(ListView)findViewById(R.id.listClient);
        t1=(EditText)findViewById(R.id.chercherCharge);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_client> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from Clients where cin ='"+t1.getText()+"'";
                Cursor c = table.rawQuery ( requet, null );
                if(c.getCount()>=1){
                    ls.clearChoices();
                    arrayList1= new ArrayList<list_client> ();
                    while (c.moveToNext ())
                    {
                        list_client list = new list_client (c.getString(0)+" "+c.getString(1),c.getString(3));
                        arrayList1.add ( list );
                    }
                    PageAdapter_client adapter_vihucle = new PageAdapter_client (mes_clients.this,arrayList1);
                    ls.setAdapter ( adapter_vihucle );
                }else{
                    ls.setAdapter (listrep);
                }
//
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from Clients ";
        Cursor c = table.rawQuery ( requet, null );
        if(c.getCount()==0){
            /*
            finish();
            Intent i=new Intent(this,Ajoute_vihicule.class);
            Bundle b1 = new Bundle();
            b1.putString("nom",Nom);
            b1.putString("prenom",Prenom);
            b1.putString("role",role);
            i.putExtras(b1);
            startActivity(i);*/
        }
        arrayList = new ArrayList<list_client> ();
        arrayList.clear ();
        while (c.moveToNext ())
        {
           list_client list = new list_client (c.getString(0)+" "+c.getString(1),c.getString(3));

            arrayList.add (list);
        }
        listrep = new PageAdapter_client ( this, arrayList );
        ls.setAdapter ( listrep );

/**
 * onselect from liste view
 */
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cin = (TextView) view.findViewById(R.id.marqueV);
                final Dialog MyDyalog;

                MyDyalog = new Dialog(mes_clients.this);
                MyDyalog.setContentView(R.layout.dialog_mise_jour_client);
                Button modifier, suprimer;
                TextView text1, text_nom;
                text1 = (TextView) MyDyalog.findViewById(R.id.text_close);
                text_nom = (TextView) MyDyalog.findViewById(R.id.text_nom);
                text_nom.setText(Cin.getText().toString());
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDyalog.dismiss();
                    }
                });

                MyDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                MyDyalog.show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent T;
        Bundle b = new Bundle();
        switch (menuItem.getItemId()){
            case R.id.vihicule:
                T = new Intent(this, vehicules.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                finish();
                startActivity(T);
                break;

        }
        return false;
    }


    public void ajouter(View view) {
        final Dialog MyDyalog_ajou;
        MyDyalog_ajou = new Dialog(mes_clients.this);
        MyDyalog_ajou.setContentView(R.layout.dialog_ajouter_client);
        final EditText text1, text2, text3, text4, text5, text6, text7, text8, text9,text13;
        final Spinner text10,text11;
        final TextView text12;
        Spinner sp ;

        sp= (Spinner) MyDyalog_ajou.findViewById(R.id.text_typePayment);
        ArrayList<String> arr  = new ArrayList<String>();
        arr.add("Chéque");
        arr.add("Virement");
        arr.add("Espèce");
        arr.add("Crédit");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        sp.setAdapter(arrayAdapter);



        text1 = (EditText) MyDyalog_ajou.findViewById(R.id.text_nom1);
        text2 = (EditText) MyDyalog_ajou.findViewById(R.id.text_prenom);
        text3 = (EditText) MyDyalog_ajou.findViewById(R.id.text_cin);
        text4 = (EditText) MyDyalog_ajou.findViewById(R.id.text_tel);
        text5 = (EditText) MyDyalog_ajou.findViewById(R.id.text_activity);
        text6 = (EditText) MyDyalog_ajou.findViewById(R.id.text_dateDebut);
        text7 = (EditText) MyDyalog_ajou.findViewById(R.id.text_dateFin);
        text8 = (EditText) MyDyalog_ajou.findViewById(R.id.text_nbJour);
        text9 = (EditText) MyDyalog_ajou.findViewById(R.id.text_Prix);
        text13 = (EditText) MyDyalog_ajou.findViewById(R.id.text_adr);
        text10 = (Spinner) MyDyalog_ajou.findViewById(R.id.text_typePayment);
        text11 = (Spinner) MyDyalog_ajou.findViewById(R.id.text_matriculeChoisi);
        text12 = (TextView) MyDyalog_ajou.findViewById(R.id.les_matricules);
        Button btn_ajoute;
        btn_ajoute = (Button) MyDyalog_ajou.findViewById(R.id.btn_ajouterClient);
        btn_ajoute.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                boolean b = db.insert_client(text1.getText().toString(),text2.getText().toString(),text13.getText().toString(),text3.getText().toString(),text4.getText().toString(),text5.getText().toString(),text6.getText().toString(),text7.getText().toString(),Integer.parseInt( text8.getText().toString()) ,Integer.parseInt( text9.getText().toString()),text10.getSelectedItem().toString(),text12.getText().toString());
                if (b) {
                    Toast.makeText(mes_clients.this, "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();
                    MyDyalog_ajou.dismiss();
                } else {
                    Toast.makeText(mes_clients.this, "l'enregistrement ne pas effectuer", Toast.LENGTH_SHORT).show();
                }

            }
    });
        MyDyalog_ajou.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MyDyalog_ajou.show();
    }
}
