package com.example.matchinggame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import com.example.matchinggame.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Animal> animals;
    ImageButton[] buttons = new ImageButton[8];
    int[] displayImages = new int[8];
    int firstAnimalIndex = -1, secondAnimalIndex = -1, previousAnimalIndex = -1;
    MediaPlayer player;
    boolean handlerRunning = false, match = false, shuffled = false;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Arrays.fill(displayImages, R.drawable.card);
        animals = Animal.getAnimals();

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
        if (previousAnimalIndex == value)
            return;
        previousAnimalIndex = value;
        if (firstAnimalIndex == -1) {
            firstAnimalIndex = value;
        } else if (secondAnimalIndex == -1) {
            if (firstAnimalIndex != value)
                secondAnimalIndex = value;
        }

        changeImageButton(value, animals.get(value).getImage());
        player = MediaPlayer.create(MainActivity.this, animals.get(value).getSound());
        player.start();

        if (secondAnimalIndex != -1) {
            handlerRunning = true;
            match = animals.get(firstAnimalIndex).equals(animals.get(secondAnimalIndex));
            delay(3000);
        }
    }

    void delay(int time) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handlerCode();
            }
        }, time);
    }

    void handlerCode() {
        if (match) {
            changeImageButton(firstAnimalIndex, 0);
            changeImageButton(secondAnimalIndex, 0);
        } else {
            changeImageButton(firstAnimalIndex, R.drawable.card);
            changeImageButton(secondAnimalIndex, R.drawable.card);
        }
        firstAnimalIndex = -1;
        secondAnimalIndex = -1;
        previousAnimalIndex = -1;
        handlerRunning = false;
    }

    void changeImageButton(int index, int image) {
        displayImages[index] = image;
        if (image == 0)
            buttons[index].setVisibility(View.INVISIBLE);
        else
            buttons[index].setImageResource(image);
    }


    @Override
    protected void onDestroy() {
        if (player != null)
            player.stop();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("displayImages", displayImages);

        outState.putInt("firstAnimalIndex", firstAnimalIndex);
        outState.putInt("secondAnimalIndex", secondAnimalIndex);
        outState.putInt("previousAnimalIndex", previousAnimalIndex);
        outState.putBoolean("handlerRunning", handlerRunning);
        outState.putBoolean("match", match);
        outState.putBoolean("shuffled", shuffled);
        outState.putParcelableArrayList("animals", animals);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        displayImages = savedInstanceState.getIntArray("displayImages");
        firstAnimalIndex = savedInstanceState.getInt("firstAnimalIndex");
        secondAnimalIndex = savedInstanceState.getInt("secondAnimalIndex");
        previousAnimalIndex = savedInstanceState.getInt("previousAnimalIndex");
        handlerRunning = savedInstanceState.getBoolean("handlerRunning");
        match = savedInstanceState.getBoolean("match");
        animals = savedInstanceState.getParcelableArrayList("animals");

        if (displayImages != null)
            for (int i = 0; i < displayImages.length; i++) {
                if (displayImages[i] == 0)
                    buttons[i].setVisibility(View.INVISIBLE);
                else
                    buttons[i].setImageResource(displayImages[i]);
            }
        if (handlerRunning)
            delay(1000);
    }
}