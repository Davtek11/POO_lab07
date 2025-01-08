package engine;

import chess.*;

import java.util.ArrayList;
import java.util.List;

import static engine.ChessPiece.board;

public class ChessGame implements ChessController {

  private ChessView view;
  private PlayerColor colorTurn;
  private Coord pieceEnPassant;
  private Coord whiteKing;
  private Coord blackKing;
  private boolean end;

  @Override
  public void start(ChessView view) {
    this.view = view;
    view.startView();
  }

  // 0 = nothing, -1 = petit roque, 1 = grand roque
  public int checkRoque(int fromX, int fromY, int toX, int toY) {
    int ret = 0;
    if(board[fromX][fromY].isFirstMove() && board[fromX][fromY].type == PieceType.KING && fromY == toY) {
      int rookX = -1;
      if(toX == fromX + 2) {
        // Petit roque
        rookX = toX + 1;
        ret = -1;
      } else if(toX == fromX - 2) {
        // Grand roque
        rookX = toX - 2;
        ret = 1;
      } else {
        return 0;
      }

      if(board[fromX][fromY].piecesCheck(rookX, toY) && board[rookX][toY] != null && board[rookX][toY].type == PieceType.ROOK && board[rookX][toY].isFirstMove()) {
        return ret;
      }
    }
    return 0;
  }

  public void roque(int x, int y, int roqueType) {

    int toX = 0, rookX = 0, rookToX = 0;

    if(roqueType == -1) {
      // Petit roque
      toX = x + 2;
      rookX = toX + 1;
      rookToX = x + 1;
      
    } else if(roqueType == 1) {
      // Grand roque
      toX = x - 2;
      rookX = toX - 2;
      rookToX = x - 1;
    }

    view.displayMessage("Roque by " + board[x][y].color + " player");

    // Move king
    movePiece(x, y, toX, y);

    // Move rook
    movePiece(rookX, y, rookToX, y);
  }

  public boolean checkPromotion(int toX, int toY) {
    return board[toX][toY].type == PieceType.PAWN &&
    (board[toX][toY].color == PlayerColor.BLACK && toY == 0 ||
    board[toX][toY].color == PlayerColor.WHITE && toY == Coord.BOARD_SIZE - 1);
  }

  public void promotion(int x, int y) {

    try {
      PieceChoice[] choices = {new PieceChoice("reine", PieceType.QUEEN, Queen.class),
      new PieceChoice("cavalier", PieceType.KNIGHT, Knight.class),
      new PieceChoice("fou", PieceType.BISHOP, Bishop.class),
      new PieceChoice("tour", PieceType.ROOK, Rook.class)};

      ChessView.UserChoice c = view.askUser("promotion", "par quelle pièce voulez-vous remplacer votre pion", choices);
      view.removePiece(x, y);

      int i;
      for(i = 0; i < choices.length; i++) {
        if(c.textValue().equalsIgnoreCase(choices[i].textValue())) {
          break;
        }
      }

      if(i < choices.length) {
        view.putPiece(choices[i].getType(), colorTurn, x, y);
        board[x][y] = (ChessPiece) choices[i].getPieceClass().getDeclaredConstructor(PlayerColor.class, int.class, int.class).newInstance(board[x][y].color, x, y);
      }
        
    } catch (Exception e) {
      System.out.println("Promotion error : " + e);
    }
    
  }

  public boolean checkEnPassant(int fromX, int fromY, int toX, int toY) {
    return board[fromX][fromY].type == PieceType.PAWN &&
    Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1 && // Pawn is moving diagonaly
    board[toX][fromY] != null && board[toX][fromY].type == PieceType.PAWN // Other piece is a pawn
    && board[toX][fromY].color != board[fromX][fromY].color && // Other piece is of opponent's color
    toX == pieceEnPassant.x && fromY == pieceEnPassant.y; // En passant is possible on that piece
  }

  public void enPassant(int fromX, int fromY, int toX, int toY) {
    view.displayMessage("en passant by " + board[fromX][fromY].color + " player");

    // Eat other piece
    view.removePiece(toX, fromY);
    board[toX][fromY] = null;

    // Move piece
    movePiece(fromX, fromY, toX, toY);
  }

  public void toggleTurn() {
    if(colorTurn == PlayerColor.WHITE)
      colorTurn = PlayerColor.BLACK;
    else if(colorTurn == PlayerColor.BLACK)
      colorTurn = PlayerColor.WHITE;
  }

  public void movePiece(int fromX, int fromY, int toX, int toY) {
    if (board[fromX][fromY].type == PieceType.KING) {
      if (board[fromX][fromY].color == PlayerColor.BLACK){
        blackKing = new Coord(toX, toY);
      }
      whiteKing = new Coord(toX, toY);
    }
    view.removePiece(fromX, fromY);
    view.putPiece(board[fromX][fromY].type, board[fromX][fromY].color, toX, toY);
    board[toX][toY] = board[fromX][fromY];
    board[toX][toY].pos.x = toX;
    board[toX][toY].pos.y = toY;
    board[fromX][fromY] = null;
  }

