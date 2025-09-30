package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor team;
    private ChessBoard board;

    public ChessGame() {
        this.team = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> candidateMoves = ChessPiece.pieceMoves(board, startPosition);
        for (var move : candidateMoves){
            if (!isInCheck(team)){
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        if (!validMoves(startPosition).contains(move)){
            throw new InvalidMoveException("Invalid move");
        }
        ChessPiece myPiece = board.getPiece(startPosition);
        if (myPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("Not your turn!");
        }
        // I know this is a POLA violation but oh well
        board.addPiece(startPosition, null);
        board.addPiece(endPosition, myPiece);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (hasDiagonalThreats(teamColor)){
            return true;
        } else if (hasHorizontalVerticalThreats(teamColor)) {
            return true;
        } else {
            return hasKnightThreats(teamColor);
        }
    }

    private boolean hasDiagonalThreats(TeamColor teamColor){
        ChessPosition myKingPosition = getKingPosition(teamColor);

        // Check if there are pawns that can capture the king
        Collection<ChessPiece> enemyShortPieces = new ArrayList<>();

        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, -1, -1, 1));
        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, -1, 1, 1));
        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, 1, -1, 1));
        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, 1, 1, 1));

        if (checkIfThreat(enemyShortPieces, ChessPiece.PieceType.PAWN)){
            return true;
        }

        // Check for queens or bishops that can capture the king
        Collection<ChessPiece> enemyLongPieces = new ArrayList<>();

        enemyLongPieces.add(getEnemyPiece(board, myKingPosition, -1, -1, 8));
        enemyLongPieces.add(getEnemyPiece(board, myKingPosition, -1, 1, 8));
        enemyLongPieces.add(getEnemyPiece(board, myKingPosition, 1, -1, 8));
        enemyLongPieces.add(getEnemyPiece(board, myKingPosition, 1, 1, 8));

        if (checkIfThreat(enemyLongPieces, ChessPiece.PieceType.QUEEN)){
            return true;
        } else return checkIfThreat(enemyLongPieces, ChessPiece.PieceType.BISHOP);
    }

    private boolean hasHorizontalVerticalThreats(TeamColor teamColor){
        ChessPosition myKingPosition = getKingPosition(teamColor);
        Collection<ChessPiece> enemyPieces = new ArrayList<>();

        // Check if there are queens or rooks in horizontal/vertical paths
        enemyPieces.add(getEnemyPiece(board, myKingPosition, -1, 0, 8));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 1, 0, 8));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 0, -1, 8));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 0, 1, 8));

        if (checkIfThreat(enemyPieces, ChessPiece.PieceType.QUEEN)){
            return true;
        } else{
            return checkIfThreat(enemyPieces, ChessPiece.PieceType.ROOK);
        }
    }

    private boolean hasKnightThreats(TeamColor teamColor){
        ChessPosition myKingPosition = getKingPosition(teamColor);
        Collection<ChessPiece> enemyPieces = new ArrayList<>();

        // Check for all possible knight locations
        enemyPieces.add(getEnemyPiece(board, myKingPosition, -2, -1, 1));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, -2, 1, 1));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 2, -1, 1));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 2, 1, 1));

        enemyPieces.add(getEnemyPiece(board, myKingPosition, -1, -2, 1));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, -1, 2, 1));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 1, -2, 1));
        enemyPieces.add(getEnemyPiece(board, myKingPosition, 1, 2, 1));

        return checkIfThreat(enemyPieces, ChessPiece.PieceType.KNIGHT);
    }

    private boolean checkIfThreat (Collection<ChessPiece> enemyPieces, ChessPiece.PieceType type){
        for (var enemyPiece : enemyPieces){
            if (enemyPiece != null && enemyPiece.getPieceType() == type){
                return true;
            }
        }
        return false;
    }

    private ChessPiece getEnemyPiece(ChessBoard board, ChessPosition myPosition, int hMove, int vMove, int moveLimit){
        ChessPosition currentPosition = myPosition;
        ChessPiece enemyPiece = null;
        int movesMade = 0;

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
                // Check if enemy. If so, return this piece as an enemy piece
                if (checkIfEnemy(board, myPosition, candidatePosition)){
                    enemyPiece = board.getPiece(candidatePosition);
                }
                // At this point, the piece is blocked and the loop stops
                break;
            }
            // Update parameters to continue loop if appropriate
            else{
                currentPosition = candidatePosition;
                movesMade ++;
            }
        }
        return enemyPiece;
    }

    private boolean checkIfEnemy(ChessBoard board, ChessPosition myPosition, ChessPosition candidatePosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessPiece otherPiece = board.getPiece(candidatePosition);

        return myPiece.getTeamColor() != otherPiece.getTeamColor();
    }

    private boolean checkIfInBounds(ChessPosition candidatePosition) {
        return (candidatePosition.getRow() >= 1 &&
                candidatePosition.getRow() <= 8 &&
                candidatePosition.getColumn() >= 1 &&
                candidatePosition.getColumn() <= 8);
    }

    private ChessPosition getCandidatePosition(ChessPosition myPosition, int hMove, int vMove){
        return new ChessPosition(myPosition.getRow() + vMove, myPosition.getColumn() + hMove);
    }

    private ChessPosition getKingPosition(TeamColor teamColor){
        ChessPosition myKingPosition = null;
        ChessPiece myKingPiece = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        int i = 1;
        int j = 1;
        while (i <= 8){
            while (j <= 8){
                ChessPosition candidatePosition = new ChessPosition(i, j);
                ChessPiece candidatePiece = board.getPiece(candidatePosition);
                if (candidatePiece.equals(myKingPiece)){
                    myKingPosition = candidatePosition;
                }
                j++;
            }
            i++;
        }
        return myKingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "team=" + team +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, board);
    }
}
