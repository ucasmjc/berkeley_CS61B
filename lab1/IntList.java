public class IntList {
        public static void fillGrid(int[] LL,int[] UR,int[][] SS) {
            int N = SS.length;
            int kl = 0;
            int kr = 0;
            int i;
            for (i = 0; i < N-1;i += 1){

                for (int j = i; j < N-1; j++) {
                    SS[i][j + 1] = UR[kr];
                    kr++;
                }
                for (int j = 0; j < i + 1; j++) {
                    SS[i + 1][j] = LL[kl];
                    kl++;
                }

            }
        }

        public static void main(String[] args) {
            int[] LL = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0 };
            int[] UR = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
            int[][] S = {
                    { 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0},
                    { 0, 0, 0, 0, 0}
            };
            fillGrid(LL, UR, S);
            System.out.println(S[0][0]);

        }
    }


