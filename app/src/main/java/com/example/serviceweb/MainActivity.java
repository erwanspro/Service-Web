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

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, "https://gsb.siochaptalqper.fr/praticiens/numdep/", null, response -> { }, error -> { }
        );


    }
    public TextView fetchNumDep(int numdep){
        textViewDep.setText("Vous avez choisi le numéro : "+numdep);
        String url = "https://gsb.siochaptalqper.fr/praticiens/" + numdep + "/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extraire les données du film
                            String num = response.getString("PRA_NUM");
                            String nom = response.getString("PRA_NOM");
                            String prenom =  response.getString("PRA_PRENOM");
                            String add = response.getString("PRA_ADRESSE");

                            int episodeId = response.getInt("episode_id");
                            // Afficher les détails du film
                            textViewDep.setText(num+"\n"+"nom : "+nom+"\n"+"prenom : "+prenom+"\n"+"adresse : "+add);
                            //textViewEpisodeId.setText("Episode n°" +episodeId) ;
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
        btnDepartement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmIdStr = textViewDep.getText().toString().trim();

                if (filmIdStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un numéro de departement",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int filmId = Integer.parseInt(filmIdStr);
//                if (filmId < 1 || filmId > 6) {
//                    Toast.makeText(MainActivity.this, "Numéro de film doit être entre 1 et 6",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }

                //fetchFilmDetails(filmId);
            }
        });
    }
}