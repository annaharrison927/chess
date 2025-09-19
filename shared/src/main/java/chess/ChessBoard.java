package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    final private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() -1] = piece;
        // Java starts with 0, but our board starts with 1
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Add pawns
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPawnsToBoard(2, whitePawn);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPawnsToBoard(7, blackPawn);

        // Add Rooks
        addOtherPieces(1, 1, 8, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addOtherPieces(8, 1, 8, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        // Add Knights
        addOtherPieces(1, 2, 7, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addOtherPieces(8, 2, 7, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

        // Add Bishops
        addOtherPieces(1, 3, 6, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addOtherPieces(8, 3, 6, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

        // Add Kings
        addOtherPieces(1, 5, 0, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addOtherPieces(8, 5, 0, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        // Add Queens
        addOtherPieces(1, 4, 0, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addOtherPieces(8, 4, 0, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
    }

    private void addPawnsToBoard (int rowNum, ChessPiece piece){
        for (int i = 1; i <= 8; i++){
            ChessPosition currentPosition = new ChessPosition(rowNum, i);
            addPiece(currentPosition, piece);
        }
    }

    private void addOtherPieces(int startRow, int pos1, int pos2, ChessGame.TeamColor color, ChessPiece.PieceType type){
        ChessPiece piece1 = new ChessPiece(color, type);
        ChessPosition position1 = new ChessPosition(startRow, pos1);
        addPiece(position1, piece1);

        if (pos2 != 0){
            ChessPiece piece2 = new ChessPiece(color, type);
            ChessPosition position2 = new ChessPosition(startRow, pos2);
            addPiece(position2, piece2);
        }
    }


    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
