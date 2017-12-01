package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class CardLab {

    private static CardLab cardLab;

    public static CardLab newInstance() {
        if (cardLab == null) {
            cardLab = new CardLab();
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
}
