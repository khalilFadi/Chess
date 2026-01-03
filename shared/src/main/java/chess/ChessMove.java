package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    public ChessPosition startPosition, endPosition;
    public ChessPiece.PieceType promotionType;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionType = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionType;
    }
    private boolean comparePosition(ChessPosition reference, ChessPosition originial){
        return reference.getRow() == originial.getRow() && reference.getColumn() == originial.getColumn();
    }
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null || o.getClass() != getClass()) return false;
        ChessMove that = (ChessMove) o;
        return comparePosition(getStartPosition(), that.getStartPosition()) && comparePosition(getEndPosition(), that.getEndPosition()) && getPromotionPiece() == that.getPromotionPiece();
    }

    @Override
    public int hashCode(){
        //start position(2 digits), end position (2 digits), type 
        int start = getStartPosition().getRow() * 10 + getStartPosition().getColumn();
        int end = getEndPosition().getRow() * 10 + getEndPosition().getColumn();
        int prom = (getPromotionPiece() != null ? getPromotionPiece().ordinal() : 0);
        int result = start * 1000 + end * 10 + prom;
        return result;
    }
}
