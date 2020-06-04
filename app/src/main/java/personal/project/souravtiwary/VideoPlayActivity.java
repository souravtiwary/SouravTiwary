package personal.project.souravtiwary;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

//this class does not use MediaController all things are done manually
public class VideoPlayActivity extends AppCompatActivity {

    VideoView videoView;
    ImageView prev, next, pause, rotate, mute;
    SeekBar seekBar;
    double current_pos, total_duration;
    TextView current, total;
    LinearLayout showProgress;
    Handler mHandler,handler;
    RelativeLayout relativeLayout;
    String medianame = "tacoma_narrows";
    boolean isVisible = true;
    int aux = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setVideo();
    }

    public void setVideo(){
        videoView = (VideoView) findViewById(R.id.videoview);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
        pause = (ImageView) findViewById(R.id.pause);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        showProgress = (LinearLayout) findViewById(R.id.showProgress);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        rotate = (ImageView) findViewById(R.id.rotate);
        mute = (ImageView) findViewById(R.id.mute);



        mHandler = new Handler();
        handler = new Handler();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoPlayActivity.this, "Play Complete", Toast.LENGTH_SHORT).show();
                videoView.seekTo(1);
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
            }
        });

        playVideo();
        prevTenSec();
        forwardTenSec();
        setPause();
        hideLayout();
        rotateScreen();
        muteVideo();
    }



    //playing video
    public void playVideo(){
        try {
            Uri videopath = Uri.parse("android.resource://"+getPackageName()
                    +"/raw/"+medianame);
            videoView.setVideoURI(videopath);
            videoView.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //display video progress
    public void setVideoProgress() {
        //getting video duration
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();

        total.setText(timeConversion((long) total_duration));
        current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });

    }

    public void forwardTenSec(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_pos<(videoView.getDuration()-10000)){
                    current.setText(timeConversion((long) (current_pos+10000)));
                    long change = (long) (current_pos + (long)10000);
                    Log.d("forward", "onClick: "+ timeConversion(change));
                    videoView.seekTo((int) change);
                }
            }
        });
    }

    public void prevTenSec(){
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_pos < (videoView.getDuration()+10000)){
                    current.setText(timeConversion((long) (current_pos-10000)));
                    long change = (long) (current_pos - (long)10000);
                    Log.d("prev", "onClick: "+ timeConversion(change));
                    videoView.seekTo((int) change);
                }
            }
        });
    }

    public void setPause(){
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    pause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                } else {
                    videoView.start();
                    pause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                }
            }
        });
    }

    public String timeConversion(long value){
        String videotime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videotime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videotime = String.format("%02d:%02d", mns, scs);
        }
        return videotime;
    }

    public void hideLayout() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showProgress.setVisibility(View.GONE);
                isVisible = false;
            }
        };
        handler.postDelayed(runnable, 5000);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                if (isVisible) {
                    showProgress.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    showProgress.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(runnable, 5000);
                    isVisible = true;
                }
            }
        });

    }

    public void rotateScreen(){
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                final int orientation = getRequestedOrientation();
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
    }

    public void muteVideo(){
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                if(aux % 2 == 0){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    aux++;
                    mute.setImageResource(R.drawable.ic_baseline_volume_up_24);
                } else {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    aux++;
                    mute.setImageResource(R.drawable.ic_baseline_volume_off_24);
                }
            }
        });
    }

}