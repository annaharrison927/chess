package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        Collection<ChessMove> moveCollection;
        List<Integer> horizontalMoves = null;
        List<Integer> verticalMoves = null;
        boolean goToEndOfBoard = true;
        if (piece.getPieceType() == PieceType.BISHOP) {
            // Bishop can move any diagonal direction as long as the path isn't blocked
            horizontalMoves = List.of(-1, 1);
            verticalMoves = List.of(-1, 1);
        } else if (piece.getPieceType() == PieceType.KING) {
            // King can move in any diagonal, vertical, or horizontal position that's 1 away
            goToEndOfBoard = false;
            horizontalMoves = List.of(-1, 0, 1);
            verticalMoves = List.of(-1, 0, 1);
        }
        moveCollection = findMoves(myPosition, board, horizontalMoves, verticalMoves, goToEndOfBoard);
        return moveCollection;
    }

    // Helper and Override Methods

    private Collection<ChessMove> findMoves(ChessPosition myPosition, ChessBoard board, List<Integer> horizontalMoves, List<Integer> verticalMoves, boolean goToEndOfBoard){
        List<ChessMove> moveCollection = new ArrayList<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        // Starting from the current position, iterate through all possible next positions and add to list
        for (var hMove : horizontalMoves){
            for (var vMove : verticalMoves){
                int tempRowInt = startRow + hMove;
                int tempColInt = startCol + vMove;
                // Check if move is out of bounds (Row and Col positions must be b/t 1 and 8)
                while (tempRowInt >= 1 & tempRowInt <= 8 & tempColInt >= 1 & tempColInt <= 8){
                    // Check if there's a piece in the way
                    ChessPosition candidatePosition = new ChessPosition(tempRowInt, tempColInt);
                    boolean pieceAtPosition = false; // This will be the default
                    if (board.getPiece(candidatePosition) != null){
                        pieceAtPosition = true;
                        // Check if it's an enemy piece
                        ChessPiece myPiece = board.getPiece(myPosition);
                        ChessPiece otherPiece = board.getPiece(candidatePosition);
                        if (myPiece.getTeamColor() == otherPiece.getTeamColor()){
                            break; // Don't capture your own piece. That would be silly.
                        }
                    }
                    // Add move to list
                    ChessMove validMove = new ChessMove(myPosition, candidatePosition, null);
                    moveCollection.add(validMove);
                    // If the piece has limited mobility (e.g., a king), stop here
                    if (!goToEndOfBoard){
                        break;
                    }
                    else{
                        // If you captured an enemy piece, stop here
                        if (pieceAtPosition){
                            break;
                        }
                        else{
                            // Update integers for next position check
                            tempRowInt = tempRowInt + hMove;
                            tempColInt = tempColInt + vMove;
                        }
                    }
                }
            }
        }
        return moveCollection;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", pieceColor, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}


