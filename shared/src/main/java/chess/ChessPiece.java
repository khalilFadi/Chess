package chess;

import java.util.ArrayList;
import java.util.Collection;


import chess.ChessGame.TeamColor;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }


    @Override
    public boolean equals(Object o){
        if (this == o){ 
            return true;
        }else if(o == null || getClass() != o.getClass()){
            return false; 
        }
        ChessPiece that = (ChessPiece) o;
        if (that.getTeamColor() ==  getTeamColor() && that.getPieceType() == getPieceType()){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        //to do this i will be making each digit showing a differnt value so 
        // color(2):type(6):row(8):column(8) //nvm row and column aren't included 
        int result = getTeamColor() == TeamColor.WHITE ? 1 : 2;
        result = result*10 + getPieceType().ordinal();
        return result;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        
        switch (pieceType) {
            case ROOK:
                return rookMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case KING:
                return kingMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            default:
                return moves;
        }
    }  
    
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] direction : directions){
            int rowDelta = direction[0];
            int colDelta = direction[1];
            for(int i = 1; i <= 8; i++){
                int newRow = myPosition.getRow() + (rowDelta * i);
                int newCol = myPosition.getColumn() + (colDelta * i);
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8){
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceatThatPosition = board.getPiece(newPosition);
                if (pieceatThatPosition != null){
                    if (pieceatThatPosition.getTeamColor() != pieceColor){
                        moves.add(new ChessMove((myPosition), newPosition, null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};

        for (int[] direction : directions){
            int rowDelta = direction[0];
            int colDelta = direction[1];
            for(int i = 1; i<=8;i++){
                int newRow = myPosition.getRow() + (rowDelta * i);
                int newCol = myPosition.getColumn() + (colDelta * i);
                
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8){
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceatThatPosition = board.getPiece(newPosition);
                if (pieceatThatPosition != null){
                    if(pieceatThatPosition.getTeamColor() != pieceColor){
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(bishopMoves(board, myPosition));
        moves.addAll(rookMoves(board, myPosition));
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        int direction = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;

        int newRow = currentRow + direction;
        if (newRow >= 1 && newRow <= 8){
            ChessPosition forwardPos = new ChessPosition(newRow, currentCol);
            if (board.getPiece(forwardPos) == null){
                if (newRow == 8 || newRow == 1){
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.KNIGHT));
                } else{
                    moves.add(new ChessMove((myPosition), forwardPos, null));
                }
            }
        }
        if (currentRow == startRow){
            int doubleRow = currentRow + (2 * direction);
            if (doubleRow >= 1 && doubleRow <= 8){
                ChessPosition doublePos = new ChessPosition(doubleRow, currentCol);
                ChessPosition singlePos = new ChessPosition(currentRow + direction, currentCol);
                if (board.getPiece(singlePos) == null && board.getPiece(doublePos) == null){
                    moves.add(new ChessMove(myPosition, doublePos, null));
                }
            }
        }
        for (int colOffset : new int[]{-1, 1}) {
            int newestRow = currentRow + direction;
            int newCol = currentCol + colOffset;
            
            if (newestRow >= 1 && newestRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition capturePos = new ChessPosition(newestRow, newCol);
                ChessPiece pieceAtPos = board.getPiece(capturePos);
                
                // Can capture if there's an enemy piece
                if (pieceAtPos != null && pieceAtPos.getTeamColor() != pieceColor) {
                    // Check for promotion
                    if (newestRow == 8 || newestRow == 1) {
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(myPosition, capturePos, null));
                    }
                }
            }
        }
        return moves;
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] knightMoves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
        {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : knightMoves){
            int newestRow = myPosition.getRow() + move[0];
            int newCol = myPosition.getColumn() + move[1];

            if (newestRow >= 1 && newestRow <= 8 && newCol >= 1 && newCol <= 8){
                ChessPosition newPosition = new ChessPosition(newestRow, newCol);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);

                if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != pieceColor){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] direction : directions){
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8){
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);
                if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != pieceColor){
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return moves;
    }

}
