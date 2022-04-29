interface Order
{
    public boolean lessThan(Order other);
}

public class Point implements Order
{
    private double x, y;
    public Point(double newX, double newY)
    {
        x = newX;
        y = newY;
    }

    public static <T> T getLargerPoint(T t1, T t2) {
        if(t1.lessThan(t2)) {
            return t1;
        }
        return t2;
    }

    public boolean lessThan(Order other)
    {
        Point p2 = (Point) other;
        if(this.x < p2.x) {
            return true;
        }
        else if(this.x > p2.x) {
            return false;
        }
        else if(this.x == p2.x) {
            if(this.y < p2.y) {
                return true;
            }
            else if(this.y > p2.y) {
                return false;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args)
    {
        Point p1 = new Point(1, 1);
        Order o1 = new Point(2, 2);
        System.out.println(p1.lessThan(o1));
    }
}

