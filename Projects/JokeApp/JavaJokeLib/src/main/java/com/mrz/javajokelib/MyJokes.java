package com.mrz.javajokelib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Step 1:
public class MyJokes {
    private List<String> jokes;


    // Jokes taken from https://www.rd.com/jokes/riddles/ - Hey I didn't say they were great, ok.
    public MyJokes() {
        jokes = new ArrayList<>();
        jokes.add("What five-letter word becomes shorter if you add two letters to it? Short.");
        jokes.add("I have a neck, but no head and I wear a cap. What am I? A bottle!");
        jokes.add("You can break me without touching me or even seeing me. What am I? A promise.");
        jokes.add("How many seconds are there in a year? Twelve - Jan 2nd, Feb 2nd...");
        jokes.add("How do you make the number one disappear? Add the letter G and then presto - it's gone!");
        jokes.add("How many cats can you put in an empty box? One, after that the box isn't empty anymore.");
        jokes.add("What has one head, one foot and four legs? A bed!");
        jokes.add("Why was the chef embarrassed? Because he saw the salad dressing!");
        jokes.add("Where is the ocean the deepest? On the bottom.");
        jokes.add("What do you break before  you use it? An egg!");
    }

    public String pickOne() {
        Random random = new Random();
        return jokes.get(random.nextInt(jokes.size()));
    }
}
