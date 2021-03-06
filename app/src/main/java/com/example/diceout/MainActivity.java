package com.example.diceout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    // Field to send final score to FinalScoreActivity
    public static final String EXTRA_FINAL_SCORE = "com.example.diceout.FINAL_SCORE";
    //Keys for savedInstanceState
    static final String STATE_SCORE = "playerScore";
    static final String STATE_ROLLS = "playerTurns";
    static final String STATE_MSG = "playerMessage";
    static final String STATE_LIST = "playerDice";

    // Field to hold the roll result text
    TextView rollResult;

    // Field to hold the score
    int score;

    //Field to hold the number of turns left
    int turns;

    //field to hold the turns text
    TextView turnsText;

    // Field to hold random number generator
    Random rand;

    // Fields to hold the die values
    int die1,die2,die3,die4,die5;

    // Build message with the result
    String msg;

    //field to hold the score text
    TextView scoreText;

    // ArrayList to hold all three dice ImageViews
    ArrayList<ImageView> diceImageViews;

    // ArrayList to hold all three die values
    ArrayList<Integer> dice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //fab = floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice(view);
            }
        });


        // Set initial score and turns
        score = 0;
        turns = 10;

        // Create greeting

         if (savedInstanceState == null)
            Toast.makeText(getApplicationContext(), "Welcome to DiceOut!", Toast.LENGTH_SHORT).show();

        // Link instances to widgets in the activity view
        rollResult = (TextView) findViewById(R.id.rollResult);
        scoreText = (TextView) findViewById(R.id.scoreText);
        turnsText = (TextView) findViewById(R.id.turnsText);

        // Initialize the random number generator
        rand = new Random();

        // Create ArrayList container for the dice values
        dice = new ArrayList<Integer>();

        // Access the dice ImageView widgets
        ImageView die1image = (ImageView) findViewById(R.id.die1Image);
        ImageView die2image = (ImageView) findViewById(R.id.die2Image);
        ImageView die3image = (ImageView) findViewById(R.id.die3Image);
        ImageView die4image = (ImageView) findViewById(R.id.die4Image);
        ImageView die5image = (ImageView) findViewById(R.id.die5Image);
        // Build ArrayList with dice ImageView instances
        diceImageViews = new ArrayList<ImageView>();
        diceImageViews.add(die1image);
        diceImageViews.add(die2image);
        diceImageViews.add(die3image);
        diceImageViews.add(die4image);
        diceImageViews.add(die5image);


    }

    public void rollDice(View v) {

        if(turns == 0){

        }
        else {
            // Roll dice between [1,6]
            die1 = rand.nextInt(6) + 1;
            die2 = rand.nextInt(6) + 1;
            die3 = rand.nextInt(6) + 1;
            die4 = rand.nextInt(6) + 1;
            die5 = rand.nextInt(6) + 1;

            //decrement the turns count
            turns--;

            }

            // Set dice values into an ArrayList
            dice.clear();
            dice.add(die1);
            dice.add(die2);
            dice.add(die3);
            dice.add(die4);
            dice.add(die5);

            //set image drawables
            showDice();

            if (die1 == die2 && die1 == die3) {
                //triples
                int scoreDelta = die1 * 100;
                msg = "You rolled a triple " + die1 + "! You score " + scoreDelta + " points!";
                score += scoreDelta;
            } else if (die1 == die2 || die1 == die3 || die2 == die3) {
                msg = "You rolled doubles for 50 points!";
                score += 50;
            } else {
                msg = "You didn't score this roll. Try again!";
            }

            // Update the app to display the result message and number of turns left
            rollResult.setText(msg);
            if(turns == 0){
                //scoreText.setText("Final Score : " + score);
                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        showFinalScore();
                    }
                }, 500);


            }
            else {
                scoreText.setText("Score: " + score);
            }
            turnsText.setText("You have: " + turns + " turns left!");
        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_SCORE, score);
        savedInstanceState.putInt(STATE_ROLLS, turns);
        savedInstanceState.putString(STATE_MSG, msg);
        savedInstanceState.putIntegerArrayList(STATE_LIST, dice);

        //Always call superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);

    }
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        //restore state members from saved instance
        score = savedInstanceState.getInt(STATE_SCORE);
        turns = savedInstanceState.getInt(STATE_ROLLS);
        msg = savedInstanceState.getString(STATE_MSG);
        dice = savedInstanceState.getIntegerArrayList(STATE_LIST);

        rollResult.setText(msg);
        scoreText.setText("Score: " + score);
        turnsText.setText("You have: " + turns + " turns left!");

        showDice();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showFinalScore(){
        Intent intent = new Intent(this,FinalScoreActivity.class);
        intent.putExtra(EXTRA_FINAL_SCORE,scoreText.getText());

        startActivity(intent);

    }

    private void showDice() {
        for (int dieOfSet = 0; dieOfSet < 3; dieOfSet++) {
            String imageName = "die_" + dice.get(dieOfSet) + ".png";

            try {
                InputStream stream = getAssets().open(imageName);
                Drawable d = Drawable.createFromStream(stream, null);
                diceImageViews.get(dieOfSet).setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
