package com.raoul.founditt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SearchresultActivity extends Activity {
     String title;
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        Intent datagetintent = getIntent();
        title= datagetintent.getStringExtra("catagory");
        content= datagetintent.getStringExtra("contente");
        TextView titletextview=(TextView)findViewById(R.id.search_catagorytextview);
        TextView contenttextview=(TextView)findViewById(R.id.catagory_content_textView);
        titletextview.setText(title);
        contenttextview.setText(content);
        Button fillterButton = (Button) findViewById(R.id.return_button);

        fillterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(SearchresultActivity.this, SearchActivity.class);


                startActivity(intent);

            }
        });

    }



}
