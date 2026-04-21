package com.example.tictactoe;

import android.widget.Button;

public class MyBotAlgorithm {

    GameLogic gameLogic;

    public MyBotAlgorithm(Button[][] tiles) {
        this.gameLogic = new GameLogic(tiles);
    }

    public int[] myBotAlgo(Button[][] tiles) {

        final String PLAYER_SYMBOL = "X";
        final String BOT_SYMBOL = "O";

        int[] botWinningCombination = checkForBotWinningPosition(tiles, BOT_SYMBOL);
        int[] botCounterCombination = checkForBotWinningPosition(tiles, PLAYER_SYMBOL);

        if (botWinningCombination[0] != -1)
            return botWinningCombination;
        else if (botCounterCombination[0] != -1) {
            return botCounterCombination;
        }

        if (tiles[1][1].getText().toString().isEmpty())
            return new int[]{1, 1};
        else if (tiles[1][1].getText().toString().equals(PLAYER_SYMBOL)) {
            for (int i = 0; i < 3; i+=2) {
                for (int j = 0; j < 3; j+=2) {
                    if (tiles[i][j].getText().toString().isEmpty())
                        return new int[]{i, j};
                }
            }
        } else {

            for (int i = 0; i < 3; i+=2) {
                for (int j = 0; j <= 2; j+=2) {
                    if (tiles[i][j].getText().toString().isEmpty())
                        return new int[]{i, j};
                }
            }

            if (tiles[0][1].getText().toString().isEmpty())
                return new int[]{0, 1};
            else if (tiles[1][0].getText().toString().isEmpty())
                return new int[]{1, 0};
            else if (tiles[1][2].getText().toString().isEmpty())
                return new int[]{1, 2};
            else if (tiles[2][1].getText().toString().isEmpty())
                return new int[]{2, 1};
        }

        return new int[]{-1, -1};
    }

    public int[] checkForBotWinningPosition(Button[][] tiles, String playerSymbol) {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (tiles[i][j].getText().toString().isEmpty()) {
                    tiles[i][j].setText(playerSymbol);
                    if (gameLogic.isWinner(playerSymbol)) {
                        tiles[i][j].setText("");
                        return new int[]{i, j};
                    }
                    tiles[i][j].setText("");
                }
            }
        }
        return new int[]{-1, -1};
    }

}
