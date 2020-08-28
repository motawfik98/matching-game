package com.example.matchinggame;

import androidx.annotation.Nullable;

public class Animal {
    private int image;
    private int sound;

    public Animal(int image, int sound) {
        this.image = image;
        this.sound = sound;
    }

    public int getImage() {
        return image;
    }

    public int getSound() {
        return sound;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        Animal animal = (Animal) obj;
        return image == animal.image && sound == animal.sound;
    }
}
