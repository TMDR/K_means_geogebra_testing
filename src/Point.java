public class Point{
    public double x,y;

    public Point(double X,double Y){
        x = X;
        y = Y;
    }

    @Override
    public String toString() {
        return "{"+x+" , "+y+"}";
    }
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Point.class)
            return false;
        Point point = (Point)obj;
        return x == point.x && y == point.y;
    }
}
