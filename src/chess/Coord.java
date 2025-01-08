package chess;

public class Coord {
  public static final int BOARD_SIZE = 8;
  public int x;
  public int y;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null || obj.getClass() != this.getClass())
      return false;

    Coord other = (Coord) obj;

    return this.x == other.x && this.y == other.y;
  }

  @Override
  public int hashCode() {
      int hash = this.x + this.y;
      return hash;
  }
}
