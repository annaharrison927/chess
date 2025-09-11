package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        List<ChessMove> moveCollection = new ArrayList<>();
        // VALID MOVES FOR BISHOP
        if (piece.getPieceType() == PieceType.BISHOP){
            // Bishop can move any diagonal direction as long as the path isn't blocked
            List<Integer> horizontalMoves = List.of(-1, 1);
            List<Integer> verticalMoves = List.of(-1, 1);
            int startRow = myPosition.getRow();
            int startCol = myPosition.getColumn();
            // Starting from the current position, iterate through all diagonal directions and add to list
            // GO BACK LATER AND UPDATE FOR EFFICIENCY
            for (var hMove : horizontalMoves){
                for (var vMove : verticalMoves){
                    int tempRowInt = startRow + hMove;
                    int tempColInt = startCol + vMove;
                    // Check if move is out of bounds (Row and Col positions must be b/t 1 and 8)
                    while (tempRowInt > 1 & tempRowInt < 8 & tempColInt > 1 & tempRowInt < 8){
                        // Check if there's a piece in the way
                        ChessPosition candidatePosition = new ChessPosition(tempRowInt, tempColInt);
                        if (board.getPiece(candidatePosition) != null){
                            break;
                        }
                        // If the path is clear, add to list
                        else{
                            ChessMove validMove = new ChessMove(myPosition, candidatePosition, null);
                            moveCollection.add(validMove);
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
}
