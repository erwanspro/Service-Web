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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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
        btnDepartement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departementStr = saisiDepartement.getText().toString().trim();
                if (departementStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un numéro de département", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int numDep = Integer.parseInt(departementStr);
                    if (numDep < 1 || (numDep > 95 && numDep < 971) || numDep > 976) {
                        Toast.makeText(MainActivity.this, "Numéro de département invalide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fetchNumDep(numDep);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Erreur : Veuillez entrer un nombre valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPraticien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String praticienStr = saisiPraticien.getText().toString().trim();
                if (praticienStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un ou plusieur caractère",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    String nomPrat = Integer.toString(Integer.parseInt(praticienStr));
                    if (nomPrat.contains("01234566789")) {
                        Toast.makeText(MainActivity.this, "Veuillez ne pas entrer un nombre",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fetchNomPraticien(nomPrat);
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.this,"Erreur : Veuillez ne pas entrer un nombre.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public TextView fetchNumDep(int numdep){
        String url = "https://gsb.siochaptalqper.fr/praticiens/numdep/" + numdep ;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String num = jsonObject.optString("PRA_NUM", "Inconnu");
                                String nom = jsonObject.optString("PRA_NOM", "Inconnu");
                                String prenom = jsonObject.optString("PRA_PRENOM", "Inconnu");
                                String add = jsonObject.optString("PRA_ADRESSE", "Inconnue");
                                String cp = jsonObject.optString("PRA_CP", "Inconnue");
                                String ville = jsonObject.optString("PRA_VILLE", "Inconnue");
                                String libelle = jsonObject.optString("TYP_LIBELLE", "Inconnue");
                                String notoriete = jsonObject.optString("PRA_COEFNOTORIETE", "Inconnue");
                                textViewDep.append("\n" + "Numéro : " + num + "\n" + "Nom : " + nom + "\n" + "Prénom : " + prenom + "\n" + "Adresse : " + add + "\n" + "Code postal : " + cp+ "\n" + "Ville : " + ville + "\n" + "Libelle :" + libelle + "\n" + "Notoriété : " + notoriete + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Erreur d'analyse JSON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
        return textViewDep;
    }
    public TextView fetchNomPraticien(String nomPrat){
        String url = "https://gsb.siochaptalqper.fr/praticiens/nom/" + nomPrat ;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String num = jsonObject.optString("PRA_NUM", "Inconnu");
                                String nom = jsonObject.optString("PRA_NOM", "Inconnu");
                                String prenom = jsonObject.optString("PRA_PRENOM", "Inconnu");
                                String add = jsonObject.optString("PRA_ADRESSE", "Inconnue");
                                String cp = jsonObject.optString("PRA_CP", "Inconnue");
                                String ville = jsonObject.optString("PRA_VILLE", "Inconnue");
                                String libelle = jsonObject.optString("TYP_LIBELLE", "Inconnue");
                                String notoriete = jsonObject.optString("PRA_COEFNOTORIETE", "Inconnue");
                                textViewNom.append("\n" + "Numéro : " + num + "\n" + "Nom : " + nom + "\n" + "Prénom : " + prenom + "\n" + "Adresse : " + add + "\n" + "Code postal : " + cp+ "\n" + "Ville : " + ville + "\n" + "Libelle :" + libelle + "\n" + "Notoriété : " + notoriete + "\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Erreur d'analyse JSON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Erreur: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
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