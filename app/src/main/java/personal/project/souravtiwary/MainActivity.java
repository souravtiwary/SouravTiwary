package personal.project.souravtiwary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_video;
    private Button btn_volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_volley = findViewById(R.id.volley_btn);
        btn_volley.setOnClickListener(MainActivity.this);

        btn_video = findViewById(R.id.video_btn);
        btn_video.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.volley_btn:
                startActivity(new Intent(MainActivity.this, VolleyClass.class));
                break;

            case R.id.video_btn:
                startActivity(new Intent(MainActivity.this, VideoPlayActivity.class));
                break;
        }
    }
}