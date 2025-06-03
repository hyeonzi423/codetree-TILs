import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int[][] map;
    static int[][] tmpMap;
    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, 1, 0, -1 };
    static boolean visited[][];
    static ArrayList<Point> red;

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isHigher(Point p) {
            if(this.x != p.x)
                return this.x > p.x;
            return this.y < p.y;
        }
    }

    static class Bomb implements Comparable<Bomb> {
        int cnt, redCnt, x, y;

        public Bomb(int cnt, int redCnt, int x, int y) {
            this.cnt = cnt;
            this.redCnt = redCnt;
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Bomb o) {
            if (this.cnt != o.cnt) {
                return o.cnt - this.cnt;
            }if (this.redCnt != o.redCnt) {
                return this.redCnt - o.redCnt;
            }if (this.x != o.x) {
                return o.x - this.x;
            }
            return this.y - o.y;
        }
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        red = new ArrayList<>();

        map = new int[N][N];
        tmpMap = new int[N][N];
        visited = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if(map[i][j] == 0) {
                    red.add(new Point(i, j));
                }
            }
        }
    }

    public static int select() {
        PriorityQueue<Bomb> bombs = new PriorityQueue<>();

        for(int i = 0; i <N; i++) {
            for(int j = 0; j < N; j++) {
                visited[i][j] = false;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] > 0 && !visited[i][j]) {
                    int color = map[i][j];
                    Queue<Point> q = new LinkedList<>();
                    int cnt = 0, redCnt = 0, x = -1, y = -1;
                    q.offer(new Point(i, j));
                    visited[i][j] = true;

                    for(Point r : red) {
                        visited[r.x][r.y] = false;
                    }

                    while (!q.isEmpty()) {
                        Point now = q.poll();
                        cnt++;
                        if (map[now.x][now.y] == 0)
                            redCnt++;
                        else {
                            if(now.isHigher(new Point(x, y))) {
                                x = now.x;
                                y = now.y;
                            }
                        }

                        for (int k = 0; k < 4; k++) {
                            int nx = now.x + dx[k];
                            int ny = now.y + dy[k];
                            if (0 > nx || nx >= N || 0 > ny || ny >= N || visited[nx][ny])
                                continue;
                            if (map[nx][ny] == color || map[nx][ny] == 0) {
                                visited[nx][ny] = true;
                                q.offer(new Point(nx, ny));
                            }
                        }
                    }
                    if(cnt >= 1) {
                        bombs.add(new Bomb(cnt, redCnt, x, y));
                    }
                }
            }
        }
        if (bombs.size() == 0 || bombs.peek().cnt <= 1) {
            return 0;
        }

        Bomb choice = bombs.poll();
        for(int i = 0; i <N; i++) {
            for(int j = 0; j < N; j++) {
                visited[i][j] = false;
            }
        }

        Queue<Point> q = new LinkedList<>();
        q.offer(new Point(choice.x, choice.y));
        visited[choice.x][choice.y] = true;
        int color = map[choice.x][choice.y];

        while (!q.isEmpty()) {
            Point now = q.poll();
            map[now.x][now.y] = -2;
            for (int k = 0; k < 4; k++) {
                int nx = now.x + dx[k];
                int ny = now.y + dy[k];
                if (0 > nx || nx >= N || 0 > ny || ny >= N || visited[nx][ny])
                    continue;
                if (map[nx][ny] == color || map[nx][ny] == 0) {
                    visited[nx][ny] = true;
                    q.offer(new Point(nx, ny));
                }
            }
        }

        return choice.cnt * choice.cnt;
    }

    public static void gravity() {
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                tmpMap[i][j] = -2;

        for(int j = 0; j < N; j++) {
            int lastIdx = N - 1;
            for(int i = N - 1; i >= 0; i--) {
                if(map[i][j] == -2)
                    continue;
                if(map[i][j] == -1)
                    lastIdx = i;
                tmpMap[lastIdx--][j] = map[i][j];
            }
        }

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                map[i][j] = tmpMap[i][j];
    }

    public static void rotate() {
        for(int i = 0; i <N; i++) {
            for(int j = 0; j < N; j++) {
                tmpMap[i][j] = 0;
            }
        }

        for(int j = N - 1; j >= 0; j--) {
            for(int i = 0; i < N; i++) {
                tmpMap[N - 1 - j][i] = map[i][j];
            }
        }

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                map[i][j] = tmpMap[i][j];
            }
        }
    }

    public static void findRed() {
        red.clear();
        for (int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(map[i][j] == 0) {
                    red.add(new Point(i,j));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        int ans = 0;
        while (true) {
            int ret = select();
            if(ret == 0) {
                break;
            }
            ans += ret;
            gravity();
            rotate();
            gravity();
            findRed();
        }
        System.out.println(ans);
    }

}
