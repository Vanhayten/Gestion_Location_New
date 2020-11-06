package com.example.gestionlocationnew;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class vehicules extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    GoogleSignInClient mGoogleSignInClient;


    String Nom,Prenom,role,login;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ListView ls;
    ArrayList<list_vihcule> arrayList;
    PageAdapter_vihucle listrep;
    gestion_location db;
    TextView matr;
    Dialog myDyalog;
    Dialog AjouteDialog;
    EditText t1;
    Cursor c,c1;



    int intColot = 0;


    private DatePickerDialog.OnDateSetListener mDateSetListenerdebute;
    private DatePickerDialog.OnDateSetListener mDateSetListenerfin;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    @Override
    protected void onStart() {
        super.onStart();



    }

    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * google
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);










        NavigationView navigationView1 = (NavigationView)findViewById(R.id.navigationView);
        View headerView = navigationView1.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.unser_name);
        TextView role1 = headerView.findViewById(R.id.role);
        profile = (CircleImageView)headerView.findViewById(R.id.profilpic);




        /**
         * get image from google and gut its in profile
         */
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        String urlsImage = sp.getString("URLImage","");
        if(!urlsImage.equals("")){
            ImageLoadTask imageLoadTask = (ImageLoadTask) new ImageLoadTask(urlsImage, profile).execute();
        }





        Bundle b = getIntent().getExtras();
        Nom = b.getString("nom");
        Prenom =  b.getString("prenom");
        role = ""+b.getString("role");
        login = ""+b.getString("login");
        username.setText(Nom+" "+Prenom);
        role1.setText(role);


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
        ls=(ListView)findViewById(R.id.list1);
        t1=(EditText)findViewById(R.id.chercherMatr);

        t1.setSelected(false);
        t1.setFocusable(false);


        /**
         * animation
         * On first use
         */

        //Animation  fromnav = AnimationUtils.loadAnimation(this,R.anim.fromnav);
      //  navigationView.setAnimation(fromnav);









        // recherche par matrucule
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<list_vihcule> arrayList1;
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from véhicules where immatriculation ='"+t1.getText()+"' and login ='"+login+"'";
               c = table.rawQuery ( requet, null );
                if(c.getCount()>=1){
                    ls.clearChoices();
                    arrayList1= new ArrayList<list_vihcule> ();
                    while (c.moveToNext ())
                    {
                        list_vihcule list = new list_vihcule (c.getString(0),c.getString(2),Integer.parseInt(c.getString(7)));
                        arrayList1.add ( list );
                    }
                    PageAdapter_vihucle adapter_vihucle = new PageAdapter_vihucle (vehicules.this,arrayList1);
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

        //-------------------------

                    SQLiteDatabase table = db.getReadableDatabase ();

                   String requet = "select * from véhicules where login ='"+login+"'";
                    c1 = table.rawQuery ( requet, null );
                      /*
                   if(c1.getCount()==0){
                        finish();
                       Intent i=new Intent(this,Ajoute_vihicule.class);
                        Bundle b1 = new Bundle();
                        b1.putString("nom",Nom);
                        b1.putString("prenom",Prenom);
                        b1.putString("role",role);
                        i.putExtras(b1);
                       startActivity(i);
                   }
                   */

                    arrayList = new ArrayList<list_vihcule> ();
                    arrayList.clear ();

        while (c1.moveToNext ())
                    {

                        list_vihcule list = new list_vihcule (c1.getString(0),c1.getString(2),Integer.parseInt(c1.getString(7)));
                        arrayList.add ( list );
                    }
                    listrep = new PageAdapter_vihucle ( this, arrayList );
                    ls.setAdapter ( listrep );



        //onclick on listner aficher le dialog

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDyalog = new Dialog(vehicules.this);
                myDyalog.setContentView(R.layout.dialog_vihicule);

               // myDyalog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                myDyalog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


                TextView text1,text2,text3,text4,text5,text6,text7,text8,text9;
                Button Suprimer,Modifier;


                text1 = (TextView)myDyalog.findViewById(R.id.text_nom);
                text2 = (TextView)myDyalog.findViewById(R.id.text_matricule);
                text3 = (TextView)myDyalog.findViewById(R.id.text_datecirulation);
                text4 = (TextView)myDyalog.findViewById(R.id.text_marqueCombision);
                text5 = (TextView)myDyalog.findViewById(R.id.text_valeur_entrer);
                text6 = (TextView)myDyalog.findViewById(R.id.text_dateeffet);
                text7 = (TextView)myDyalog.findViewById(R.id.text_dateechance);
                text8 = (TextView)myDyalog.findViewById(R.id.text_couleur);
                text9 = (TextView)myDyalog.findViewById(R.id.text_close);
                Suprimer =(Button)myDyalog.findViewById(R.id.btn_suprimer);
                Modifier =(Button)myDyalog.findViewById(R.id.btn_modifier);


                matr =(TextView)view.findViewById(R.id.matrV);






                //Onclose dyalog
                text9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDyalog.dismiss();
                    }
                });



                Modifier.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * code modifier
                         */
                        final EditText text11,text22,text33,text55,text66,text77;
                        ColorSeekBar Colorseek;;
                        final Spinner text44;
                        Button Confirmer;

                        AjouteDialog= new Dialog(vehicules.this);
                        AjouteDialog.setContentView(R.layout.dialog_ajoute_vihicule);

                        text11 = (EditText)AjouteDialog.findViewById(R.id.text_nom1);
                        text22 = (EditText)AjouteDialog.findViewById(R.id.text_prenom);
                        text33 = (EditText)AjouteDialog.findViewById(R.id.text_cin);
                        text44 = (Spinner)AjouteDialog.findViewById(R.id.text_marqueCombision1);
                        text55 = (EditText)AjouteDialog.findViewById(R.id.text_tel);
                        text66 = (EditText)AjouteDialog.findViewById(R.id.text_activity);
                        text77 = (EditText)AjouteDialog.findViewById(R.id.text_dateDebut);
                        Colorseek = (ColorSeekBar)AjouteDialog.findViewById(R.id.vihicule_Couleur);
                        Confirmer =(Button)AjouteDialog.findViewById(R.id.btn_modifier1);



                        /**
                         * get date debute
                         */
                        text33.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(vehicules.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListener,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                text33.setText(datefin);
                            }
                        };





                        /**
                         * get date debute
                         */
                        text66.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(vehicules.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListenerdebute,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenerdebute = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                text66.setText(datefin);
                            }
                        };



                        /**
                         * get date debute
                         */
                        text77.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar cal = Calendar.getInstance();
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH);
                                int Day = cal.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog dialogDate = new DatePickerDialog(vehicules.this
                                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                                        ,mDateSetListenerfin,Year, Month,Day);

                                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDate.show();

                            }
                        });

                        mDateSetListenerfin = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month =month+1;
                                String datefin = dayOfMonth+"/"+month+"/"+year;
                                text77.setText(datefin);
                            }
                        };





                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add("Diesel");
                        arrayList.add("Essence");
                        arrayList.add("Hybride");
                  ArrayAdapter<String> arraspinner = new ArrayAdapter<String>(vehicules.this,R.layout.support_simple_spinner_dropdown_item,arrayList);
                        text44.setAdapter(arraspinner);

                        SQLiteDatabase table = db.getReadableDatabase ();
                        String requet = "select * from véhicules where immatriculation ='"+matr.getText()+"' and login ='"+login+"'";
                        Cursor c = table.rawQuery ( requet, null );

                        while (c.moveToNext()){
                            text11.setText(c.getString(0));
                            text22.setText(c.getString(2));
                            text33.setText(c.getString(1));
                            text55.setText(c.getString(4));
                            text66.setText(c.getString(5));
                            text77.setText(c.getString(6));
                            intColot = Integer.parseInt(c.getString(7));
                        }
                        /**
                         * get color
                         */
                        Colorseek.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int i) {
                                intColot = i;
                            }
                        });


                        Confirmer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * confirmation suprimer
                                 *
                                 */
                                AlertDialog.Builder builder = new AlertDialog.Builder(vehicules.this);
                                builder.setCancelable(true);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Voulez-vous vraiment modifier ?");
                                builder.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                /**
                                                 * confirmer
                                                 */

                                                try {
                                                        db.modifier_vihucle(text11.getText().toString(),text33.getText().toString(),text22.getText().toString(),text44.getSelectedItem().toString(),Integer.parseInt(text55.getText().toString()),text66.getText().toString(),text77.getText().toString(),intColot,login);
                                                    Toast.makeText(vehicules.this, "Modification Réussi", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());

                                                }catch (Exception Ex){
                                                    Toast.makeText(vehicules.this, "Modification n'est pas Effectué", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         * not confirmer
                                         */
                                        AjouteDialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        });

                        AjouteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        AjouteDialog.show();
                    }
                });


                Suprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(vehicules.this);
                        builder.setCancelable(true);
                        builder.setTitle("suppression");
                        builder.setMessage("voullez-vous vraiment suprimer ?");
                        builder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.suprimer_vihucle(matr.getText().toString(),login);
                                        finish();
                                        startActivity(getIntent());
                                        myDyalog.dismiss();
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });


                //inisialise les donner from bas donner
                SQLiteDatabase table = db.getReadableDatabase ();
                String requet = "select * from véhicules where immatriculation ='"+matr.getText()+"' and login = '"+login+"'";
                Cursor c = table.rawQuery ( requet, null );

                while (c.moveToNext()){
                    text1.setText("Nom Vehicule :  "+c.getString(0));
                    text2.setText("Imatriculation :  "+c.getString(2));
                    text3.setText("Date Circulation :  "+c.getString(1));
                    text4.setText("Marque Combustion :  "+c.getString(3));
                    text5.setText("Valeur d'entrée :  "+c.getString(4));
                    text6.setText("Date Effet Assurance :  "+c.getString(5));
                    text7.setText("Date Echeance :  "+c.getString(6));
                    text8.setTextColor(Integer.parseInt(c.getString(7)));
                }



                myDyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDyalog.show();

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
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);


                break;
            case R.id.assurances:
                T = new Intent(this, assurances.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.entretiens:
                T = new Intent(this, entretiens.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;

            case R.id.recette:
                T = new Intent(this, mes_recettes.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.charges:
                T = new Intent(this, mes_charges.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.calendrier:
                T = new Intent(this, calendrier.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.clients:
                T = new Intent(this, mes_clients.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;
            case R.id.locations:
                T = new Intent(this, mes_location.class);
                b.putString("nom",Nom);
                b.putString("prenom",Prenom);
                b.putString("role",role);
                b.putString("login",login);
                T.putExtras(b);
                finish();
                startActivity(T);

                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);

                break;

            case R.id.logout:

                /**
                 * Sing out from google
                 */
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(vehicules.this, " déconnecté avec succès", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });


                T = new Intent(this, Login.class);
                SharedPreferences sp;
                sp = getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("logged",false).apply();
                startActivity(T);
                this.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_in_left);
                break;
        }
        return false;
    }

    public void Page_Ajoute(View view) {
        Intent Ajouter = new Intent(this,Ajoute_vihicule.class);
        Bundle b = new Bundle();
        b.putString("nom",Nom);
        b.putString("prenom",Prenom);
        b.putString("role",role);
        b.putString("login",login);
        Ajouter.putExtras(b);
        startActivity(Ajouter);
    }



}


