package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Board {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = " ";
    // Padded characters.

    private static final String K = " K ";
    private static final String Q = " Q ";

    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        String[] rowNames = {"8\n", "7\n", "6\n", "5\n", "4\n", "3\n", "2\n", "1\n"};

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawBoard(out);

    }

    private static void drawHeaders(PrintStream out) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            String chr = headers[boardCol];
            out.print(EMPTY);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(chr);
            out.print(EMPTY);
        }
        out.println();
    }

    private static void drawBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                printPlayer(out, rand.nextBoolean() ? K : Q, boardCol);
            }
            out.println();
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player, int i) {
        if (i % 2 == 0) {
            out.print(SET_BG_COLOR_RED);
        } else {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }

        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setBlack(out);

        if (i == 7) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(EMPTY);
            out.print("1");
        }
    }
}