package com.example.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.matchinggame.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Animal> animals = Arrays.asList(
            new Animal(R.drawable.dog, R.raw.chasdog),
            new Animal(R.drawable.dog, R.raw.chasdog),
            new Animal(R.drawable.lion, R.raw.lion4),
            new Animal(R.drawable.lion, R.raw.lion4),
            new Animal(R.drawable.monkey, R.raw.monkeypatas),
            new Animal(R.drawable.monkey, R.raw.monkeypatas),
            new Animal(R.drawable.elephant, R.raw.elephant9),
            new Animal(R.drawable.elephant, R.raw.elephant9)
    );
    ImageButton[] buttons = new ImageButton[8];
    int firstAnimalIndex = -1, secondAnimalIndex = -1;
    MediaPlayer player;
    boolean handlerRunning = false, match = false;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Collections.shuffle(animals);
        for (int i = 0; i < 8; i++) {
            buttons[i] = view.findViewWithTag(String.valueOf(i));
            buttons[i].setMaxWidth(buttons[i].getWidth());
            buttons[i].setMaxHeight(buttons[i].getHeight());
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleClick(view);
                }
            });
        }
    }

    void handleClick(View view) {
        if (player != null)
            player.stop();
        if (handlerRunning) {
            handler.removeCallbacksAndMessages(null);
            handlerCode();
            handlerRunning = false;
        }
        final int value = Integer.parseInt(view.getTag().toString());
        if (firstAnimalIndex == -1) {
            firstAnimalIndex = value;
        } else if (secondAnimalIndex == -1) {
            secondAnimalIndex = value;
        }
        buttons[value].setImageResource(animals.get(value).getImage());
        player = MediaPlayer.create(MainActivity.this, animals.get(value).getSound());
        player.start();

        if (secondAnimalIndex != -1) {
            handlerRunning = true;
            match = animals.get(firstAnimalIndex).equals(animals.get(secondAnimalIndex));
            delay();
        }
    }

    void delay() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handlerCode();
            }
        }, 3000);
    }

    void handlerCode() {
        if (match) {
            buttons[firstAnimalIndex].setVisibility(View.INVISIBLE);
            buttons[secondAnimalIndex].setVisibility(View.INVISIBLE);
        } else {
            buttons[firstAnimalIndex].setImageResource(R.drawable.card);
            buttons[secondAnimalIndex].setImageResource(R.drawable.card);
        }
        firstAnimalIndex = -1;
        secondAnimalIndex = -1;
        handlerRunning = false;
    }

    @Override
    protected void onDestroy() {
        if (player != null)
            player.stop();
        super.onDestroy();
    }
}