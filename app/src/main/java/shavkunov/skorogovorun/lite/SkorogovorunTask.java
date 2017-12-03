package shavkunov.skorogovorun.lite;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import shavkunov.skorogovorun.lite.model.Card;

public class SkorogovorunTask extends AsyncTask<Void, Void, Card[]> {

    private static final String HTTP_REQUEST = "httpRequest";
    private String url;

    private Card[] cards;

    @Override
    protected Card[] doInBackground(Void... params) {
        try {
            final String url = getUrl();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Card[]> responseEntity = restTemplate.getForEntity(url, Card[].class);
            return responseEntity.getBody();
        } catch (Exception e) {
            Log.e(HTTP_REQUEST, "The exception was caught in SkorogovorunTask");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Card[] c) {
        if (c != null) {
            cards = Arrays.copyOf(c, c.length);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Card[] getCards() {
        return cards;
    }
}
