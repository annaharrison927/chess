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

        ChessPiece myPiece = board.getPiece(startPosition);
        TeamColor teamColor = myPiece.getTeamColor();

        for (var move : candidateMoves){
            ChessBoard currentBoard = getBoard();
            ChessPosition tempStartPosition = move.getStartPosition();
            ChessPosition tempEndPosition = move.getEndPosition();
            ChessBoard tempBoard = makeTempBoard(currentBoard, tempStartPosition, tempEndPosition);
            setBoard(tempBoard);
            if (!isInCheck(teamColor)){
                validMoves.add(move);
            }
            setBoard(currentBoard);
        }
        return validMoves;
    }

    private ChessBoard makeTempBoard(ChessBoard currentBoard, ChessPosition tempStartPosition, ChessPosition tempEndPosition){
        ChessBoard tempBoard = new ChessBoard();
        ChessPiece pieceToMove = currentBoard.getPiece(tempStartPosition);
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i, j);
                if (currentPosition.equals(tempStartPosition)){
                    tempBoard.addPiece(currentPosition, null);
                } else if (currentPosition.equals(tempEndPosition)) {
                    tempBoard.addPiece(currentPosition, pieceToMove);
                } else{
                    ChessPiece currentPiece = currentBoard.getPiece(currentPosition);
                    tempBoard.addPiece(currentPosition, currentPiece);
                }
            }
        }
        return tempBoard;
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
        if (board.getPiece(startPosition) == null){
            throw new InvalidMoveException("No piece to move!");
        }
        if (!validMoves(startPosition).contains(move)){
            throw new InvalidMoveException("Invalid move");
        }
        ChessPiece myPiece = board.getPiece(startPosition);
        // Change piece for promotion moves
        if (myPiece.getPieceType() == ChessPiece.PieceType.PAWN){
            if ((myPiece.getTeamColor() == TeamColor.WHITE && endPosition.getRow() == 8) ||
                    (myPiece.getTeamColor() == TeamColor.BLACK && endPosition.getRow() == 1)){
                myPiece = new ChessPiece(myPiece.getTeamColor(), move.getPromotionPiece());
            }
        }
        if (myPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("Not your turn!");
        }
        if (move.getPromotionPiece() == ChessPiece.PieceType.PAWN || move.getPromotionPiece() == ChessPiece.PieceType.KING){
            throw new InvalidMoveException("Invalid promotion!");
        }
        // I know this is a POLA violation but oh well. Move the piece
        board.addPiece(startPosition, null);
        board.addPiece(endPosition, myPiece);

        // Update team turn
        if (myPiece.getTeamColor() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
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

        // Check if there are pawns or king that can capture the king
        Collection<ChessPiece> enemyShortPieces = new ArrayList<>();

        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, -1, -1, 1));
        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, -1, 1, 1));
        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, 1, -1, 1));
        enemyShortPieces.add(getEnemyPiece(board, myKingPosition, 1, 1, 1));

        if (checkIfThreat(enemyShortPieces, ChessPiece.PieceType.PAWN)){
            return true;
        } else if (checkIfThreat(enemyShortPieces, ChessPiece.PieceType.KING)) {
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
        } else if (checkIfThreat(enemyPieces, ChessPiece.PieceType.ROOK)) {
            return true;
        }

        Collection<ChessPiece> enemyKingPieces = new ArrayList<>();

        // Check if there are kings in horizontal/vertical paths
        enemyKingPieces.add(getEnemyPiece(board, myKingPosition, -1, 0, 1));
        enemyKingPieces.add(getEnemyPiece(board, myKingPosition, 1, 0, 1));
        enemyKingPieces.add(getEnemyPiece(board, myKingPosition, 0, -1, 1));
        enemyKingPieces.add(getEnemyPiece(board, myKingPosition, 0, 1, 1));

        return checkIfThreat(enemyKingPieces, ChessPiece.PieceType.KING);


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
                if (candidatePiece != null && candidatePiece.equals(myKingPiece)){
                    myKingPosition = candidatePosition;
                    break;
                }
                j++;
            }
            i++;
            j = 1;
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
        if (!isInCheck(teamColor)){
            return false;
        } else{
            return teamHasNoValidMoves(teamColor);
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        } else{
            return teamHasNoValidMoves(teamColor);
        }
    }

    private boolean teamHasNoValidMoves(TeamColor teamColor){
        int i = 1;
        int j = 1;

        while (i <= 8){
            while (j <= 8){
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor){
                    Collection<ChessMove> currentValidMoves = validMoves(currentPosition);
                    if (!currentValidMoves.isEmpty()){
                        return false;
                    }
                }
                j++;
            }
            i++;
            j = 1;
        }
        return true;
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
