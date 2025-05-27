package com.example.serviceweb;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private TextView textViewDep;
    private TextView textViewNom;
    private Button btnPraticien;
    private Button btnDepartement;
    private EditText saisiPraticien;
    private EditText saisiDepartement;
    private RequestQueue requestQueue;
    private Spinner spinnerPraticiens;
    private  Spinner spinnerDepartements;

    private void initView()
    {
        saisiPraticien = findViewById(R.id.saisiPraticien);
        saisiDepartement = findViewById(R.id.saisiDepartement);
        textViewDep = findViewById(R.id.textViewDep);
        textViewNom = findViewById(R.id.textViewNom);
        spinnerPraticiens = findViewById(R.id.spinnerPraticiens);
        spinnerDepartements = findViewById(R.id.spinnerDepartements);
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
                    Toast.makeText(MainActivity.this, "Veuillez entrer un ou plusieurs caractères", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!praticienStr.matches("[a-zA-Z]+")) { // Vérifie si la chaîne contient uniquement des chiffres
                    Toast.makeText(MainActivity.this, "Veuillez entrer un nom valide.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    fetchNomPraticien(praticienStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Erreur : Veuillez ne pas entrer un nombre.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public Spinner fetchNumDep(int numdep){
        String url = "https://gsb.siochaptalqper.fr/praticiens/numdep/" + numdep ;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> praticiensList = new ArrayList<>();
                        ArrayList<String> praticiensDetails = new ArrayList<>(); // Pour stocker les détails

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

                                // Stocker les détails du praticien
                                String details = "Numéro : " + num + "\nNom : " + nom + "\nPrénom : " + prenom +
                                        "\nAdresse : " + add + "\nCode postal : " + cp +
                                        "\nVille : " + ville + "\nLibelle : " + libelle +
                                        "\nNotoriété : " + notoriete;
                                praticiensDetails.add(details); // Ajouter les détails à la liste
                                praticiensList.add(nom + " " + prenom);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Erreur d'analyse JSON", Toast.LENGTH_SHORT).show();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, praticiensList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDepartements.setAdapter(adapter);
                        spinnerDepartements.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                                // Récupérer l'élément sélectionné
                                String selectedDetails = praticiensDetails.get(position); // Récupérer les détails
                                // Afficher l'élément sélectionné dans le TextView
                                textViewDep.setText(selectedDetails); // Afficher les détails
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Ne rien faire
                            }
                        });
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
        return spinnerDepartements;
    }

    public Spinner fetchNomPraticien(String nomPrat) {
        String url = "https://gsb.siochaptalqper.fr/praticiens/nom/" + nomPrat;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> praticiensList = new ArrayList<>();
                        ArrayList<String> praticiensDetails = new ArrayList<>(); // Pour stocker les détails

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

                                // Stocker les détails du praticien
                                String details = "Numéro : " + num + "\nNom : " + nom + "\nPrénom : " + prenom +
                                        "\nAdresse : " + add + "\nCode postal : " + cp +
                                        "\nVille : " + ville + "\nLibelle : " + libelle +
                                        "\nNotoriété : " + notoriete;
                                praticiensDetails.add(details); // Ajouter les détails à la liste
                                praticiensList.add(nom + " " + prenom);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Erreur d'analyse JSON", Toast.LENGTH_SHORT).show();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, praticiensList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPraticiens.setAdapter(adapter);
                        spinnerPraticiens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                                // Récupérer l'élément sélectionné
                                String selectedDetails = praticiensDetails.get(position); // Récupérer les détails
                                // Afficher l'élément sélectionné dans le TextView
                                textViewNom.setText(selectedDetails); // Afficher les détails
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Ne rien faire
                            }
                        });
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
        return spinnerPraticiens;
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