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
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(myPosition);

        if (myPiece.getPieceType() == PieceType.BISHOP){
            validMoves = getBishopMoves(board, myPosition);
        }
        if (myPiece.getPieceType() == PieceType.KING){
            validMoves = getKingMoves(board, myPosition);
        }
        if (myPiece.getPieceType() == PieceType.KNIGHT){
            validMoves = getKnightMoves(board, myPosition);
        }
        if (myPiece.getPieceType() == PieceType.ROOK){
            validMoves = getRookMoves(board, myPosition);
        }
        if (myPiece.getPieceType() == PieceType.QUEEN){
            validMoves = getQueenMoves(board, myPosition);
        }
        if (myPiece.getPieceType() == PieceType.PAWN){
            validMoves = getPawnMoves(board, myPosition);
        }

        return validMoves;
    }

    // HELPER METHODS
    private static Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getValidMoves(board, myPosition, -1, -1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 1, -1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 1, 8));

        return validMoves;
    }

    private static Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Diagonal Moves
        validMoves.addAll(getValidMoves(board, myPosition, -1, -1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 1, -1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 1, 1));

        // Horizontal/Vertical Moves
        validMoves.addAll(getValidMoves(board, myPosition, 0, -1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 0, 1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 0, 1));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 0, 1));

        return validMoves;
    }

    private static Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getValidMoves(board, myPosition, -2, -1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, -2, 1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 2, -1, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 2, 1, 1));

        validMoves.addAll(getValidMoves(board, myPosition, -1, -2, 1));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 2, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 1, -2, 1));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 2, 1));

        return validMoves;
    }

    private static Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getValidMoves(board, myPosition, 0, -1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 0, 1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 0, 8));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 0, 8));

        return validMoves;
    }

    private static Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Diagonal Moves
        validMoves.addAll(getValidMoves(board, myPosition, -1, -1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 1, -1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 1, 8));

        // Horizontal/Vertical Moves
        validMoves.addAll(getValidMoves(board, myPosition, 0, -1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 0, 1, 8));
        validMoves.addAll(getValidMoves(board, myPosition, 1, 0, 8));
        validMoves.addAll(getValidMoves(board, myPosition, -1, 0, 8));

        return validMoves;
    }

    private static Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        // GET PARAMETERS BASED ON COLOR
        // Black is default
        ChessPiece myPiece = board.getPiece(myPosition);
        int vMove = -1;
        int vMoveStart = -2;
        int startRow = 7;
        int promoRow = 2;
        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            vMove = 1;
            vMoveStart = 2;
            startRow = 2;
            promoRow = 7;
        }

        // GET FORWARD MOVES
        ChessPosition candidatePosition = getCandidatePosition(myPosition, 0, vMove);
        // Check if in bounds and make sure position is empty
        if (checkIfInBounds(candidatePosition) && board.getPiece(candidatePosition) == null){
            // Check if promotion piece
            if (myPosition.getRow() == promoRow){
                validMoves.addAll(getValidPromotionMoves(myPosition, candidatePosition));
            }
            // Otherwise, add valid move
            else{
                validMoves.add(new ChessMove(myPosition, candidatePosition, null));
                // Check if start piece
                if (myPosition.getRow() == startRow){
                    ChessPosition candidateStartPosition = getCandidatePosition(myPosition, 0, vMoveStart);
                    // Make sure start piece is empty
                    if (board.getPiece(candidateStartPosition) == null){
                        validMoves.add(new ChessMove(myPosition, candidateStartPosition, null));
                    }
                }
            }
        }

        // GET DIAGONAL MOVES
        ChessPosition candidateLeftPosition = getCandidatePosition(myPosition, -1, vMove);
        ChessPosition candidateRightPosition = getCandidatePosition(myPosition, 1, vMove);

        Collection<ChessMove> candidateLeftMoves = getValidPawnCaptureMoves(board, myPosition, candidateLeftPosition, promoRow);
        Collection<ChessMove> candidateRightMoves = getValidPawnCaptureMoves(board, myPosition, candidateRightPosition, promoRow);

        if (!candidateLeftMoves.isEmpty()){
            validMoves.addAll(candidateLeftMoves);
        }
        if (!candidateRightMoves.isEmpty()){
            validMoves.addAll(candidateRightMoves);
        }

        return validMoves;
    }

    private static Collection<ChessMove> getValidPawnCaptureMoves(ChessBoard board, ChessPosition myPosition, ChessPosition candidatePosition, int promoRow){
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Check if in bounds, position is occupied, and has an enemy piece to capture
        if (checkIfInBounds(candidatePosition) &&
                board.getPiece(candidatePosition) != null &&
                checkIfEnemy(board, myPosition, candidatePosition)){
            // Check if promotion piece
            if (myPosition.getRow() == promoRow){
                validMoves.addAll(getValidPromotionMoves(myPosition, candidatePosition));
            }
            else{
                validMoves.add(new ChessMove(myPosition, candidatePosition, null));
            }
        }
        return validMoves;
    }

    private static Collection<ChessMove> getValidPromotionMoves(ChessPosition myPosition, ChessPosition candidatePosition){
        Collection<ChessMove> validMoves = new ArrayList<>();

        validMoves.add(new ChessMove(myPosition, candidatePosition, PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, candidatePosition, PieceType.KNIGHT));
        validMoves.add(new ChessMove(myPosition, candidatePosition, PieceType.ROOK));
        validMoves.add(new ChessMove(myPosition, candidatePosition, PieceType.BISHOP));

        return validMoves;
    }

    private static Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition myPosition, int hMove, int vMove, int moveLimit){
        // Does not apply to pawns
        Collection<ChessMove> validMoves = new ArrayList<>();
        int movesMade = 0;
        ChessPosition currentPosition = myPosition;

        while(true){
            // Don't go past the move limit
            if (movesMade == moveLimit){
                break;
            }
            // Make sure move is in bounds
            ChessPosition candidatePosition = getCandidatePosition(currentPosition, hMove, vMove);
            if (!checkIfInBounds(candidatePosition)){
                break;
            }
            // Check if the spot is blocked
            if (board.getPiece(candidatePosition) != null){
                // Check if enemy. If so, capture and add valid move
                if (checkIfEnemy(board, myPosition, candidatePosition)){
                    validMoves.add(new ChessMove(myPosition, candidatePosition, null));
                }
                // At this point, the piece is blocked and the loop stops
                break;
            }
            // Update parameters to continue loop if appropriate
            else{
                validMoves.add(new ChessMove(myPosition, candidatePosition, null));
                movesMade ++;
                currentPosition = candidatePosition;
            }
        }
        return validMoves;
    }

    private static ChessPosition getCandidatePosition(ChessPosition myPosition, int hMove, int vMove){
        return new ChessPosition(myPosition.getRow() + vMove, myPosition.getColumn() + hMove);
    }

    private static boolean checkIfInBounds(ChessPosition candidatePosition){
        return (candidatePosition.getRow() >= 1 &&
                candidatePosition.getRow() <= 8 &&
                candidatePosition.getColumn() >= 1 &&
                candidatePosition.getColumn() <= 8);
    }

    private static boolean checkIfEnemy(ChessBoard board, ChessPosition myPosition, ChessPosition candidatePosition){
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessPiece otherPiece = board.getPiece(candidatePosition);

        return myPiece.getTeamColor() != otherPiece.getTeamColor();
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


