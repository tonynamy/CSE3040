import java.util.Arrays;

public class Lotto_20211533 {
    public static void main(String[] args) {

        int[] winning_numbers = { 7, 18, 32, 37, 44 };

        int[] generated_numbers_set = new int[50];
        int[] generated_numbers = new int[5];

        int iter = 0;
        int match = -1;

        do {

            match = 0;
            iter++;

            Arrays.fill(generated_numbers_set, 0);

            for (int i = 0; i < 5; i++) {
                int rand_num;

                do {
                    rand_num = (int) (Math.random() * 50);
                } while (generated_numbers_set[rand_num] > 0);

                generated_numbers[i] = rand_num;
                generated_numbers_set[rand_num]++;
            }

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (generated_numbers[i] == winning_numbers[j]) {
                        match++;
                    }
                }
            }

        } while (match < 3);

        System.out.print("Final random numbers : ");
        for (int i = 0; i < 5; i++) {
            System.out.print(generated_numbers[i] + " ");
        }
        System.out.println("\nNumber of iterations : " + iter);

    }
}