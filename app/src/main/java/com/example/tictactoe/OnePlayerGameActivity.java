package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OnePlayerGameActivity extends AppCompatActivity implements View.OnClickListener {

    static Button[][] tiles = new Button[3][3];
    private TextView turnLabel;
    boolean isGameEasy;
    private RelativeLayout difficultyChoicesLayout, gameTilesLayout;



    boolean isPlayerOneTurn = true;
    final private String BOT_SYMBOL = "O";
    boolean gameEnded = false;

    private SoundPool soundPool;
    private int winSound;
    private int tieSound;
    private int clickSound;
    private int warningSound;
    private int lossSound;
    
    GameLogic gameLogic;
    MyBotAlgorithm myBotAlgorithm;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setUpChoices();
        setUpViews();

        myBotAlgorithm = new MyBotAlgorithm(tiles);
    }

    private void setUpChoices() {
        Button easy = findViewById(R.id.easyDifficulty);
        Button hard = findViewById(R.id.hardDifficulty);

        difficultyChoicesLayout = findViewById(R.id.difficultyChoices);
        gameTilesLayout = findViewById(R.id.gameTilesLayout);

        easy.setOnClickListener(view -> proceedToGame(true));
        hard.setOnClickListener(view -> proceedToGame(false));
    }

    private void proceedToGame(boolean isGameEasy) {
        this.isGameEasy = isGameEasy;
        difficultyChoicesLayout.setVisibility(View.GONE);
        gameTilesLayout.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {
        tiles[0][0] = findViewById(R.id.btn1);
        tiles[0][1] = findViewById(R.id.btn2);
        tiles[0][2] = findViewById(R.id.btn3);
        tiles[1][0] = findViewById(R.id.btn4);
        tiles[1][1] = findViewById(R.id.btn5);
        tiles[1][2] = findViewById(R.id.btn6);
        tiles[2][0] = findViewById(R.id.btn7);
        tiles[2][1] = findViewById(R.id.btn8);
        tiles[2][2] = findViewById(R.id.btn9);

        gameLogic = new GameLogic(tiles);

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                .build();

        //initialize sound pool
        winSound = soundPool.load(this, R.raw.marimba_win, 1);
        tieSound = soundPool.load(this, R.raw.tie_sound, 1);
        clickSound = soundPool.load(this, R.raw.click, 1);
        warningSound = soundPool.load(this, R.raw.warning, 1);
        lossSound = soundPool.load(this, R.raw.loss, 1);

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tiles[i][j].setOnClickListener(this);
            }
        }

        turnLabel = findViewById(R.id.turnsLabel);
    }

    @SuppressLint("ResourceAsColor")
    public void resetTiles() {
        turnLabel.setText("X's turn");
        turnLabel.setTextColor(getResources().getColor(R.color.blue));
        gameLogic.populateTiles();
        setTilesColor(R.color.white, R.color.dark);
    }

    @SuppressLint("ResourceAsColor")
    public void haveWinnerEffects(String winnerSymbol) {
        String label = winnerSymbol + " win the Game!!!";
        turnLabel.setText(label);
        if (winnerSymbol.equals("O")) {
            turnLabel.setTextColor(getResources().getColor(R.color.redOrange));
            setTilesColor(R.color.white, R.color.loser);
        } else {
            turnLabel.setTextColor(getResources().getColor(R.color.yellow));
            setTilesColor(R.color.white, R.color.yellow);
        }
    }

    private void setTilesColor(int foreground, int background) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tiles[i][j].setTextColor(getResources().getColor(foreground));
                tiles[i][j].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(background)));
            }
        }
    }

    private int getCurrentTileTextColor() {
        final int color1 = getResources().getColor(R.color.blue);
        final int color2 = getResources().getColor(R.color.redOrange);
        return (isPlayerOneTurn) ? color1 : color2;
    }

    private void displayRetryMessage(String playerSymbol) {
        gameLogic.setTilesState(gameEnded);
        if (playerSymbol.equals("Tie")) {
            endedATieGameEffects();
        } else {
            haveWinnerEffects(playerSymbol);
        }
        Snackbar snackbar = Snackbar.make(findViewById(R.id.gameActivity), "Retry the game?", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetTiles();
                        isPlayerOneTurn = true;
                        gameEnded = false;
                        gameLogic.setTilesState(false);
                        turnLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                    }
                });
        snackbar.show();
    }

    private void endedATieGameEffects() {
        turnLabel.setText("Game Ended in a Tie!");
        turnLabel.setTextColor(getResources().getColor(R.color.grey));
        setTilesColor(R.color.grey, R.color.white);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public void onClick(View view) {
        //ensures clickSound is loaded successfully
        if (clickSound != 0)
            soundPool.play(clickSound, 1, 1, 0, 0, 1);
        int buttonClickedId = view.getId();
        boolean buttonFound = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j].getId() == buttonClickedId) {

                    if (gameLogic.checkIfAvailable(tiles[i][j])) {
                        soundPool.play(warningSound, 1, 1, 0, 0, 1);
                        return;
                    }

                    tiles[i][j].setText(gameLogic.getCurrentSymbol(isPlayerOneTurn));
                    tiles[i][j].setTextColor(getCurrentTileTextColor());
                    buttonFound = true;
                    break;
                }
            }

            if (buttonFound) break;
        }

        if (!checkForWinner()) {
            updateGameState();
            if (!gameEnded) {
                gameLogic.setTilesState(true);
                botTurns();
            }
        }
    }

    private boolean checkForWinner() {

        if (gameLogic.isWinner(gameLogic.getCurrentSymbol(isPlayerOneTurn))) {
            soundPool.play(winSound, 1, 1, 0, 0, 1);
            gameEnded = true;
            displayRetryMessage(gameLogic.getCurrentSymbol(isPlayerOneTurn));
            return true;
        }
        else if (gameLogic.isTie()) {
            soundPool.play(tieSound, 1, 1, 0, 0, 1);
            gameEnded = true;
            displayRetryMessage("Tie");
            return true;
        }
        return false;
    }

    private void updateGameState() {
        //Proceed to next player turn
        isPlayerOneTurn = !isPlayerOneTurn;

        String label = gameLogic.getCurrentSymbol(isPlayerOneTurn) + "'s turn";
        turnLabel.setText(label);
        turnLabel.setTextColor(getCurrentTileTextColor());
    }

    private void botTurns() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            int[] botMove = bestMove();
            tiles[botMove[0]][botMove[1]].setText(BOT_SYMBOL);
            tiles[botMove[0]][botMove[1]].setTextColor(getCurrentTileTextColor());

            if (!gameLogic.isWinner(BOT_SYMBOL)) {
                updateGameState();
            } else {
                soundPool.play(lossSound, 1, 1, 0, 0, 1);
                gameEnded = true;
                displayRetryMessage(BOT_SYMBOL);
            }
        }, 100);
        gameLogic.setTilesState(false);
    }

    private int[] minimax(boolean isMaximizing) {
        String PLAYER_SYMBOL = "X";
        int[] bestCoordinate = {-1, -1};

        if (gameLogic.isWinner(BOT_SYMBOL))
            return new int[]{10, -1, -1};
        else if (gameLogic.isWinner(PLAYER_SYMBOL))
            return new int[]{-10, -1, -1};
        else if (gameLogic.isTie())
            return new int[]{0, -1, -1};

        int bestScore = (isMaximizing) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (tiles[i][j].getText().toString().isEmpty()) {
                    tiles[i][j].setText((isMaximizing) ? BOT_SYMBOL : PLAYER_SYMBOL);

                    int moveVal = minimax(!isMaximizing)[0];

                    tiles[i][j].setText("");

                    if ((isMaximizing && moveVal > bestScore) || !isMaximizing && moveVal < bestScore) {
                        bestScore = moveVal;
                        bestCoordinate[0] = i;
                        bestCoordinate[1] = j;
                    }

                }
            }
        }

        return new int[]{bestScore, bestCoordinate[0], bestCoordinate[1]};
    }

    private int[] bestMove() {
        if (!isGameEasy) {
            int[] bestMoveCoordinate = minimax(true);

            if (bestMoveCoordinate[1] == -1 || bestMoveCoordinate[2] == -1) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (tiles[i][j].getText().toString().isEmpty())
                            return new int[]{i, j};
                    }
                }
            }

            return new int[]{bestMoveCoordinate[1], bestMoveCoordinate[2]};
        } else {
            int[] bestMoveCoordinate = myBotAlgorithm.myBotAlgo(tiles);

            if (bestMoveCoordinate[0] == -1 || bestMoveCoordinate[1] == -1) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (tiles[i][j].getText().toString().isEmpty())
                            return new int[]{i, j};
                    }
                }
            }

            return bestMoveCoordinate;
        }
    }
}