  public boolean knightCheck(Coord base) {
    int[][] moves = {
            {1, 2}, {-1, 2}, {2, -1}, {2, 1},
            {-1, -2}, {1, -2}, {-2, -1}, {-2, 1}
    };

    for (int[] move : moves) {
      int newX = base.x + move[0];
      int newY = base.y + move[1];

      if (isInsideBoard(newX, newY) &&
              board[newX][newY] != null &&
              board[newX][newY].type == PieceType.KNIGHT &&
              board[newX][newY].color != colorTurn) {
        return true;
      }
    }
    return false;
  }

  private boolean isInsideBoard(int x, int y) {
    return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
  }


  public void echecCheck(){
    Coord kingsCoord = colorTurn == PlayerColor.BLACK ? blackKing : whiteKing;
    boolean echec = false;
    boolean mat;
    boolean canNegate = false;
    boolean pat;

    if (isInEchec(kingsCoord)){
      view.displayMessage("Player " + colorTurn + " is in echec");
      echec = true;
    }

    mat = isSurrounded(kingsCoord);

    if (howMuchThreat(kingsCoord)){
      canNegate = canNegateThreat(kingsCoord);
    }

    if (echec && mat && !canNegate){
      view.displayMessage("Player " + colorTurn + " is in echec et mat he lost");
      end = true;

    }

    pat = !canAnyPieceMove(colorTurn);


    if (!echec && pat){
      view.displayMessage("Player " + colorTurn + " is in pat, it is a tie");
      end = true;
    }
  }

  private boolean isInEchec (Coord kingsCoord){
    for (int i = -1; i <2; i++){
      for (int j = -1; j <2; j++){
        if (i != 0 || j != 0){
          if (checkPotentialThreat(new Coord(i, j),kingsCoord,colorTurn)){
            return true;
          }
        }
      }
    }
    return knightCheck(kingsCoord);
  }

