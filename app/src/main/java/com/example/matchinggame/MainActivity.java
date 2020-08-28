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

        Arrays.fill(displayImages, R.drawable.card); // make the displayImages array full of cards
        animals = Animal.getAnimals(); // gets the animals list

        for (int i = 0; i < 8; i++) {
            buttons[i] = view.findViewWithTag(String.valueOf(i)); // get the button by its tag
            buttons[i].setMaxWidth(buttons[i].getWidth()); // restrict the button's dimensions
            buttons[i].setMaxHeight(buttons[i].getHeight());
            buttons[i].setOnClickListener(new View.OnClickListener() { // add click listener to handle the click
                @Override
                public void onClick(View view) {
                    handleClick(view);
                }
            });
        }
    }

    void handleClick(View view) {
        if (player != null) // if there's a player object, stop the sound to avoid conflicts
            player.stop();
        if (handlerRunning) { // if there's a handler (delay) function running
            handler.removeCallbacksAndMessages(null); // remove the handler from the system
            handlerCode(); // perform the code that should've been performed at the end of the delay
            handlerRunning = false;
        }
        final int value = Integer.parseInt(view.getTag().toString()); // get the tag (index) of the clicked button
        if (previousAnimalIndex == value) // if the clicked animal is the same as the last animal
            return; // do nothing and return
        previousAnimalIndex = value; // update the last pressed animal
        if (firstAnimalIndex == -1) { // if there's no first animal
            firstAnimalIndex = value; // set the value of the first animal to the clicked animal
        } else if (secondAnimalIndex == -1) { // if there's no second animal
            secondAnimalIndex = value; //set the value of the second animal to the clicked animal
        }

        changeImageButton(value, animals.get(value).getImage()); // change the image of the clicked button
        player = MediaPlayer.create(MainActivity.this, animals.get(value).getSound());
        player.start(); // create and start the sound of the selected animal

        if (secondAnimalIndex != -1) { // if there are two animals displayed (clicked)
            handlerRunning = true; // indicate that there's a handler running
            match = animals.get(firstAnimalIndex).equals(animals.get(secondAnimalIndex)); // boolean to hold if the two animals are equal or not
            delay(3000); // add handler (delay) with 3000 ms (3 seconds)
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
        if (match) { // if the two animals are the same
            // change the visibility of both of them to INVISIBLE
            changeImageButton(firstAnimalIndex, 0);
            changeImageButton(secondAnimalIndex, 0);
        } else { // the two animals are different
            // change the image of both of them to cards (flip them over)
            changeImageButton(firstAnimalIndex, R.drawable.card);
            changeImageButton(secondAnimalIndex, R.drawable.card);
        }
        // reset the used variables at the end
        firstAnimalIndex = -1;
        secondAnimalIndex = -1;
        previousAnimalIndex = -1;
        handlerRunning = false;
    }

    void changeImageButton(int index, int image) {
        displayImages[index] = image; // update displayImages to be able to preserve their state
        if (image == 0) // if the image is equal to zero, remove its visibility
            buttons[index].setVisibility(View.INVISIBLE);
        else // set the button's image to the given image
            buttons[index].setImageResource(image);
    }


    @Override
    protected void onDestroy() {
        if (player != null) // stop the player on destroying the activity
            player.stop();
        super.onDestroy();
    }

    // override to preserve the state of the important variables
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

    // get the state of the preserved variables
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
                // update buttons to display the displayed images before rotation
                if (displayImages[i] == 0)
                    buttons[i].setVisibility(View.INVISIBLE);
                else
                    buttons[i].setImageResource(displayImages[i]);
            }
        // if the user switched the orientation and a handler was running
        if (handlerRunning)
            delay(1000); // perform the delay function but with smaller delay (1000 ms -> 1 sec)
    }
}