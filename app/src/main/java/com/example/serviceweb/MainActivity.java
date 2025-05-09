package com.example.serviceweb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.UnsupportedCharsetException;

public class MainActivity extends AppCompatActivity {
    private TextView textViewDep;
    private TextView textViewNom;
    private Button btnPraticien;
    private Button btnDepartement;
    private EditText saisiPraticien;
    private EditText saisiDepartement;
    private RequestQueue requestQueue;

    private void initView()
    {

        saisiPraticien = findViewById(R.id.saisiPraticien);
        saisiDepartement = findViewById(R.id.saisiDepartement);
        textViewDep = findViewById(R.id.textViewDep);
        textViewNom = findViewById(R.id.textViewNom);
        btnPraticien = findViewById(R.id.btnPraticien);
        btnDepartement = findViewById(R.id.btnDepartement);
        requestQueue = Volley.newRequestQueue(this);
        btnDepartement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departementStr = saisiDepartement.getText().toString().trim();
                try {
                    if (departementStr.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Veuillez entrer un numéro de departement ",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int numDep = Integer.parseInt(departementStr);

                    fetchNumDep(numDep);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this,"Erreur : Veuillez ne pas entrer une chaine de caractère.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
//        btnPraticien.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String praticienStr = saisiPraticien.getText().toString().trim();
//                try{
//                    if (praticienStr.isEmpty()) {
//                        Toast.makeText(MainActivity.this, "Veuillez entrer un ou plusieur caractère",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    String nomPrat = Integer.toString(Integer.parseInt(praticienStr));
////                    if (nomPrat.contains("01234566789")) {
////                        Toast.makeText(MainActivity.this, "Veuillez ne pas entrer un nombre",
////                                Toast.LENGTH_SHORT).show();
////                        return;
////                    }
//                    fetchNomPraticien(nomPrat);
//                } catch (NullPointerException e) {
//                    Toast.makeText(MainActivity.this,"Erreur : Veuillez ne pas entrer un nombre.",
//                            Toast.LENGTH_SHORT).show();
//                }

//            }
//        });
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET, "https://gsb.siochaptalqper.fr/praticiens/", null, response -> { }, error -> { }
//        );
    }
    public TextView fetchNumDep(int numdep){
//        textViewDep.setText("Vous avez choisi le numéro : " + numdep);
        String url = "https://gsb.siochaptalqper.fr/praticiens/numdep/" + numdep ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extraire les données de la tabble
                            int num = response.getInt("PRA_NUM");
                            String nom = response.getString("PRA_NOM");
                            String prenom =  response.getString("PRA_PRENOM");
                            String add = response.getString("PRA_ADRESSE");

                            textViewDep.setText(num + "\n" + "nom : " + nom + "\n" + "prenom : " + prenom + "\n" + "adresse : " + add);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Erreur d'analyse JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erreur: " +
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Ajouter la requête à la file d'attente
        requestQueue.add(jsonObjectRequest);

        return textViewDep;

    }
    public TextView fetchNomPraticien(String nomPrat){
//        textViewNom.setText("Voici les praticiens correspondant a la recherche: "+ nomPrat);
        String url = "https://gsb.siochaptalqper.fr/praticiens/numdep/" + nomPrat ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extraire les données de la tabble
                            int num = response.getInt("PRA_NUM");
                            String nom = response.getString("PRA_NOM");
                            String prenom =  response.getString("PRA_PRENOM");
                            String add = response.getString("PRA_ADRESSE");

                            textViewNom.setText(num+"\n"+"nom : "+nom+"\n"+"prenom : "+prenom+"\n"+"adresse : "+add);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Erreur d'analyse JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erreur: " +
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Ajouter la requête à la file d'attente
        requestQueue.add(jsonObjectRequest);

        return textViewNom;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        initView();
    }
}