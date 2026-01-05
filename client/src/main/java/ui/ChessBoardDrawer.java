package ui;

import chess.*;

public class ChessBoardDrawer {
    public static void drawBoard(ChessBoard board, ChessGame.TeamColor perspective){
        if(perspective == ChessGame.TeamColor.WHITE){
            drawBoardWhitePerspective(board);
        } else {
            drawBoardBlackPerspective(board);
        }
    }

    private static void drawBoardWhitePerspective(ChessBoard board){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        System.out.println("    a  b  c  d  e  f  g  h");
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);

        for(int row = 8; row >= 1;row--){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            System.out.print(" " + row + " ");
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);

            for(int col = 1; col <= 8; col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                boolean isLightSquare = (row + col) % 2== 0;
                if(isLightSquare){
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                }

                if(piece != null){
                    String pieceSymbol = getPieceSymbol(piece);
                    System.out.print(pieceSymbol);
                }else{
                    System.out.print(EscapeSequences.EMPTY);
                }
                System.out.print(EscapeSequences.RESET_BG_COLOR);
            }
            //row nuymbers
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            System.out.print(" " + row);
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.println();
        }
        //columns
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        System.out.println("     a  b  c  d  e  f  g  h");
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static void drawBoardBlackPerspective(ChessBoard board){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        System.out.println("    a  b  c  d  e  f  g  h");
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);

        for(int row = 1; row >= 8;row++){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            System.out.print(" " + row + " ");
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);

            for(int col = 8; col <= 1; col--){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                boolean isLightSquare = (row + col) % 2== 0;
                if(isLightSquare){
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                }

                if(piece != null){
                    String pieceSymbol = getPieceSymbol(piece);
                    System.out.print(pieceSymbol);
                }else{
                    System.out.print(EscapeSequences.EMPTY);
                }
                System.out.print(EscapeSequences.RESET_BG_COLOR);
            }
            //row nuymbers
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            System.out.print(" " + row);
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.println();
        }
        //columns
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        System.out.println("     a  b  c  d  e  f  g  h");
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private static String getPieceSymbol(ChessPiece piece){
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        if (color == ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            return switch (type) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
