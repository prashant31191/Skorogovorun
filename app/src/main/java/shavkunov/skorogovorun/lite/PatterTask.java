package shavkunov.skorogovorun.lite;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import shavkunov.skorogovorun.lite.model.Patter;

public class PatterTask extends AsyncTask<Void, Void, Patter[]> {

    private static final String REQUEST = "request";

    private Patter[] patters;

    @Override
    protected Patter[] doInBackground(Void... params) {
        try {
            final String url = "https://api.myjson.com/bins/15qa8b";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Patter[]> responseEntity = restTemplate.getForEntity(url, Patter[].class);
            return responseEntity.getBody();
        } catch (Exception e) {
            Log.e(REQUEST, "The exception was caught in PatterTask");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Patter[] p) {
        if (p != null) {
            patters = Arrays.copyOf(p, p.length);
        }
    }

    public Patter[] getPatters() {
        return patters;
    }
}
