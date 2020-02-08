public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        for (int i = 0; i < 10; i++) {
            x += i;
            if (x == 45) {
                System.out.println(x);
                break;
            }
            System.out.print(x + " ");
        }
    }
}