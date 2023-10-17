package com.example.vhr.DoctorActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorsResearchActivity extends AppCompatActivity {
    private EditText Name;
    private EditText FirstName;
    private EditText City;
    private EditText Department;
    private OkHttpClient client = new OkHttpClient();
    private static final String URL_API_BASE = "https://production.api-annuaire-sante.fr/";
    private static final String URL_API_REQUEST = "https://production.api-annuaire-sante.fr/professionnel_de_santes";
    private String token;
    private List<Doctor> answerDoctorResearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_doctors);

        Init();

        Button ButtonResearch = findViewById(R.id.ButtonResearch);
        ButtonResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorsResearchActivity.this, "Recherche en cours", Toast.LENGTH_LONG).show();
                new ResearchAsyncTask().execute();
                updateListView();
            }
        });

        Button buttonResearchBack = findViewById(R.id.buttonResearchBack);
        buttonResearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TreatingDoctorActivity.class);
                startActivity(intent);
            }
        });
    }

    // We initialize the EditText and retrieve the connection token
    protected void Init(){
        Name = findViewById(R.id.DoctorLastName);
        FirstName = findViewById(R.id.DoctorFirstName);
        City = findViewById(R.id.DoctorCity);
        Department = findViewById(R.id.DoctorDepartment);
        answerDoctorResearch = new ArrayList<Doctor>();

        //We initialize the display of the list of doctors returned in response
        updateListView();

        try {
            recoveryTokenConnexion();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // AsyncTask to search in the background
    private class ResearchAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                research();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateListView();
        }
    }

    // Method to search for a doctor based on user input
    protected void research() throws IOException {
        if (token == null || token.isEmpty()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DoctorsResearchActivity.this, "The token is empty or null. Unable to perform search.", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        //We add the information on the doctor sought in the url
        String doctorInfoURL = "";

        String doctorName = FirstName.getText().toString().trim();
        if(!doctorName.isEmpty()){
            doctorInfoURL += "prenom="+doctorName;
        }

        String doctorFirstName = Name.getText().toString().trim();
        if(!doctorFirstName.isEmpty()){
            if(!doctorName.isEmpty()){
                doctorInfoURL += "&";
            }
            doctorInfoURL += "nom="+doctorFirstName;
        }

        String doctorDepartment = Department.getText().toString().trim();
        if(!doctorDepartment.isEmpty()){
            if(!doctorName.isEmpty() || !doctorFirstName.isEmpty()){
                doctorInfoURL += "&";
            }
            doctorInfoURL += "libelleDepartement="+doctorDepartment;
        }

        String doctorCity = City.getText().toString().trim();
        if(!doctorCity.isEmpty()){
            if(!doctorName.isEmpty() || !doctorFirstName.isEmpty() || !doctorDepartment.isEmpty()){
                doctorInfoURL += "&";
            }
            doctorInfoURL += "libelleCommune="+doctorCity;
        }

        String researchURL = "";
        if (!doctorInfoURL.isEmpty()){
            researchURL = "?"+doctorInfoURL;
        }

        // We build the query
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_API_REQUEST+researchURL)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                  .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseData = response.body().string();

            try {
                JSONObject jsonResponse = new JSONObject(responseData);
                JSONArray doctorsArray = jsonResponse.getJSONArray("hydra:member");
                if(doctorsArray.length() == 0){
                    Toast.makeText(DoctorsResearchActivity.this, "No matching doctor", Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < doctorsArray.length(); i++) {
                    JSONObject doctor = doctorsArray.getJSONObject(i);

                    int id = doctor.getInt("id");
                    String prenom = doctor.getString("prenom");
                    String nom = doctor.getString("nom");
                    String libelleCommune = doctor.getString("libelleCommune");
                    String identificationNationalePP = doctor.getString("identificationNationalePP");
                    String savoirFaire = doctor.getString("libelleSavoirFaire");
                    String profession = doctor.getString("libelleProfession");
                    String numeroVoie =  doctor.getString("numeroVoie");
                    String libelleVoie = doctor.getString("libelleVoie");
                    String libelleTypeVoie = doctor.getString("libelleTypeVoie");
                    String bureauCedex = doctor.getString("bureauCedex");
                    String codePostal = doctor.getString("codePostal");

                    Doctor tmp_Doctor = new Doctor(id,prenom,nom,libelleCommune,identificationNationalePP,savoirFaire,profession,numeroVoie,
                            libelleVoie,libelleTypeVoie,bureauCedex,codePostal);
                    answerDoctorResearch.add(tmp_Doctor);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error", "Error parsing JSON : " + e.getMessage());
            }
        }
    }

    protected void recoveryTokenConnexion() throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"username\": \"bejo62192@eleve.isep.fr\",\"password\": \"XtMR4fiEaMB!!9A\"}");

        Request request = new Request.Builder()
                .url(URL_API_BASE + "login_check")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String content = response.body().string();
                        JSONObject result = new JSONObject(content);
                        token = result.getString("token");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DoctorsResearchActivity.this, "Connection problem", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void updateListView() {
        DisplayResearchDoctorComponent adapter = new DisplayResearchDoctorComponent(this, answerDoctorResearch);
        ListView listViewAnswerDoctors = findViewById(R.id.listViewAnswerDoctors);
        listViewAnswerDoctors.setAdapter(adapter);
    }
}
