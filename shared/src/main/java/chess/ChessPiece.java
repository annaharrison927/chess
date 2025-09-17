package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
        Collection<ChessMove> moveCollection = new ArrayList<>();
        if (piece.getPieceType() == PieceType.BISHOP) {
            moveCollection = findBishopMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.KING) {
            moveCollection = findKingMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            moveCollection = findKnightMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.ROOK) {
            moveCollection = findRookMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.QUEEN) {
            moveCollection = findQueenMoves(myPosition, board);
        }
        return moveCollection;
    }

    // HELPER METHODS

    // Returns valid Bishop moves
    private Collection<ChessMove> findBishopMoves(ChessPosition myPosition, ChessBoard board){
        Collection<ChessPosition> candidatePositions = new ArrayList<>();

        // Find diagonal moves (use loop since Bishop can move until blocked)
        candidatePositions.addAll(loopThroughPositions(myPosition, board, -1, -1, "diagonal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, -1, 1, "diagonal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 1, -1, "diagonal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 1, 1, "diagonal"));

        return makeValidMoveCollection(myPosition, board, candidatePositions);
    }

    private Collection<ChessMove> findRookMoves(ChessPosition myPosition, ChessBoard board){
        Collection<ChessPosition> candidatePositions = new ArrayList<>();

        // Find horizontal moves
        candidatePositions.addAll(loopThroughPositions(myPosition, board, -1, 0, "horizontal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 1, 0, "horizontal"));

        // Find vertical moves
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 0, -1, "vertical"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 0, 1, "vertical"));

        return makeValidMoveCollection(myPosition, board, candidatePositions);
    }

    // Returns valid King moves
    private Collection<ChessMove> findKingMoves(ChessPosition myPosition, ChessBoard board){
        Collection<ChessPosition> candidatePositions = new ArrayList<>();

        // Find diagonal moves
        candidatePositions.add(moveDiagonal(myPosition, -1, -1));
        candidatePositions.add(moveDiagonal(myPosition, -1, 1));
        candidatePositions.add(moveDiagonal(myPosition, 1, -1));
        candidatePositions.add(moveDiagonal(myPosition, 1, 1));

        // Find horizontal moves
        candidatePositions.add(moveHorizontal(myPosition, -1));
        candidatePositions.add(moveHorizontal(myPosition, 1));

        // Find vertical moves
        candidatePositions.add(moveVertical(myPosition, -1));
        candidatePositions.add(moveVertical(myPosition, 1));

        return makeValidMoveCollection(myPosition, board, candidatePositions);
    }

    // Return valid Knight moves
    private Collection<ChessMove> findKnightMoves(ChessPosition myPosition, ChessBoard board){
        Collection<ChessPosition> candidatePositions = new ArrayList<>();

        // This accounts for all 8 possible positions a knight can go to
        // Kind of redundant but this will do for now
        ChessPosition tempPosition1 = moveHorizontal(myPosition, -2);
        candidatePositions.add(moveVertical(tempPosition1, -1));

        ChessPosition tempPosition2 = moveHorizontal(myPosition, -2);
        candidatePositions.add(moveVertical(tempPosition2, 1));

        ChessPosition tempPosition3 = moveHorizontal(myPosition, -1);
        candidatePositions.add(moveVertical(tempPosition3, -2));

        ChessPosition tempPosition4 = moveHorizontal(myPosition, -1);
        candidatePositions.add(moveVertical(tempPosition4, 2));

        ChessPosition tempPosition5 = moveHorizontal(myPosition, 1);
        candidatePositions.add(moveVertical(tempPosition5, -2));

        ChessPosition tempPosition6 = moveHorizontal(myPosition, 1);
        candidatePositions.add(moveVertical(tempPosition6, 2));

        ChessPosition tempPosition7 = moveHorizontal(myPosition, 2);
        candidatePositions.add(moveVertical(tempPosition7, -1));

        ChessPosition tempPosition8 = moveHorizontal(myPosition, 2);
        candidatePositions.add(moveVertical(tempPosition8, 1));

        return makeValidMoveCollection(myPosition, board, candidatePositions);
    }

    private Collection<ChessMove> findQueenMoves(ChessPosition myPosition, ChessBoard board){
        Collection<ChessPosition> candidatePositions = new ArrayList<>();

        // MAKE THIS MORE EFFICIENT LATER, THIS REPEATS A LOT OF STUFF
        // Find diagonal moves
        candidatePositions.addAll(loopThroughPositions(myPosition, board, -1, -1, "diagonal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, -1, 1, "diagonal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 1, -1, "diagonal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 1, 1, "diagonal"));

        // Find horizontal moves
        candidatePositions.addAll(loopThroughPositions(myPosition, board, -1, 0, "horizontal"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 1, 0, "horizontal"));

        // Find vertical moves
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 0, -1, "vertical"));
        candidatePositions.addAll(loopThroughPositions(myPosition, board, 0, 1, "vertical"));

        return makeValidMoveCollection(myPosition, board, candidatePositions);
    }

    // Returns position after moving diagonally
    private ChessPosition moveDiagonal(ChessPosition myPosition, int hMove, int vMove){
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        return new ChessPosition(myRow + hMove, myCol + vMove);
    }

    // Returns position after moving horizontally
    private ChessPosition moveHorizontal(ChessPosition myPosition, int hMove){
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        return new ChessPosition(myRow + hMove, myCol);
    }

    // Returns position after moving vertically
    private ChessPosition moveVertical(ChessPosition myPosition, int vMove){
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        return new ChessPosition(myRow, myCol + vMove);
    }

    // For pieces that can move until blocked, this loop finds all possible moves in the direction it's going
    private Collection<ChessPosition> loopThroughPositions(ChessPosition myPosition, ChessBoard board, int hMove, int vMove, String moveType){
        Collection<ChessPosition> candidatePositions = new ArrayList<>();
        ChessPosition currentPosition = myPosition;
        while (true){
            ChessPosition candidatePosition = null;
            // Create position based on move type
            if (Objects.equals(moveType, "diagonal")){
                candidatePosition = moveDiagonal(currentPosition, hMove, vMove);
            } else if (Objects.equals(moveType, "horizontal")) {
                candidatePosition = moveHorizontal(currentPosition, hMove);
            } else if (Objects.equals(moveType, "vertical")) {
                candidatePosition = moveVertical(currentPosition, vMove);
            }
            // Check if in bounds
            if (!checkIfInBounds(candidatePosition.getRow(), candidatePosition.getColumn())){
                break;
            }
            // Check if position is occupied
            else if (board.getPiece(candidatePosition) != null) {
                // Check if enemy piece
                if (checkIfEnemy(board, myPosition, candidatePosition)){
                    candidatePositions.add(candidatePosition);
                }
                break;
            }
            else{
                candidatePositions.add(candidatePosition);
                currentPosition = candidatePosition;
            }
        }
        return candidatePositions;
    }

    // Returns valid moves based on valid end positions
    private Collection<ChessMove> makeValidMoveCollection(ChessPosition myPosition, ChessBoard board, Collection<ChessPosition> candidatePositions){
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (var candidatePosition : candidatePositions){
            ChessMove candidateMove = new ChessMove(myPosition, candidatePosition, null);
            // Make sure piece isn't out of bounds
            if (checkIfInBounds(candidatePosition.getRow(), candidatePosition.getColumn())){
                // Check if the piece is open or occupied by an enemy
                if (board.getPiece(candidatePosition) != null){
                    // Add position if enemy piece is there
                    if (checkIfEnemy(board, myPosition, candidatePosition)){
                        validMoves.add(candidateMove);
                    }
                }
                else{
                    validMoves.add(candidateMove);
                }
            }
        }
        return validMoves;
    }

    // Checks if a piece at a given position is an enemy
    private boolean checkIfEnemy(ChessBoard board, ChessPosition myPosition, ChessPosition endPos){
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessPiece otherPiece = board.getPiece(endPos);
        return myPiece.getTeamColor() != otherPiece.getTeamColor(); // Don't capture your own piece. That would be silly.
    }

    // Makes sure piece doesn't go out of bounds
    private boolean checkIfInBounds(int rowInt, int colInt){
        return rowInt >= 1 & rowInt <= 8 & colInt >= 1 & colInt <= 8;
    }

    // OVERRIDE METHODS

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


