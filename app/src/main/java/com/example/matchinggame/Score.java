package com.example.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.matchinggame.databinding.ActivityScoreBinding;

public class Score extends AppCompatActivity {
    ActivityScoreBinding binding;
    TextView score, seconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        score = binding.score;
        seconds = binding.seconds;
        Intent intent = getIntent();
        int time = (int) intent.getLongExtra("currentScore", 0);
        int maxScoreTime = 20;
        int bonusPerSecond = 50;
        int score = Math.max(0, maxScoreTime - time) * bonusPerSecond + time;
        this.score.setText(String.valueOf(score));
        StringBuilder sb = new StringBuilder().append(seconds.getText()).append(" ").append(time);
        this.seconds.setText(sb.toString());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}