package example.com.asd4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShowActivity extends AppCompatActivity {

    private TextView mText1;
    private ImageView mImg1;
    private String aaa;
    private String sss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent intent = getIntent();
        aaa = intent.getStringExtra("aaa");
        sss = intent.getStringExtra("sss");
        initView();

    }

    public void initView() {
        mText1 = findViewById(R.id.text_1);
        mImg1 = findViewById(R.id.img_1);
        mText1.setText(aaa);
        Glide.with(ShowActivity.this).load(sss).into(mImg1);





    }

}
