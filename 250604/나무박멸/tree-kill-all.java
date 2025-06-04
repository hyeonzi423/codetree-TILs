import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

    static int N, M, K, C;
    static int[][] map, tmpMap, remain;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static int[] xdx = {-1, 1, 1, -1};
    static int[] xdy = {1, 1, -1, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        tmpMap = new int[N][N];
        remain = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int ans = 0;
        for(int i = 0; i < M; i++){
            grow();
            spread();
            int[] ret = choice();
            ans += ret[0];
            medicine(ret[1], ret[2]);
            recover();
        }
        System.out.println(ans);
    }

    public static void initMap(int[][] map) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tmpMap[i][j] = 0;
            }
        }
    }

    public static void grow() {
        initMap(tmpMap);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] <= 0) {
                    continue;
                }

                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if (nx < 0 || ny < 0 || nx >= N || ny >= N) {
                        continue;
                    }
                    if (map[nx][ny] > 0) {
                        tmpMap[nx][ny]++;
                    }
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] += tmpMap[i][j];
            }
        }
    }

    public static void spread() {
        initMap(tmpMap);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] <= 0) {
                    continue;
                }
                ArrayList<int[]> points = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if (nx < 0 || ny < 0 || nx >= N || ny >= N) {
                        continue;
                    }
                    if(remain[nx][ny] < 0) {
                        continue;
                    }
                    if (map[nx][ny] == 0) {
                        points.add(new int[]{nx, ny});
                    }
                }
                for (int[] point : points) {
                    tmpMap[point[0]][point[1]] += map[i][j] / points.size();
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                map[i][j] += tmpMap[i][j];
            }
        }
    }

    public static int[] choice() {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(b[0], a[0]);
            else if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[2], b[2]);
        });

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] > 0) {
                    int tmpCnt = map[i][j];
                    for (int k = 0; k < 4; k++) {
                        for (int q = 1; q < N; q++) {
                            int nx = i + q * xdx[k];
                            int ny = j + q * xdy[k];
                            if (nx < 0 || ny < 0 || nx >= N || ny >= N) {
                                break;
                            }
                            if (map[nx][ny] <= 0) {
                                break;
                            }
                            tmpCnt += map[nx][ny];
                        }
                    }
                    pq.add(new int[]{tmpCnt, i, j});
                }
            }
        }
        return pq.isEmpty() ? null : pq.poll();
    }

    public static void medicine(int x, int y) {
        map[x][y] = 0;
        remain[x][y] = -1 -C;

        for (int k = 0; k < 4; k++) {
            for (int q = 1; q < N; q++) {
                int nx = x + q * xdx[k];
                int ny = y + q * xdy[k];
                if (nx < 0 || ny < 0 || nx >= N || ny >= N) {
                    break;
                } else if (map[nx][ny] < 0) {
                    break;
                } else if (map[nx][ny] == 0) {
                    remain[nx][ny] = -1 - C;
                    map[nx][ny] = 0;
                    break;
                } else if (map[nx][ny] > 0) {
                    remain[nx][ny] = -1 - C;
                    map[nx][ny] = 0;
                }
            }
        }
    }

    public static void recover() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (remain[i][j] < 0) {
                    remain[i][j]++;
                }
            }
        }
    }
}
