package com.example.tictactoe;
import android.widget.Button;

public class GameLogic {

    Button[][] tiles;

    public GameLogic(Button[][] tiles) {
        this.tiles = tiles;
    }

    public boolean checkIfAvailable(Button choiceTile) {
        return !choiceTile.getText().toString().isEmpty();
    }

    public boolean isWinner(String playerSymbol) {

        //Horizontal & vertical winning combination check
        for (int i = 0; i < 3; i++){
            if ((tiles[i][0].getText().toString().equals(playerSymbol) && tiles[i][1].getText().toString().equals(playerSymbol) && tiles[i][2].getText().toString().equals(playerSymbol))
                    || (tiles[0][i].getText().toString().equals(playerSymbol) && tiles[1][i].getText().toString().equals(playerSymbol) && tiles[2][i].getText().toString().equals(playerSymbol)))
                return true;
        }

        //Diagonal winning combination check
        return ((tiles[0][0].getText().toString().equals(playerSymbol) && tiles[1][1].getText().toString().equals(playerSymbol) && tiles[2][2].getText().toString().equals(playerSymbol))
                || (tiles[0][2].getText().toString().equals(playerSymbol) && tiles[1][1].getText().toString().equals(playerSymbol) && tiles[2][0].getText().toString().equals(playerSymbol)));
    }

    public boolean isTie() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (tiles[i][j].getText().toString().isEmpty())
                    return false;
            }
        }
        return true;
    }

    public void populateTiles() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tiles[i][j].setText("");
            }
        }
    }

    public void setTilesState(boolean gameEnded) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tiles[i][j].setClickable(!gameEnded);
            }
        }
    }

    public String getCurrentSymbol(boolean isPlayerOneTurn) {
        final String player1Symbol = "X";
        final String player2Symbol = "O";
        return (isPlayerOneTurn) ? player1Symbol: player2Symbol;
    }

}
