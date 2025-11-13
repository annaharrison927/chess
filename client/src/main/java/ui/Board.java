package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Board {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = " ";

    private static final String K = " K ";
    private static final String Q = " Q ";
    private static final String R = " R ";
    private static final String N = " N ";
    private static final String B = " B ";
    private static final String P = " P ";

    private static Random rand = new Random();

//    public static void main(String[] args) {
//        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        String[] rowNames = {"8\n", "7\n", "6\n", "5\n", "4\n", "3\n", "2\n", "1\n"};
//        out.print(ERASE_SCREEN);
//
//        drawHeaders(out, color);
//        drawBoard(out);
//    }

    public void createBoard(String color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        setBlack(out);
        drawHeaders(out, color);
        drawBoard(out, color);
    }

    private static void drawHeaders(PrintStream out, String color) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if (Objects.equals(color, "BLACK")) {
            headers = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        }

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            String chr = headers[boardCol];
            out.print(EMPTY);
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(chr);
            out.print(EMPTY);
        }
        out.println();
    }

    private static void drawBoard(PrintStream out, String color) {
        String[] numLabels = {"8", "7", "6", "5", "4", "3", "2", "1"};
        if (Objects.equals(color, "BLACK")) {
            numLabels = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        }

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            String rowNum = numLabels[boardRow];
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                printPlayer(out, rand.nextBoolean() ? K : Q, boardRow, boardCol, rowNum);
            }
            out.println();
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player, int boardRow, int boardCol, String rowNum) {
        if (boardRow % 2 == 0) {
            if (boardCol % 2 == 0) {
                out.print(SET_BG_COLOR_RED);
            } else {
                out.print(SET_BG_COLOR_DARK_GREEN);
            }
        } else {
            if (boardCol % 2 == 0) {
                out.print(SET_BG_COLOR_DARK_GREEN);
            } else {
                out.print(SET_BG_COLOR_RED);
            }
        }

        out.print(SET_TEXT_COLOR_BLACK);
        out.print(player);
        setBlack(out);

        if (boardCol == 7) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(EMPTY);
            out.print(rowNum);
        }
    }
}