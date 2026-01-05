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
    private boolean whiteKingHasMoved = false;
    private boolean blackKingHasMoved = false;
    private boolean whiteRookKingsideMoved = false;
    private boolean whiteRookQueensideMoved = false;
    private boolean blackRookKingsideMoved = false;
    private boolean blackRookQueensideMoved = false;

    private ChessPosition enPassantTarget = null;

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
    public Collection<ChessMove> addCasteling(Collection<ChessMove> moves, ChessPosition starPosition, ChessPiece piece, ChessGame.TeamColor teamColor){
        // check if the rock moved before 
        // check the position that we have and according to the position check whichever one is there and if it moved before 
        // i can do a switch whiever one it matches check if it moved before
        int kingRow = starPosition.getRow();
        int kingCol =  starPosition.getColumn();
        if(kingCol != 5){
            return moves;
        }
        //did the king move yet
        if((teamColor == TeamColor.WHITE && whiteKingHasMoved)|| 
        (teamColor == TeamColor.BLACK && blackKingHasMoved)){
            return moves;
        }

        //can't work in check 
        if(isInCheck(teamColor)){
            return moves;
        }

        switch (kingRow){
            case 1: //bottom so white 
                if(!whiteRookKingsideMoved && canCastleKingSide(teamColor, kingRow)){
                    moves.add(new ChessMove(starPosition, new ChessPosition(kingRow, 7), null));
                }
                //other side
                if(!whiteRookQueensideMoved && canCastleQueenSide(teamColor, kingRow)){
                    moves.add(new ChessMove(starPosition, new ChessPosition(kingRow, 3), null));
                }
                break;
            case 8: //top side so black
                if (!blackRookKingsideMoved && canCastleKingSide(teamColor, kingRow)) {
                    moves.add(new ChessMove(starPosition, new ChessPosition(kingRow, 7), null));
                }
                // Queenside castling
                if (!blackRookQueensideMoved && canCastleQueenSide(teamColor, kingRow)) {
                    moves.add(new ChessMove(starPosition, new ChessPosition(kingRow, 3), null));
                }
                break;
            default:
                return moves;
        }
        return moves;
    }
    private boolean canCastleKingSide(TeamColor teamColor, int row){
        ChessPiece rook = board.getPiece(new ChessPosition(row, 8));
        if (rook == null || rook.getPieceType() != ChessPiece.PieceType.ROOK || 
            rook.getTeamColor() != teamColor) {
            return false;
        }
        //check spaces between are basicaly empty 
        if(board.getPiece(new ChessPosition(row, 6)) != null || board.getPiece(new ChessPosition(row, 7)) != null){
            return false;
        }
        //check that all squares king ends up on are not attacked.
        if (wouldBeInCheckAfterThisMove(teamColor, new ChessPosition(row, 6)) || 
        wouldBeInCheckAfterThisMove(teamColor, new ChessPosition(row, 7))){
                return false;
        }
        return true;
    }
    private boolean canCastleQueenSide(TeamColor teamColor, int row){
        ChessPiece rook = board.getPiece(new ChessPosition(row, 1));
        if (rook == null || rook.getPieceType() != ChessPiece.PieceType.ROOK || 
            rook.getTeamColor() != teamColor) {
            return false;
        }
        //check spaces between are basicaly empty 
        if(board.getPiece(new ChessPosition(row, 4)) != null || 
        board.getPiece(new ChessPosition(row, 3)) != null || 
        board.getPiece(new ChessPosition(row, 2)) != null){
            return false;
        }
        //check that all squares king ends up on are not attacked.
        if (wouldBeInCheckAfterThisMove(teamColor, new ChessPosition(row, 4)) || 
        wouldBeInCheckAfterThisMove(teamColor, new ChessPosition(row, 3))){
                return false;
        }
        return true;
    }

    private boolean wouldBeInCheckAfterThisMove(TeamColor teamColor, ChessPosition position){
        ChessPosition ogKingPosition = findKing(teamColor);
        ChessPiece king = board.getPiece(ogKingPosition);

        board.addPiece(ogKingPosition, null);
        board.addPiece(position, king);

        boolean ischecked = isInCheck(teamColor);
        board.addPiece(ogKingPosition, king);
        board.addPiece(position, null);
        return ischecked;
    }   
    
    private ChessPosition findKing(TeamColor teamColor){
        for(int row = 1; row <= 8; row++){
            for(int col = 1;col <= 8;col++){
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    public Collection<ChessMove> addenPassant(Collection<ChessMove> moves, ChessPosition startPosition, ChessPiece piece){
        if(enPassantTarget == null){
            return moves;
        }
        int pawnRow = startPosition.getRow();
        int pawnCol = startPosition.getColumn();
        int targetRow = enPassantTarget.getRow();
        int targetCol = enPassantTarget.getColumn();

        if(pawnRow != targetRow){
            return moves;
        }
        if(Math.abs(pawnCol - targetCol) != 1){
            return moves;
        }
        int direction = (piece.getTeamColor() == TeamColor.WHITE) ? 1 : -1;
        int captureRow = targetRow + direction;
        ChessPosition capturePosition = new ChessPosition(captureRow, targetCol);
        moves.add(new ChessMove(startPosition, capturePosition, null));
        return moves; 
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);

        //special moves 
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            //casteling
            addCasteling(allMoves, startPosition, piece, teamTurn);
        }
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            //en passant 
            addenPassant(allMoves, startPosition, piece);
        }
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
        // casteling 
        boolean isCastling = false;
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            int startCol = move.getStartPosition().getColumn();
            int endCol = move.getEndPosition().getColumn();
            if (startCol == 5 && (endCol == 7 || endCol == 3)){
                isCastling = true;
                handleCastling(move, piece.getTeamColor());
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            if (piece.getTeamColor() == TeamColor.WHITE){
                whiteKingHasMoved = true;
            }else{
                blackKingHasMoved = true;
            }
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK){
            int row = move.getStartPosition().getRow();
            int col = move.getStartPosition().getColumn();
            if(row == 1 && col == 8) whiteRookKingsideMoved = true;
            if(row == 1 && col == 1) whiteRookQueensideMoved = true;
            if(row == 8 && col == 8) blackRookKingsideMoved= true;
            if(row == 8 && col == 1) blackRookQueensideMoved = true;
        }
        //handle el pessant target tracking 
        boolean isEnPassant = false;
        ChessPosition capturedPawnPosition = null;
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && enPassantTarget != null) {
            int startRow = move.getStartPosition().getRow();
            int startCol = move.getStartPosition().getColumn();
            int endRow = move.getEndPosition().getRow();
            int endCol = move.getEndPosition().getColumn();
            int targetRow = enPassantTarget.getRow();
            int targetCol = enPassantTarget.getColumn();
            if (startRow == targetRow && Math.abs(startCol - targetCol) == 1 && 
                endCol == targetCol && endRow == targetRow + (piece.getTeamColor() == TeamColor.WHITE ? 1 : -1)) {
                isEnPassant = true;
                capturedPawnPosition = enPassantTarget;
            }
        }
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            int startRow = move.getStartPosition().getRow();
            int endRow = move.getEndPosition().getRow();
            if(Math.abs(endRow - startRow)  == 2) {
                enPassantTarget = move.getEndPosition();
            }else{
                enPassantTarget = null;
            }
        }else{
            enPassantTarget = null;
        }
        
        if (!isCastling){
            ChessPiece movedPiece = piece;
            if (move.getPromotionPiece() != null){
                movedPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            }

            board.addPiece(move.getEndPosition(), movedPiece);
            board.addPiece(move.getStartPosition(), null);
            if (isEnPassant) {
                board.addPiece(capturedPawnPosition, null);
            }
            
        }
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }
    private void handleCastling(ChessMove move, TeamColor teamColor){
        int row = move.getStartPosition().getRow();
        int endCol = move.getEndPosition().getColumn();
        //moving the king
        ChessPiece king = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), king);
        board.addPiece(move.getStartPosition(), null);

        //move the rock
        if(endCol == 7){
            ChessPosition rookstart = new ChessPosition(row, 8);
            ChessPosition rookend = new ChessPosition(row, 6);
            ChessPiece rook = board.getPiece(rookstart);
            board.addPiece(rookend, rook);
            board.addPiece(rookstart, null);
        }else if(endCol == 3){
            ChessPosition rookstart = new ChessPosition(row, 1);
            ChessPosition rookend = new ChessPosition(row, 4);
            ChessPiece rook = board.getPiece(rookstart);
            board.addPiece(rookend, rook);
            board.addPiece(rookstart, null);
        }
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
