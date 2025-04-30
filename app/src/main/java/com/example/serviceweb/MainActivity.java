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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
                Request.Method.GET, "https://gsb.siochaptalqper.fr/praticiens", null, response -> { }, error -> { }
        );


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
        btnPraticien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmIdStr = textViewNom.getText().toString().trim();

                if (filmIdStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un numéro de film",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int filmId = Integer.parseInt(filmIdStr);
                if (filmId < 1 || filmId > 6) {
                    Toast.makeText(MainActivity.this, "Numéro de film doit être entre 1 et 6",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //fetchFilmDetails(filmId);
            }
        });
    }
}