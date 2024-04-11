import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M, ans = 0;
    static int[][] map;
    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, 1, 0, -1 };
    static Dice dice;

    static class Dice {
        int x, y, d, u, f, r;

        public Dice(int x, int y, int d, int u, int f, int r) {
            this.x = x;
            this.y = y;
            this.d = d;
            this.u = u;
            this.f = f;
            this.r = r;
        }

        @Override
        public String toString() {
            return "Dice [x=" + x + ", y=" + y + ", d=" + d + ", u=" + u + ", f=" + f + ", r=" + r + "]";
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        dice = new Dice(0, 0, 1, 1, 2, 3);
        
        for(int i = 0; i < M; i++) {
            moveDice();
            bfs();
        }
        System.out.println(ans);
    }
    public static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
    
    public static void moveDice() {
        int x = dice.x;
        int y = dice.y;
        int d = dice.d;
        
        int nx = x + dx[d];
        int ny = y + dy[d];
        if(!inRange(nx, ny)) { // 범위 밖이면 반대로 이동
            d = (d + 2)%4;
            nx = x + dx[d];
            ny = y + dy[d];
        }
        
        int u = dice.u;
        int f = dice.f;
        int r = dice.r;
        int tmp;
        
        if(d == 0) {
            tmp = u;
            u = f;
            f = 7-tmp;
        }else if(d == 1) {
            tmp = r;
            r = u;
            u = 7-tmp;
        }else if(d == 2) {
            tmp = f;
            f = u;
            u = 7-tmp;
        }else {
            tmp = u;
            u = r;
            r = 7-tmp;
        }
        dice = new Dice(nx, ny, d, u, f, r);
    }
    
    public static void bfs() {
        Queue<Point> q = new LinkedList<>();
        q.add(new Point(dice.x, dice.y));
        boolean[][] visited = new boolean[N][N];
        visited[dice.x][dice.y] = true;
        int bottom = map[dice.x][dice.y];
        ans += bottom;
        
        while(!q.isEmpty()) {
            Point now = q.poll();
            for(int i = 0; i < 4; i++) {
                int nx = now.x + dx[i];
                int ny = now.y + dy[i];
                if(!inRange(nx, ny) || visited[nx][ny]) continue;
                if(map[nx][ny] != bottom) continue;
                q.add(new Point(nx, ny));
                visited[nx][ny] = true;
                ans += bottom;
            }
        }
        
        if(bottom == 7 - dice.u) return;
        else if(bottom < 7 - dice.u) {
            dice.d = (dice.d+1)%4;
        }else {
            dice.d = (dice.d-1 + 4)%4;
        }
    }
}