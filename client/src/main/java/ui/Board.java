package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

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

    private static final HashMap<Integer, Integer> BLACK_WHITE_FLIP_MAP = new HashMap<>();

    static {
        BLACK_WHITE_FLIP_MAP.put(0, 7);
        BLACK_WHITE_FLIP_MAP.put(1, 6);
        BLACK_WHITE_FLIP_MAP.put(2, 5);
        BLACK_WHITE_FLIP_MAP.put(3, 4);
        BLACK_WHITE_FLIP_MAP.put(4, 3);
        BLACK_WHITE_FLIP_MAP.put(5, 2);
        BLACK_WHITE_FLIP_MAP.put(6, 1);
        BLACK_WHITE_FLIP_MAP.put(7, 0);
    }

    public void createBoard(String color, ChessGame chessGame) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        ChessBoard chessBoard = chessGame.getBoard();
        if (chessBoard.boardEmpty()) {

        }

        setBlack(out);
        drawHeaders(out, color);
        drawBoard(out, color, chessBoard);
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

    private static void drawBoard(PrintStream out, String color, ChessBoard chessBoard) {
        String[] numLabels = {"8", "7", "6", "5", "4", "3", "2", "1"};
        if (Objects.equals(color, "BLACK")) {
            numLabels = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        }

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            String rowNum = numLabels[boardRow];
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                String chr = pickPiece2(boardRow, boardCol, color, chessBoard);
                printChr(color, out, chr, boardRow, boardCol, rowNum);
            }
            out.println();
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printChr(String color, PrintStream out, String chr, int boardRow, int boardCol, String rowNum) {
        pickSquareColor(boardRow, boardCol, out);
        pickPieceColor(boardRow, color, out);
        out.print(chr);
        setBlack(out);

        if (boardCol == 7) {
            out.print(SET_TEXT_COLOR_GREEN);
            out.print(EMPTY);
            out.print(rowNum);
        }
    }

/*
    private static String pickPiece(int boardRow, int boardCol, String color) {
        if (boardRow == 1 || boardRow == 6) {
            return P;
        } else if (boardRow == 0 || boardRow == 7) {
            if (boardCol == 0 || boardCol == 7) {
                return R;
            } else if (boardCol == 1 || boardCol == 6) {
                return N;
            } else if (boardCol == 2 || boardCol == 5) {
                return B;
            } else if (boardCol == 3) {
                if (Objects.equals(color, "WHITE")) {
                    return Q;
                } else {
                    return K;
                }
            } else {
                if (Objects.equals(color, "WHITE")) {
                    return K;
                } else {
                    return Q;
                }
            }
        } else {
            return "   ";
        }
    }
*/

    private static String pickPiece2(int boardRow, int boardCol, String color, ChessBoard chessBoard) {
        if (Objects.equals(color, "BLACK")) {
            boardRow = BLACK_WHITE_FLIP_MAP.get(boardRow);
            boardCol = BLACK_WHITE_FLIP_MAP.get(boardCol);
        }
        ChessPiece.PieceType pieceType = chessBoard.getPiece(new ChessPosition(boardCol, boardRow)).getPieceType();
        switch (pieceType) {
            case null -> {
                return "   ";
            }
            case KING -> {
                return K;
            }
            case QUEEN -> {
                return Q;
            }
            case BISHOP -> {
                return B;
            }
            case KNIGHT -> {
                return N;
            }
            case ROOK -> {
                return R;
            }
            case PAWN -> {
                return P;
            }
        }
    }

    private static void pickSquareColor(int boardRow, int boardCol, PrintStream out) {
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
    }

    private static void pickPieceColor(int boardRow, String color, PrintStream out) {
        if (Objects.equals(color, "WHITE") && (boardRow == 6 || boardRow == 7)) {
            out.print(SET_TEXT_COLOR_WHITE);
        } else if (Objects.equals(color, "BLACK") && (boardRow == 0 || boardRow == 1)) {
            out.print(SET_TEXT_COLOR_WHITE);
        } else {
            out.print(SET_TEXT_COLOR_BLACK);
        }
    }

}