package au.com.realestate.hometime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import au.com.realestate.hometime.Adapter.ListAdapter;
import au.com.realestate.hometime.models.ApiResponse;
import au.com.realestate.hometime.models.Token;
import au.com.realestate.hometime.models.Tram;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    TextView txtNoDataNorth;
    TextView txtNoDataSouth;
    private List<Tram> southTrams;
    private List<Tram> northTrams;
    private RecyclerView northRecyclerView;
    private RecyclerView southRecyclerView;
    private ListAdapter northListAdapter;
    private ListAdapter southListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        northRecyclerView = (RecyclerView) findViewById(R.id.northRecyclerView);
        southRecyclerView = (RecyclerView) findViewById(R.id.southRecyclerView);
        txtNoDataNorth = (TextView) findViewById(R.id.txt_nodata_north);
        txtNoDataSouth = (TextView) findViewById(R.id.txt_nodata_south);
    }

    public void refreshClick(View view) {

        TramsApi tramsApi = createApiClient();

        try {
            String token = new RequestToken(tramsApi).execute("").get();
            this.northTrams = new RequestTrams(tramsApi, token).execute("4055").get();
            this.southTrams = new RequestTrams(tramsApi, token).execute("4155").get();
            showTrams();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void clearClick(View view) {
        northTrams = new ArrayList<>();
        southTrams = new ArrayList<>();
        showTrams();
    }

    private void showTrams() {

        // north data set
        northListAdapter = new ListAdapter(northTrams, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        northRecyclerView.setLayoutManager(mLayoutManager);
        northRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, LinearLayout.VERTICAL);
        northRecyclerView.addItemDecoration(dividerItemDecoration);
        northRecyclerView.setAdapter(northListAdapter);


        //sout data set
        southListAdapter = new ListAdapter(southTrams, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(MainActivity.this);
        southRecyclerView.setLayoutManager(mLayoutManager1);
        southRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(MainActivity.this, LinearLayout.VERTICAL);
        southRecyclerView.addItemDecoration(dividerItemDecoration1);
        southRecyclerView.setAdapter(southListAdapter);


        // show no data text
        if(northTrams.size()>0){
            txtNoDataNorth.setVisibility(View.GONE);
        }else {
            txtNoDataNorth.setVisibility(View.VISIBLE);
        }
        if(southTrams.size()>0){
            txtNoDataSouth.setVisibility(View.GONE);
        }else {
            txtNoDataSouth.setVisibility(View.VISIBLE);
        }

    }


    ////////////
    // API
    ////////////

    private TramsApi createApiClient() {

        String BASE_URL = "http://ws3.tramtracker.com.au";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        return retrofit.create(TramsApi.class);
    }

    private interface TramsApi {

        @GET("/TramTracker/RestService/GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTimeAndroid")
        Call<ApiResponse<Token>> token();

        @GET("/TramTracker/RestService//GetNextPredictedRoutesCollection/{stopId}/78/false/?aid=TTIOSJSON&cid=2")
        Call<ApiResponse<Tram>> trams(
                @Path("stopId") String stopId,
                @Query("tkn") String token
        );
    }

    private class RequestToken extends AsyncTask<String, Integer, String> {

        TramsApi api;

        RequestToken(TramsApi api) {
            this.api = api;
        }

        @Override
        protected String doInBackground(String... params) {
            Call<ApiResponse<Token>> call = api.token();
            try {
                return call.execute().body().responseObject.get(0).value;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class RequestTrams extends AsyncTask<String, Integer, List<Tram>> {

        private TramsApi api;
        private String token;

        RequestTrams(TramsApi api, String token) {
            this.api = api;
            this.token = token;
        }

        @Override
        protected List<Tram> doInBackground(String... stops) {

            Call<ApiResponse<Tram>> call = api.trams(stops[0], token);
            try {
                Response<ApiResponse<Tram>> resp = call.execute();
                return resp.body().responseObject;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
