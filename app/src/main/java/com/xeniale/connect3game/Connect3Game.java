package com.xeniale.connect3game;

import android.util.Log;

public class Connect3Game {
    private final Byte[][] matrix = new Byte[3][3];
    private static final RowCol[][] WIN_POSITIONS = {
            {RowCol.of(0, 0), RowCol.of(0, 1), RowCol.of(0, 2)},
            {RowCol.of(1, 0), RowCol.of(1, 1), RowCol.of(1, 2)},
            {RowCol.of(2, 0), RowCol.of(2, 1), RowCol.of(2, 2)},

            {RowCol.of(0, 0), RowCol.of(1, 0), RowCol.of(2, 0)},
            {RowCol.of(0, 1), RowCol.of(1, 1), RowCol.of(2, 1)},
            {RowCol.of(0, 2), RowCol.of(1, 2), RowCol.of(2, 2)},

            {RowCol.of(0, 0), RowCol.of(1, 1), RowCol.of(2, 2)},
            {RowCol.of(2, 0), RowCol.of(1, 1), RowCol.of(0, 2)}
    };


    static class RowCol {
        public final short row;
        public final short column;

        RowCol(short row, short column) {
            this.row = row;
            this.column = column;
        }

        public static RowCol of(short row, short column) {
            return new RowCol(row, column);
        }

        public static RowCol of(int row, int column) {
            return new RowCol((short) row, (short) column);
        }
    }

    enum Player {
        X(((byte) 0)),
        O(((byte) 1));
        public final byte number;

        Player(byte number) {
            this.number = number;
        }
    }

    enum MoveResult {
        Win,
        Draw,
        Proceed
    }

    public MoveResult nextMove(Player player, RowCol rowColumn) {
        if (matrix[rowColumn.row][rowColumn.column] != null) {
            Log.d("Count3GameClick", "Already selected this row " + rowColumn.row + " column: " + rowColumn.column);
            return MoveResult.Proceed;
        }

        matrix[rowColumn.row][rowColumn.column] = player.number;

        for (RowCol[] winPosition : WIN_POSITIONS) {
            boolean win = true;
            for (int i = 1; i < winPosition.length; i++) {
                RowCol prevWinPosition = winPosition[i - 1];
                RowCol currentWinPosition = winPosition[i];
                if (matrix[currentWinPosition.row][currentWinPosition.column] == null
                        || !matrix[currentWinPosition.row][currentWinPosition.column]
                        .equals(matrix[prevWinPosition.row][prevWinPosition.column])
                ) {
                    win = false;
                    break;
                }
            }

            if (win) {
                return MoveResult.Win;
            }
        }

        //check draw
        boolean draw = true;
        for (Byte[] positions : matrix) {
            for (Byte position : positions) {
                if (position == null) {
                    draw = false;
                }
            }
        }

        if (draw) {
            return MoveResult.Draw;
        }

        return MoveResult.Proceed;
    }

}
