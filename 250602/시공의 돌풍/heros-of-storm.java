import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int N, M, T;
    static int[][] map, tmpMap;
    static int robot;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        tmpMap = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if(map[i][j] == -1){
                    if(robot == 0){
                        robot = i;
                    }
                }
            }
        }

        for (int i = 0; i < T; i++) {
            spread();
            clean();
        }

        int ans = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                ans += map[i][j];
            }
        }
        System.out.println(ans - map[robot][0] - map[robot+1][0]);

    }

    public static void spread(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                tmpMap[i][j] = 0;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if((i == robot && j == 0) || (i == robot + 1 && j== 0)) continue;
                int able = 0;

                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];

                    if(nx < 0 || ny < 0 || nx >= N || ny >= M){
                        continue;
                    }
                    if((nx == robot && ny == 0) || (nx == robot + 1 && ny == 0)) continue;
                    tmpMap[nx][ny] += map[i][j] / 5;
                    able++;
                }
                tmpMap[i][j] -= able * (map[i][j] / 5);
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                map[i][j] += tmpMap[i][j];
            }
        }
    }

    public static void clean() {
        int up = robot;
        int down = robot + 1;
        for (int i = up - 1; i > 0; i--) {
            map[i][0] = map[i - 1][0];
        }
        for (int i = 0; i < M - 1; i++) {
            map[0][i] = map[0][i + 1];
        }
        for (int i = 0; i < up; i++) {
            map[i][M - 1] = map[i + 1][M - 1];
        }
        for (int i = M - 1; i > 1; i--) {
            map[up][i] = map[up][i - 1];
        }
        map[up][1] = 0;

        for (int i = down + 1; i < N - 1; i++) {
            map[i][0] = map[i + 1][0];
        }
        for (int i = 0; i < M - 1; i++) {
            map[N - 1][i] = map[N - 1][i + 1];
        }
        for (int i = N - 1; i > down; i--) {
            map[i][M - 1] = map[i - 1][M - 1];
        }
        for (int i = M - 1; i > 1; i--) {
            map[down][i] = map[down][i - 1];
        }
        map[down][1] = 0;
    }
}
