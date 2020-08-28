package com.example.matchinggame;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

// Animal class to hold the image and sound for the animal
public class Animal implements Parcelable {
    private int image;
    private int sound;
    private static ArrayList<Animal> animals; // initialize the animals array without the user interaction

    private Animal(int image, int sound) {
        this.image = image;
        this.sound = sound;
    }
    // implementing Parcelable to be able tp preserve the state of the shuffled animals list
    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

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
        if (!(obj instanceof Animal))
            return false;
        Animal animal = (Animal) obj;
        return image == animal.image && sound == animal.sound;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(image);
        parcel.writeInt(sound);
    }

    private Animal(Parcel in) {
        image = in.readInt();
        sound = in.readInt();
    }
    // add 2 instances of each animal in the list then shuffle it
    public static ArrayList<Animal> getAnimals() {
        if (animals == null) {
            animals = new ArrayList<>();
            animals.add(new Animal(R.drawable.dog, R.raw.chasdog));
            animals.add(new Animal(R.drawable.dog, R.raw.chasdog));
            animals.add(new Animal(R.drawable.lion, R.raw.lion4));
            animals.add(new Animal(R.drawable.lion, R.raw.lion4));
            animals.add(new Animal(R.drawable.monkey, R.raw.monkeypatas));
            animals.add(new Animal(R.drawable.monkey, R.raw.monkeypatas));
            animals.add(new Animal(R.drawable.elephant, R.raw.elephant9));
            animals.add(new Animal(R.drawable.elephant, R.raw.elephant9));
            Collections.shuffle(animals);
        }
        return animals;
    }
}
