package com.math.crazymath;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;




/**
 * Created by Tom on 04/03/2017.
 */

public class ActivityGame extends Activity implements View.OnClickListener {

    //variables
    private TextView questionTextField, scoreTextField, timerText, answerTextField;
    private int level, userAnswer, score, questionCount, leftTime, clickCounter ;
    private cDtimer timer = new cDtimer(11000, 1000);
    private ImageView responseImage;
    private Boolean hintsChecked;
    private ProgressBar timeLeft;
    private boolean clickFlag;
    static TextView hintsTxt;
    private Answer answer;
    boolean isContinueClicked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        isContinueClicked = getIntent().getExtras().getBoolean("clicked");
        //passing the switch button for enable and disable hints
        hintsChecked = getSharedPreferences("com.math.crazymath", Context.MODE_PRIVATE).getBoolean("state",false);
        //text and image views
        timeLeft = (ProgressBar) findViewById(R.id.progressBarTimer);
        responseImage = (ImageView) findViewById(R.id.response);
        questionTextField = (TextView) findViewById(R.id.questionEvaluation);
        answerTextField = (TextView) findViewById(R.id.answer);
        answerTextField.setText("?");
        hintsTxt = (TextView) findViewById(R.id.hintBoxText);
        scoreTextField = (TextView) findViewById(R.id.score);
        timerText = (TextView) findViewById(R.id.timerBox);
        responseImage.setVisibility(View.INVISIBLE);


        //create buttons
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        Button btn6 = (Button) findViewById(R.id.btn6);
        Button btn7 = (Button) findViewById(R.id.btn7);
        Button btn8 = (Button) findViewById(R.id.btn8);
        Button btn9 = (Button) findViewById(R.id.btn9);
        Button btn0 = (Button) findViewById(R.id.btn0);
        Button btnEnter = (Button) findViewById(R.id.enter);
        Button btnClear = (Button) findViewById(R.id.clear);
        Button btnMinus = (Button) findViewById(R.id.minus);

        //buttons click listener
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        this.level = getIntent().getIntExtra("level", Level.NOVICE);
        int current = getIntent().getIntExtra("current", -1);
        int score = getIntent().getIntExtra("score", -1);
        timer.cancel();

