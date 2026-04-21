package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class TwoPlayerGameActivity extends AppCompatActivity implements View.OnClickListener {

    static Button[][] tiles = new Button[3][3];
    boolean isPlayerOneTurn = true;
    private TextView turnLabel;
    boolean gameEnded = false;
    private SoundPool soundPool;
    private int winSound;
    private int tieSound;
    private int clickSound;
    private int warningSound;
    GameLogic gameLogic;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setUpViews();

    }

    private void setUpViews() {

        RelativeLayout difficultyChoicesLayout = findViewById(R.id.difficultyChoices);
        RelativeLayout gameTilesLayout = findViewById(R.id.gameTilesLayout);

        difficultyChoicesLayout.setVisibility(View.GONE);
        gameTilesLayout.setVisibility(View.VISIBLE);

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
        turnLabel.setTextColor(getResources().getColor(R.color.yellow));
        setTilesColor(R.color.white, R.color.yellow);
    }

    private void setTilesColor(int foreground, int background) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tiles[i][j].setTextColor(getResources().getColor(foreground));
                tiles[i][j].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(background)));
            }
        }
    }

    @Override
    public void onClick(View view) {
        soundPool.play(clickSound, 1, 1, 0, 0, 1);
        int buttonClickedId = view.getId();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j].getId() == buttonClickedId) {

                    if (gameLogic.checkIfAvailable(tiles[i][j])) {
                        soundPool.play(warningSound, 1, 1, 0, 0, 1);
                        return;
                    }

                    tiles[i][j].setText(gameLogic.getCurrentSymbol(isPlayerOneTurn));
                    tiles[i][j].setTextColor(getCurrentTileTextColor());
                }
            }
        }

        if (gameLogic.isWinner(gameLogic.getCurrentSymbol(isPlayerOneTurn))) {
            soundPool.play(winSound, 1, 1, 0, 0, 1);
            gameEnded = true;
            displayRetryMessage(gameLogic.getCurrentSymbol(isPlayerOneTurn));
            return;
        }
        else if (gameLogic.isTie()) {
            soundPool.play(tieSound, 1, 1, 0, 0, 1);
            gameEnded = true;
            displayRetryMessage("Tie");
            return;
        }

        //Proceed to next player turn
        isPlayerOneTurn = !isPlayerOneTurn;

        String label = gameLogic.getCurrentSymbol(isPlayerOneTurn) + "'s turn";
        turnLabel.setText(label);
        turnLabel.setTextColor(getCurrentTileTextColor());
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

}
