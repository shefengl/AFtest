package com.mycompany.aftest.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mycompany.aftest.R;
import com.mycompany.aftest.model.Promotions;

import java.net.URL;

/**
 * Created by shefeng on 6/28/2016.
 */
public class PromotionsActivity extends Activity {
    TextView footer,description,title;
    Button button;
    ImageView image_promotion;
    WebView webView;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    NetworkImageView networkImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_card);
//        image_promotion = (ImageView) findViewById(R.id.imageView);
        footer= (TextView) findViewById(R.id.promotion_footer);
        description= (TextView) findViewById(R.id.description_promotion);
        title= (TextView) findViewById(R.id.title_promotion);
        button= (Button) findViewById(R.id.button);
//        webView = (WebView) findViewById(R.id.webView);
        networkImageView =(NetworkImageView)findViewById(R.id.imageView);
        final Promotions data = (Promotions) getIntent().getExtras().getSerializable("data");
        URL newurl = null;
        if(imageLoader == null){
            imageLoader = AppController.getInstance().getImageLoader();

        }
        networkImageView.setImageUrl(data.getThumbnailUrl(),imageLoader);
//        try {
//            newurl =  new URL(data.getPromotion_image_url());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        Picasso.with(this).load(String.valueOf(newurl)).into(image_promotion);
        description.setText("" + data.getDescription());
        footer.setText(""+data.getFooter());
        title.setText(""+data.getName());
        button.setText(""+data.getButtonTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getPromotion_button_target()));
                //startActivity(intent);
                Intent intent = new Intent(PromotionsActivity.this,WebviewActivity.class);
                intent.putExtra("url",data.getButtonTarget());
                startActivity(intent);
            }
        });



    }
}
