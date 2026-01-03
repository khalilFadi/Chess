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
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
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
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        TeamColor pieceColor = piece.getTeamColor();
        for(ChessMove move : allMoves){
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);

            if(!isInCheck(pieceColor)){
                validMoves.add(move);
            }
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), capturedPiece);
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
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece == null){
            throw new InvalidMoveException("no piece is being selected");
        }
        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("bro this isn't your team");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(!validMoves.contains(move)){
            throw new InvalidMoveException("idk you can't really go there");
        }

        ChessPiece movedPiece = piece;
        if (move.getPromotionPiece() != null){
            movedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }

        board.addPiece(move.getEndPosition(), movedPiece);
        board.addPiece(move.getStartPosition(), null);
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8;col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if( piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    kingPosition = pos;
                    break;
                }
            }
            if(kingPosition != null) break;
        }
        if (kingPosition == null) return false; // end game this is wrong
        TeamColor opposColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        for(int row = 1; row <= 8;row++){
            for(int col = 1;col <= 8;col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == opposColor){
                    Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                    for(ChessMove move : moves){
                        if(move.getEndPosition().equals((kingPosition))){
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8;col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if( piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = validMoves(pos);
                    if(moves != null && !moves.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //so basically neither check nor any valid moves 
        if(isInCheck(teamColor)){
            return false;
        }
        for (int row = 1; row <= 8;row++){
            for(int col = 1; col <= 8;col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if(piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = validMoves(pos);
                    if(moves != null && !moves.isEmpty()){
                        return false; //there' something to do
                    }
                }

            }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
