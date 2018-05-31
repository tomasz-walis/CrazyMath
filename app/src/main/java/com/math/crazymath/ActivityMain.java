package com.math.crazymath;

import android.app.*;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.view.*;
import android.widget.*;




public class ActivityMain extends AppCompatActivity {


    Switch hintSwitch;
    private Dialog levelDialog;
    ActivityGame game = new ActivityGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hintSwitch =  (Switch) findViewById(R.id.hintsCheckBox);

    }
    // start game with novice level
    public void startNoviceGame(View view) {
        startGame(Level.NOVICE);
    }
    // start game with easy level
    public void startEasyGame(View view) {
        startGame(Level.EASY);
    }
    // start game with medium level
    public void startMediumGame(View view) {
        startGame(Level.MEDIUM);
    }
    // start game with guru level
    public void startGuruGame(View view) {
        startGame(Level.GURU);
    }

    //start game method, starts new game
    private void startGame(int chosenLevel) {
        levelDialog.cancel();
        Intent playIntent = new Intent(this, ActivityGame.class);
        playIntent.putExtra("level", chosenLevel);
        this.startActivity(playIntent);
    }

    //this method is executed when play button is clicked, shows the dialog where user chooses the difficulty level
    public void newGame(View view) {
        levelDialog = new Dialog(this);
        levelDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        levelDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_level, null));
        levelDialog.show();
    }



    //passing the state of the hints button
    public void passSwitch(View view){
        SharedPreferences.Editor editor = getSharedPreferences(
                "com.math.crazymath", Context.MODE_PRIVATE).edit();
        editor.putBoolean("state", hintSwitch.isChecked());
        editor.apply();
    }

    // show help method, this method shows the pop up when about button is clicked
    public void showHelp(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityMain.this);
        dialog.setCancelable(false);
        dialog.setTitle("CrazyMath!");
        dialog.setMessage(R.string.help_info);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    //shows high score when high score button clicked
    public void showHighScores(View view) {
        Intent intent = new Intent(this, ActivityHighScore.class);
        this.startActivity(intent);
    }

    /*this method is executed when the exit button is clicked. The dialog is presented to the user to ask if he/she wants to exit the game
     when the user exits the game the state of the game is saved*/
    public void exitApp(View view) {
        if(Quiz.getInstance().isInProgress()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityMain.this);
            dialog.setCancelable(false);
            dialog.setTitle("Exit CrazyMath");
            dialog.setMessage("You have a game in progress. Are you sure you want to exit the game?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    saveCurrentGame();
                    finish();
                    System.exit(1);
                }
            })
                    .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog alert = dialog.create();
            alert.show();
        }
        else {
            finish();
            System.exit(1);
        }

    }

    // method is executed when continue button is pressed
    public void continueGame(View view) {
        SharedPreferences preferences = getSharedPreferences("currentGame", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "");
        int current = preferences.getInt("current", -1);
        int score = preferences.getInt("score", -1);
        int level = preferences.getInt("level", -1);

        if(current == -1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityMain.this);
            dialog.setCancelable(false);
            dialog.setTitle("Cannot resume game");
            dialog.setMessage("You do not have a game saved!");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();
        }
        else {
            Intent playIntent = new Intent(this, ActivityGame.class);
            playIntent.putExtra("level", level);
            playIntent.putExtra("current", current);
            playIntent.putExtra("score", score);
            this.startActivity(playIntent);
        }
    }

    //this method saves the game state in to shared preferences, method is called when user preses the exit button
    private void saveCurrentGame() {
        SharedPreferences preferences = getSharedPreferences("currentGame", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", Quiz.getInstance().getUser());
        editor.putInt("current", Quiz.getInstance().getCurrent());
        editor.putInt("score", game.getScore());
        editor.putInt("level", Quiz.getInstance().getLevel());
        editor.commit();
    }

}