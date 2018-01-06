package shavkunov.skorogovorun.lite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class CardLab {

    private volatile static CardLab cardLab;

    private CardLab() {
    }

    public static CardLab newInstance() {
        if (cardLab == null) {
            synchronized (CardLab.class) {
                if (cardLab == null) {
                    cardLab = new CardLab();
                }
            }
        }
        return cardLab;
    }

    public void setImage(View itemView, @DrawableRes int imageRes, ImageView imageView) {
        Glide.with(itemView)
                .load(imageRes)
                .apply(RequestOptions.bitmapTransform(
                        new ColorFilterTransformation(Color.argb(210, 66, 66, 66))))
                .into(imageView);
    }

    public void setIntent(Activity activity, Class<?> cls, int requestCode) {
        Intent intent = new Intent(activity, cls);
        activity.startActivityForResult(intent, requestCode);
    }

    public void setIntent(Activity activity, Class<?> cls, String tag,
                          String extraString, int requestCode) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(tag, extraString);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Проверяет, подключено ли устройство к интернету
     *
     * @return true, если устройство имеет доступ к сети
     */
    public boolean isConnectedToNetwork(Activity activity) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                connected = ni.isConnected();
            }
        }

        return connected;
    }
}
