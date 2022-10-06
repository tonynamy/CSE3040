public class Getxy_20211533 {

    public static void main(String[] args) {

        for (int x = 1; x <= 9; x++) {
            for (int y = 1; y <= 9; y++) {
                int xxy = x * 100 + x * 10 + y;
                int yy = y * 10 + y;
                int yxx = y * 100 + x * 10 + x;

                if (xxy + yy == yxx) {
                    System.out.println("x=" + x + ", y=" + y + " (" + xxy + "+" + yy + "=" + yxx + ")");
                }
            }
        }

    }
}