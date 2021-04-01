package WorldModel;

import Constants.Parameters;

import java.util.Objects;

public class Vector2D {

    private final int x;
    private final int y;

    public Vector2D(int x, int y) {
        this.x = Parameters.normalize(x);
        this.y = Parameters.normalize(y);
    }

    public Vector2D add(Vector2D otherVector){
        return new Vector2D(
                this.x + otherVector.x,
                this.y + otherVector.y);
    }

    public Vector2D add(int dx, int dy){
        return new Vector2D(this.x + dx, this.y + dy);
    }

    public static Vector2D randomizeVector(int xConstraint, int yConstraint){
        return new Vector2D(
                (int)(Math.random() * xConstraint),
                (int)(Math.random() * yConstraint));
    }

    public Vector2D copy() {
        return this.add(0, 0);
    }

//    public boolean equals(Vector2D other){
//        return (this.x == other.x && this.y == other.y);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2D)) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x &&
                y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString(){
        return String.format("(x: %d, y: %d)", this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
