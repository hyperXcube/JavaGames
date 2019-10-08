import java.util.*;

public class mmind {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        Guess guesses[] = new Guess[N];
        for (int i = 0; i < N; i++) {
            guesses[i] = new Guess(sc.nextInt(), sc.nextInt(), sc.nextInt());
        }
        boolean printed = false;
        for (int i = 1000; i <= 9999; i++) {
            boolean works = true;
            for (int j = 0; j < N; j++) {
                String temp1 = Integer.toString(guesses[j].guess);
                String temp2 = Integer.toString(i);
                int corlocCount = 0;
                for (int k = 0; k < 4; k++) {
                    if (temp1.charAt(k) == temp2.charAt(k)) {
                        corlocCount++;
                        if (k == 3) {
                            temp1 = temp1.substring(0, k) + " ";
                            temp2 = temp2.substring(0, k) + " ";
                        }
                        else {
                            temp1 = temp1.substring(0, k) + " " + temp1.substring(k+1);
                            temp2 = temp2.substring(0, k) + " " + temp2.substring(k+1);
                        }
                    }
                }
                if (corlocCount != guesses[j].corloc) works = false;
                int wrolocCount = 0;
                for (int k = 0; k < 4; k++) {
                    if (temp2.charAt(k) != ' ') {
                        for (int l = 0; l < 4; l++) {
                            if (temp2.charAt(k) == temp1.charAt(l) && l != k && temp1.charAt(k) != temp2.charAt(k)) {
                                wrolocCount++;
                            }
                        }
                    }
                }
                if (wrolocCount != guesses[j].wroloc) works = false;
            }
            if (works) {
                System.out.println(i);
                printed = true;
                break;
            }
        }
        if (!printed) System.out.println("NONE");
    }
}

class Guess {
    int guess, corloc, wroloc;
    Guess(int a, int b, int c) {
        guess = a;
        corloc = b;
        wroloc = c;
    }
}
