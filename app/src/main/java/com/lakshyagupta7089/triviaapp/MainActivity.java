package com.lakshyagupta7089.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.lakshyagupta7089.triviaapp.controller.AppController;
import com.lakshyagupta7089.triviaapp.data.Repository;
import com.lakshyagupta7089.triviaapp.databinding.ActivityMainBinding;
import com.lakshyagupta7089.triviaapp.model.Question;
import com.lakshyagupta7089.triviaapp.model.Score;
import com.lakshyagupta7089.triviaapp.util.Prefs;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    private ArrayList<Question> questionList;
    private int scoreCounter = 0;
    private Score score;
    private static final String HIGHEST_SCORE = "highest_score";
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        AppController.context = this;
        score = new Score();
        prefs = new Prefs(this);

        new Repository(this).getQuestions(questionArrayList -> {
            questionList = questionArrayList;
            updateQuestion(questionList);
            updateCounter();
        });

        binding.buttonNext.setOnClickListener(view -> changeQuestionAndUpdateQuestionCounter());

        binding.buttonPrev.setOnClickListener(view -> {
            currentQuestionIndex = currentQuestionIndex == 0 ? questionList.size() - 1 :
                    currentQuestionIndex - 1;
            updateQuestion(questionList);
        });

        binding.buttonTrue.setOnClickListener(view -> {
            if (!questionList.isEmpty()) {
                checkAnswer(true);
                updateQuestion(questionList);
            }
        });

        binding.buttonFalse.setOnClickListener(view -> {
            if (!questionList.isEmpty()) {
                checkAnswer(false);
                updateQuestion(questionList);
            }
        });
        
        setHighestScore();
    }

    private void setHighestScore() {
        binding.highestScore.setText(String.format(getString(R.string.highest_score_text),
                prefs.getHighestScore()));
    }

    private void checkAnswer(boolean userChoseCorrect) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessage;

        if (answer == userChoseCorrect) {
            addCounter();
            snackMessage = R.string.correct_answer;
            fadeAnimation();
            slideUpAnimation();
        } else {
            deductPoints();
            snackMessage = R.string.wrong_answer;
            shakeAnimation();
            if (scoreCounter != 0)
                slideDownAnimation();
        }

        prefs.saveHighestScore(scoreCounter);
        setHighestScore();
        Snackbar.make(binding.cardView, snackMessage, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void changeQuestionAndUpdateQuestionCounter() {
        if (questionList != null && !questionList.isEmpty()) {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
            updateQuestion(questionList);
            updateCounter();
        }
    }

    private void updateCounter() {
        binding.textViewOutOf.setText
                (String.format(getString(R.string.question), currentQuestionIndex + 1, questionList.size()));
    }

    private void updateQuestion(ArrayList<Question> questionList) {
        updateCounter();
        String difficulty = questionList.get(currentQuestionIndex).getDifficulty().toUpperCase();
        changeColor(difficulty);

        binding.questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        binding.difficultyTextView.setText(difficulty);
        binding.categoryTextView.setText(questionList.get(currentQuestionIndex).getCategory());
    }

    private void changeColor(String difficulty) {
        if (difficulty.equals("easy".toUpperCase())) {
            binding.difficultyTextView.setTextColor(ContextCompat.getColor(
                    this,
                    R.color.easy
            ));
        } else if (difficulty.equals("medium".toUpperCase())) {
            binding.difficultyTextView.setTextColor(ContextCompat.getColor(
                    this,
                    R.color.medium
            ));
        } else if (difficulty.equals("hard".toUpperCase())){
            binding.difficultyTextView.setTextColor(ContextCompat.getColor(
                    this,
                    R.color.hard
            ));
        }
    }

    public void slideUpAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.side_up);
        binding.currentScore.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void slideDownAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.side_down);
        binding.currentScore.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);


        binding.cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);

                binding.buttonFalse.setClickable(false);
                binding.buttonTrue.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                changeQuestionAndUpdateQuestionCounter();

                binding.buttonFalse.setClickable(true);
                binding.buttonTrue.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                changeQuestionAndUpdateQuestionCounter();

                binding.buttonFalse.setClickable(true);
                binding.buttonTrue.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.saveHighestScore(scoreCounter);
    }

    private void deductPoints() {
        if (scoreCounter > 0) {
            scoreCounter -= 100;
        } else {
            scoreCounter = 0;
        }
        binding.currentScore.setTextColor(Color.RED);
        score.setScore(scoreCounter);
        binding.currentScore.setText(String.format(getString(R.string.cureent_score), scoreCounter));
    }

    private void addCounter() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        binding.currentScore.setText(String.format(getString(R.string.cureent_score), scoreCounter));
        binding.currentScore.setTextColor(Color.GREEN);
    }
}