  private boolean isSurrounded (Coord kingsCoord){
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i != 0 || j != 0) {
          Coord tile = new Coord(kingsCoord.x + i, kingsCoord.y + j);
          if (isInsideBoard(tile.x, tile.y) &&
                  (board[tile.x][tile.y] == null || board[tile.x][tile.y].color != colorTurn)) {
            if (!isInEchec(tile) && !knightCheck(tile)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  private boolean canAnyPieceMove (PlayerColor color){
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        if (board[x][y] != null && board[x][y].color == color) {
          if (board[x][y].type == PieceType.KING) {
            if (!isSurrounded(new Coord(x, y))){
              return true;
            }

          } else {
            if (board[x][y].canMove()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean checkPotentialThreat(Coord add, Coord baseCoord, PlayerColor color) {
    Coord tile = new Coord(baseCoord.x, baseCoord.y);
    for (int i = 0; i < 8 - Math.max(Math.abs(add.x), Math.abs(add.y)); i++ ) {
      tile.x += add.x;
      tile.y += add.y;

      if (!isInsideBoard(tile.x, tile.y)){
        break;
      }


      if (board[tile.x][tile.y] != null) {
        if (board[tile.x][tile.y].color != color){
          if (board[tile.x][tile.y].move(baseCoord.x, baseCoord.y)) {
            return true;
          }
        }
        break;
      }
    }
    return false;
  }

  private boolean howMuchThreat (Coord kingsCoord){
    int count = 0;
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        if (board[x][y] != null && board[x][y].color != colorTurn) {
          if (board[x][y].move(kingsCoord.x, kingsCoord.y)) {
            count++;
          }
        }
      }
    }
    return count <= 1;
  }

  private List<Coord> getThreatPath (Coord kingsCoord, Coord threat){
    List<Coord> path = new ArrayList<>();
    int dx = Integer.compare(threat.x, kingsCoord.x);
    int dy = Integer.compare(threat.y, kingsCoord.y);

    int steps = Math.max(Math.abs(threat.x - kingsCoord.x), Math.abs(threat.y - kingsCoord.y));
    for (int i = 1; i < steps; i++) {
      path.add(new Coord(kingsCoord.x + i * dx, kingsCoord.y + i * dy));
    }

    return path;
  }

  private boolean canNegateThreat (Coord kingsCoord){
    ChessPiece threat = null;
    List<Coord> threatenedTiles = new ArrayList<>();

    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        ChessPiece piece = board[x][y];
        if (piece != null && piece.color != colorTurn) {
          if (piece.move(kingsCoord.x, kingsCoord.y)) {
            threat = piece;

            threatenedTiles.add(new Coord(x,y));
            if (piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN || piece.type == PieceType.ROOK){
              threatenedTiles.addAll(getThreatPath(kingsCoord, threat.pos));
            }
          }
        }
      }
    }

    if (threat != null){
      for (int x = 0; x < 8; x++) {
        for (int y = 0; y < 8; y++) {
          ChessPiece piece = board[x][y];
          if (piece != null && piece.color == colorTurn && piece.type != PieceType.KING) {
            for (Coord tile: threatenedTiles) {
              if (piece.move(tile.x, tile.y)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  private boolean stillechec (int fromX, int fromY, int toX, int toY){
    ChessPiece fromPiece = board[fromX][fromY];
    ChessPiece toPiece = board[toX][toY];

    boolean still = false;
    fromPiece.pos.x = toX;
    fromPiece.pos.y = toY;
    board[toX][toY] = fromPiece;
    board[fromX][fromY] = null;
    Coord kingsCoord = colorTurn == PlayerColor.BLACK ? blackKing : whiteKing;
    if (fromPiece.type == PieceType.KING){
      kingsCoord = new Coord(toX, toY);
    }
    if (isInEchec(kingsCoord)){
      view.displayMessage("you cannot do that");
      still =  true;
    }
    fromPiece.pos.x = fromX;
    fromPiece.pos.y = fromY;
    board[fromX][fromY] = fromPiece;
    board[toX][toY] = toPiece;

    return still;
  }

  @Override
  public boolean move(int fromX, int fromY, int toX, int toY) {
    if (end){
      view.displayMessage("the game has ended you cannot continue");
      return false;
    }

    if(board[fromX][fromY] == null) {
      return false;
    } else if(board[fromX][fromY].color != colorTurn) {
      System.out.println("Not your turn !!!!");
      return false;
    }

    // Roque if possible, else check other movements
    int roqueType = 0;
    if((roqueType = checkRoque(fromX, fromY, toX, toY)) != 0)
    {
      roque(fromX, fromY, roqueType);
    }
    else if(checkEnPassant(fromX, fromY, toX, toY))
    {
      enPassant(fromX, fromY, toX, toY);
    } 
    else if (board[fromX][fromY].move(toX, toY))
    {
      if (board[toX][toY] != null) {
        if(board[toX][toY].color == board[fromX][fromY].color) {
          System.out.println("this position is not empty.");
          return false;
        } else {
          view.displayMessage("a "+ board[toX][toY].color + " " + board[toX][toY].type + " has been eaten");
        }
      }

      if (stillechec(fromX, fromY, toX, toY)){
        return false;
      }
      movePiece(fromX, fromY, toX, toY);

      // Check if "en passant" is possible
      if(board[toX][toY].type == PieceType.PAWN && Math.abs(toY - fromY) == 2) {
        pieceEnPassant = new Coord(toX, toY);
      } else {
        pieceEnPassant = new Coord(-1, -1);
      }

      // Promotion
      if(checkPromotion(toX, toY)) {

        System.out.println("promotion");
        promotion(toX, toY);

      }

    } else {
      System.out.println("this piece can't move like this, learn how to play!!!!!");
      return false;
    }

    if(board[toX][toY].isFirstMove()) {
      board[toX][toY].makeFirstMove();
    }

    toggleTurn();
    echecCheck();
    return true;
  }

  @Override
  public void newGame() {
    end = false;
    board = new ChessPiece[8][8];
    ///TODO: optimiser placement pieces

    colorTurn = PlayerColor.WHITE;
    pieceEnPassant = new Coord(-1, -1);
    
    // Placement de départ
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 0, 0);
    new Rook(PlayerColor.WHITE, 0, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 1, 0);
    new Knight(PlayerColor.WHITE, 1, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 2, 0);
    new Bishop(PlayerColor.WHITE, 2, 0);
    view.putPiece(PieceType.QUEEN, PlayerColor.WHITE, 3, 0);
    new Queen(PlayerColor.WHITE, 3, 0);
    view.putPiece(PieceType.KING, PlayerColor.WHITE, 4, 0);
    whiteKing = new Coord(4, 0);
    new King(PlayerColor.WHITE, 4, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 5, 0);
    new Bishop(PlayerColor.WHITE, 5, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 6, 0);
    new Knight(PlayerColor.WHITE, 6, 0);
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 7, 0);
    new Rook(PlayerColor.WHITE, 7, 0);

    for(int i = 0; i < 8; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.WHITE, i, 1);
      new Pawn(PlayerColor.WHITE, i, 1);
    }

    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 0, 7);
    new Rook(PlayerColor.BLACK, 0, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 1, 7);
    new Knight(PlayerColor.BLACK, 1, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 2, 7);
    new Bishop(PlayerColor.BLACK, 2, 7);
    view.putPiece(PieceType.QUEEN, PlayerColor.BLACK, 3, 7);
    new Queen(PlayerColor.BLACK, 3, 7);
    view.putPiece(PieceType.KING, PlayerColor.BLACK, 4, 7);
    blackKing = new Coord(4, 7);
    new King(PlayerColor.BLACK, 4, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 5, 7);
    new Bishop(PlayerColor.BLACK, 5, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 6, 7);
    new Knight(PlayerColor.BLACK, 6, 7);
    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 7, 7);
    new Rook(PlayerColor.BLACK, 7, 7);

    for(int i = 0; i < 8; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.BLACK, i, 6);
      new Pawn(PlayerColor.BLACK, i, 6);
    }

  }
}
