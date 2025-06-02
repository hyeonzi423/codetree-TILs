import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    static int N, M, T;
    static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
    static ArrayList<Atom>[][] map, tmpMap;

    static class Atom {
        int m, s, d; // 질량, 속력, 방향

        public Atom(int m, int s, int d) {
            this.m = m;
            this.s = s;
            this.d = d;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 격자의 크기
        M = Integer.parseInt(st.nextToken()); // 원자의 개수
        T = Integer.parseInt(st.nextToken()); // 실험 시간

        map = new ArrayList[N][N];
        tmpMap = new ArrayList[N][N];

        init(map);
        init(tmpMap);

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            map[x][y].add(new Atom(m, s, d));
        }

        for (int i = 0; i < T; i++) {
            move();
            plus();
        }

        int ans = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (Atom a : map[i][j]) {
                    ans += a.m;
                }
            }
        }
        System.out.println(ans);
    }

    public static void move() {
        init(tmpMap);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j].isEmpty()) continue;
                for (Atom a : map[i][j]) {
                    int nx = (i + a.s * dx[a.d] + N * N) % N;
                    int ny = (j + a.s * dy[a.d] + N * N) % N;
                    tmpMap[nx][ny].add(new Atom(a.m, a.s, a.d));
                }
            }
        }
    }

    public static void init(ArrayList<Atom>[][] map) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] = new ArrayList<>();
            }
        }
    }

    public static void plus() {
        init(map);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tmpMap[i][j].isEmpty()) continue;
                if (tmpMap[i][j].size() == 1) {
                    map[i][j].add(tmpMap[i][j].get(0));
                    continue;
                }
                int sumM = 0, sumS = 0;
                int even = 0, odd = 0;
                for (Atom a : tmpMap[i][j]) {
                    if (a.d % 2 == 0) {
                        even++;
                    } else {
                        odd++;
                    }
                    sumM += a.m;
                    sumS += a.s;
                }
                if (sumM < 5) continue;

                int newM = sumM / 5;
                int newS = sumS / tmpMap[i][j].size();
                boolean allEven = (odd == 0);
                boolean allOdd = (even == 0);

                int[] dirs = (allEven || allOdd) ? new int[]{0, 2, 4, 6} : new int[]{1, 3, 5, 7};
                for (int d : dirs) {
                    map[i][j].add(new Atom(newM, newS, d));
                }
            }
        }
    }
}
