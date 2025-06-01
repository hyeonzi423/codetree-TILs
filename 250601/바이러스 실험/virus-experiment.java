import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

    static int N, M, K;
    static PriorityQueue<Integer>[][] map;
    static int[][] food, addValue;
    static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 격자 크기
        M = Integer.parseInt(st.nextToken()); // 바이러스 개수
        K = Integer.parseInt(st.nextToken()); // 사이클 수

        addValue = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                addValue[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        map = new PriorityQueue[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] = new PriorityQueue<>(); // 필요하다면 Comparator도 지정
            }
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int age = Integer.parseInt(st.nextToken());

            map[x][y].add(age);
        }


        food = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                food[i][j] = 5;
            }
        }

        for (int i = 0; i < K; i++) {
            eat();
            spread();
            add();
        }

        System.out.println(survive());
    }

    public static void eat() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!map[i][j].isEmpty()) {
                    int plus = 0;
                    PriorityQueue<Integer> pq = new PriorityQueue<>();

                    while (!map[i][j].isEmpty()) {
                        int now = map[i][j].poll();

                        if (food[i][j] - now > 0) {
                            food[i][j] -= now;
                            pq.add(now + 1);
                        } else {
                            plus += now / 2;
                        }
                    }

                    for (int n : pq) {
                        map[i][j].add(n);
                    }
                    food[i][j] += plus;
                }
            }
        }
    }

    public static void spread() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!map[i][j].isEmpty()) {
                    for (int n : map[i][j]) {
                        if (n % 5 == 0) {
                            for (int k = 0; k < 8; k++) {
                                int nx = i + dx[k];
                                int ny = j + dy[k];
                                if (nx < 0 || ny < 0 || nx >= N || ny >= N) {
                                    continue;
                                }
                                map[nx][ny].add(1);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void add() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                food[i][j] += addValue[i][j];
            }
        }
    }

    public static int survive() {
        int ans = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                ans += map[i][j].size();
            }
        }

        return ans;
    }
}