import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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

    static class Bomb implements Comparable<Bomb> {
        int cnt, redCnt, x, y;
        ArrayList<Point> where;

        public Bomb(int cnt, int redCnt, int x, int y, ArrayList<Point> where) {
            this.cnt = cnt;
            this.redCnt = redCnt;
            this.x = x;
            this.y = y;
            this.where = where;
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

        map = new int[N][N];
        tmpMap = new int[N][N];
        visited = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
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
                    int cnt = 0, redCnt = 0, x = -1, y = N;
                    ArrayList<Point> where = new ArrayList<>();
                    where.add(new Point(i, j));
                    q.offer(new Point(i, j));
                    visited[i][j] = true;

                    while (!q.isEmpty()) {
                        Point now = q.poll();
                        cnt++;
                        if (map[now.x][now.y] == 0)
                            redCnt++;
                        else {
                            x = Math.max(x, now.x);
                            y = Math.min(y, now.y);
                        }

                        for (int k = 0; k < 4; k++) {
                            int nx = now.x + dx[k];
                            int ny = now.y + dy[k];
                            if (0 > nx || nx >= N || 0 > ny || ny >= N || visited[nx][ny])
                                continue;
                            if (map[nx][ny] == color) {
                                visited[nx][ny] = true;
                                q.offer(new Point(nx, ny));
                                where.add(new Point(nx, ny));
                            }
                            if(map[nx][ny] == 0) {
                                q.offer(new Point(nx, ny));
                                where.add(new Point(nx, ny));
                            }
                        }
                    }
                    if(cnt + redCnt >= 2) {
                    	bombs.add(new Bomb(cnt, redCnt, x, y, where));
                    }
                }
            }
        }
        if (bombs.size() == 0) {
            return 0;
        }

        Bomb choice = bombs.poll();
        
        for (Point p : choice.where) {
            map[p.x][p.y] = -2;
        }
        return choice.cnt * choice.cnt;
    }

    public static void gravity() {
        for (int j = 0; j < N; j++) {
            int i = N - 2;
            while (i >= 0) {
                if (map[i + 1][j] == -2 && map[i][j] > 0) {
                    int low = i;
                    while(low + 1 < N) {
                        if (map[low + 1][j] == -2 && map[low][j] > 0) {
                            map[low + 1][j] = map[low][j];
                            map[low][j] = -2;
                            low += 1;
                        }
                        else {
                        	break;
                        }
                    }
                }
                i -= 1;
            }
        }
    }
    
    public static void rotate() { 
    	for(int i = 0; i <N; i++) {
        	for(int j = 0; j < N; j++) {
        		tmpMap[i][j] = 0;
        	}
        }
    	
    	for(int i = 0; i < N; i++) {
    		for(int j = 0; j < N; j++) {
    			tmpMap[i][j] = map[j][N-1-i];
    		}
    	}
    	
    	for(int i = 0; i < N; i++) {
    		for(int j = 0; j < N; j++) {
    			map[i][j] = tmpMap[i][j];
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
        }
        System.out.println(ans);
    }

}