        //if current question is -1 then start the quiz otherwise resume the quiz
        if (current == -1) {
            startQuiz();
        } else {
            resumeQuiz(this.level, current, score);
        }
        //goes to te update score method
        this.updateScoreText();
    }


    //resume quiz method
    private void resumeQuiz(int level, int current, int score) {
        Quiz.getInstance().resume(level, current, score);
        QuestionEvaluation questionEvaluation = Quiz.getInstance().getNextQuestion();
        showQuestion(questionEvaluation);
        timer.start();
    }

    //start quiz method, starts the quiz after asking the user to input the username
    private void startQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Start Quiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userName = input.getText().toString();
                if (userName != null && userName.trim().length() > 0) {
                    Quiz.getInstance().setUser(userName);
                }
                //start timer after username is entered
                timer.start();
            }

        });
        builder.show();
        Quiz.getInstance().start(this.level);
        QuestionEvaluation questionEvaluation = Quiz.getInstance().getNextQuestion();
        showQuestion(questionEvaluation);
    }

    //buttons onClick action
    @Override
    public void onClick(View view) {
        //# button action listener
        if (view.getId() == R.id.enter) {
            //increase the click counter variable each time # is clicked
            clickCounter++;
            /*if statement to check if the hints are on or off if hints are on it calls the hintsAreOn method
              if hints are off it calls the hintsAreOff method*/
            if (hintsChecked == true){
                hintsAreOn();
            }else if (hintsChecked==false) {
                hintsAreOff();
            }
            //- button action listener goes to the class answer if the answer is negative and there the negative answer is set
        } else if (view.getId() == R.id.minus) {
            this.answer.negate();
            //Del button action listener, calls the answer class and from there the default value of answer text field is set
        } else if (view.getId() == R.id.clear) {

            this.answer.delete();

        } else {
            //get the tag from button and append from answer class
            String userInput = view.getTag().toString();
            this.answer.append(userInput);
        }

        //answer to string
        answerTextField.setText(this.answer.toString());
    }


    //hints are off method this method is called when user switches the hints off
    public void hintsAreOff(){

        if (clickFlag == false){
            userAnswer = answer.value();
            //if correct answer is given
            if (Quiz.getInstance().validate(userAnswer)) {
                leftTime=Integer.parseInt(timerText.getText().toString());
                responseImage.setImageResource(R.drawable.correct);
                calculateScore();
                //else if incorrect answer is given
            } else {
                responseImage.setImageResource(R.drawable.wrong);
            }
            //set the response of correct or incorrect to be visible
            responseImage.setVisibility(View.VISIBLE);
            timer.cancel();
            clickFlag = true;
            //move to the next question if # is clicked 2nd time
        }else if (clickFlag== true){
            timer.start();
            moveToNextQuestion();
            clickFlag = false;
        }
    }
    //hints are on method this method is called when user switches the hints on
    public void hintsAreOn() {
        //counting the # button clicks
        if (clickCounter<5) {
            userAnswer = answer.value();
            //if correct answer is given
            if (Quiz.getInstance().hintsValidate(userAnswer)) {
                leftTime = Integer.parseInt(timerText.getText().toString());
                responseImage.setImageResource(R.drawable.correct);
                clickCounter=4;
                timer.cancel();
                calculateScore();

            } else {
                responseImage.setImageResource(R.drawable.wrong);
            }
            responseImage.setVisibility(View.VISIBLE);

            if (clickCounter==4){
                timer.cancel();
            }

        //# button clicked more than 4 times
        } else if (clickCounter>4) {
            clickCounter=0;
            timer.start();
            moveToNextQuestion();
        }
    }


    //method to show question this method is called at the start and resume of the quiz
    private void showQuestion(QuestionEvaluation questionEvaluation) {
        this.responseImage.setVisibility(View.INVISIBLE);
        this.answer = new Answer();
        this.questionTextField.setText(questionEvaluation.toString());
    }


    //move to the next question
    private void moveToNextQuestion() {
        //if game is completed go to the end quiz method
        if (Quiz.getInstance().isComplete()) {
            endQuiz();
        } else {
            //set the default value to the answer txt
            answer.reset();
            answerTextField.setText(answer.toString());
            //show next question
            QuestionEvaluation questionEvaluation = Quiz.getInstance().getNextQuestion();
            showQuestion(questionEvaluation);
            //update the score
            updateScoreText();

            timer.start();
            hintsTxt.setText("");
            clickCounter=0;
        }
    }

   /* this is the end quiz method this method is called at the end of next quiz and user is presented with number of questions he/she answered correctly,
    pressing the OK on the dialog moves to the high scores */
    private void endQuiz() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityGame.this);
        dialog.setCancelable(false);
        dialog.setTitle("Results");
        String resultMessage = String.format("You answered %d questions out of %d correctly.",
                questionCount , Quiz.getInstance().size() -1);
        timer.cancel();
        dialog.setMessage(resultMessage);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences preferences = getSharedPreferences("currentGame", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                dialog.cancel();
                saveResults();
                finish();
                showHighScores();

            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }


    //saving the results
    private void saveResults() {
        SharedPreferences preferences = getSharedPreferences("scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Quiz.getInstance().getUser(), getScore());
        editor.commit();

    }

    //show high score screen at the end of quiz when OK is pressed on the dialog
    public void showHighScores() {
        Intent intent = new Intent(this, ActivityHighScore.class);
        this.startActivity(intent);
    }

    //calculating the user score based on time left on the counter
    public void calculateScore(){
        /*If the user answers a question with 10 secs remaining*/
        if (leftTime==10){
            score += 100;
            //if answer given in less than 10 seconds remaining
        }else{
            double calculateScore = 100/(10-leftTime);
            int value = (int) Math.rint(calculateScore);
            score+=value;
            questionCount++;
        }

    }

    //sets the text of hints txt box
    public String setText(String str){
        hintsTxt.setText(str);
        return str;
    }



    //updates the score
    private void updateScoreText() {
        scoreTextField.setText(String.format("Score: %d/%d", getScore(), (Quiz.getInstance().getCurrent() + 1)));
    }

    //get score
    public int getScore() {
        return score ;
    }



    //inner class fo count down timer
    public class cDtimer extends CountDownTimer {

        public cDtimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //when timer finishes move to the next question
        public void onFinish() {

            moveToNextQuestion();
            timeLeft.setProgress(0);
        }

        public void onTick(long millisUntilFinished) {
            timerText.setText("" + millisUntilFinished / 1000);
            int progress = (int) (millisUntilFinished/1000);
            timeLeft.setProgress(progress);

        }
    }


    /*when back button pressed cancel the timer and finish the activity
    this prevents from timer running in background */
    public void onBackPressed(){
     timer.cancel();
        finish();
    }

}