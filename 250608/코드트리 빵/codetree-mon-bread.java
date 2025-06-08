import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M, T, goal;
    static int[] dx = {-1, 0, 0, 1};
    static int[] dy = {0, -1, 1, 0};
    static int[][] baseCamp; // 못 지나가는 경우 -1로 설정
    static ArrayList<Point> baseCampList;
    static Point[] destination;
    static Person[] people;

    public static class Person {
        int x, y, state; // state -> 0: 출발 안 함, 1: 출발 2: 도착

        public Person(int x, int y, int state) {
            this.x = x;
            this.y = y;
            this.state = state;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken()); // 격자의 크기
        M = Integer.parseInt(st.nextToken()); // 사람의 수
        T = 0;
        goal = 0;

        baseCamp = new int[N][N];
        baseCampList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                baseCamp[i][j] = Integer.parseInt(st.nextToken());
                if (baseCamp[i][j] == 1) {
                    baseCampList.add(new Point(i, j));
                }
            }
        }

        destination = new Point[M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            destination[i] = new Point(x, y);
        }

        people = new Person[M];
        while (goal != M) {
            move();
            if (T < M) {
                choice();
            }
            T++;
        }
        System.out.println(T);
    }

    public static void move() {
        ArrayList<Point> canNotGo = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            if (people[i] == null || people[i].state == 2) continue;

            int min = Integer.MAX_VALUE;
            Point candidate = null;
            for (int j = 0; j < 4; j++) {
                int nx = people[i].x + dx[j];
                int ny = people[i].y + dy[j];

                if (!isRange(nx, ny)) continue;
                if (baseCamp[nx][ny] == -1) continue;
                int tmp = bfs(destination[i], nx, ny);
                if (tmp < min) {
                    candidate = new Point(nx, ny);
                    min = tmp;
                }
            }

            people[i].x = candidate.x;
            people[i].y = candidate.y;
            if (candidate.x == destination[i].x && candidate.y == destination[i].y) {
                people[i].state = 2;
                canNotGo.add(destination[i]);
                goal++;
            }
        }

        for (Point p : canNotGo) {
            baseCamp[p.x][p.y] = -1;
        }
    }

    public static void choice() {
        Point now = destination[T];
        int min = Integer.MAX_VALUE;
        Point candidate = null;
        for (Point b : baseCampList) {
            if (baseCamp[b.x][b.y] == -1) continue;

            int tmp = bfs(now, b.x, b.y);
            if (tmp < min) {
                candidate = b;
                min = tmp;
            }
        }
        baseCamp[candidate.x][candidate.y] = -1;
        people[T] = new Person(candidate.x, candidate.y, 1);
    }

    public static int bfs(Point des, int nowX, int nowY) {
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{nowX, nowY, 0});
        boolean[][] visited = new boolean[N][N];
        visited[nowX][nowY] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == des.x && cur[1] == des.y) {
                return cur[2];
            }
            for (int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i];
                int ny = cur[1] + dy[i];
                if (!isRange(nx, ny)) continue;
                if (visited[nx][ny]) continue;
                if (baseCamp[nx][ny] == -1) continue;

                visited[nx][ny] = true;
                q.add(new int[]{nx, ny, cur[2] + 1});
            }
        }
        return Integer.MAX_VALUE;
    }

    public static boolean isRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}
