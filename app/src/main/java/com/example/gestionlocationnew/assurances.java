package com.example.gestionlocationnew;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class assurances extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String Nom,Prenom,role;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ListView ls;
    EditText t1;
    ArrayList<list_vihcule> arrayList;
    PageAdapter_vihucle listrep;
    gestion_location db;
    Dialog myDyalog;
    TextView matr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assurances);

        db= new gestion_location(this);

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
        // recherche par matrucil
        t1=(EditText)findViewById(R.id.Recherche_assurance);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_vihcule> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from véhicules where immatriculation ='"+t1.getText()+"'";
                Cursor c = table.rawQuery ( requet, null );
                if(c.getCount()>=1){
                    ls.clearChoices();
                    arrayList1= new ArrayList<list_vihcule> ();
                    while (c.moveToNext ())
                    {
                        list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),c.getString(7));
                        arrayList1.add ( list );
                    }
                    PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle (assurances.this,arrayList1);
                    ls.setAdapter ( adapter_vihucle );
                }else{
                    ls.setAdapter (listrep);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //-------------------------
        ls=(ListView)findViewById(R.id.list2);
        //onclick on listner aficher le dialog
        myDyalog = new Dialog(this);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Button sinistre,assurance;
                TextView textClose,textnom;
                myDyalog.setContentView(R.layout.dialog_assurance_sinistre);
                sinistre   =(Button)myDyalog.findViewById(R.id.btn_sinistres);
                assurance  =(Button)myDyalog.findViewById(R.id.btn_assurance);
                textClose =(TextView)myDyalog.findViewById(R.id.text_close);
                textnom =(TextView)myDyalog.findViewById(R.id.text_nom);


                /**
                 * get matricule to dialog
                 */
                matr =(TextView)view.findViewById(R.id.marqueV);
                textnom.setText("le matricule : "+matr.getText());

                /**
                 * button sinistre
                 */
                sinistre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TextView textnom,textClose;
                        Button historique,ajoute;
                        final Dialog myDyalog_ajoute;
                        myDyalog_ajoute = new Dialog(assurances.this);
                        myDyalog_ajoute.setContentView(R.layout.dialog_historique_ajouter_sinistre);
                        textnom =(TextView)myDyalog_ajoute.findViewById(R.id.text_nom);
                        textnom.setText("le matricule : "+matr.getText());

                        //Onclose dyalog
                        textClose =(TextView)myDyalog_ajoute.findViewById(R.id.text_close);
                        textClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDyalog_ajoute.dismiss();
                            }
                        });

                        /**
                         * buton historique
                         */
                        historique   =(Button)myDyalog.findViewById(R.id.btn_historique);
                        historique.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                        Intent I = new Intent(assurances.this,historique_sinistre.class);
                        Bundle B = new Bundle();
                        B.putString("matricule",matr.getText().toString());
                        I.putExtras(B);
                        startActivity(I);
                            }
                        });







                        myDyalog_ajoute.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDyalog_ajoute.show();

                    }
                });





                //Onclose dyalog
                textClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDyalog.dismiss();
                    }
                });
                myDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDyalog.show();

            }
        });






        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from véhicules ";
        Cursor c = table.rawQuery ( requet, null );
        if(c.getCount()==0){
            Intent i=new Intent(this,Ajoute_vihicule.class);
            startActivity(i);
        }
        arrayList = new ArrayList<list_vihcule> ();
        arrayList.clear ();
        while (c.moveToNext ())
        {
            list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),c.getString(7));
            arrayList.add ( list );
        }
       listrep = new PageAdapter_vihucle ( this, arrayList );
        ls.setAdapter ( listrep );



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
                startActivity(T);
                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;

            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                T.putExtras(b);
                startActivity(T);
                break;

        }
        return false;
    }

}
