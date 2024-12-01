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
	static int[] dx = { -1, 0, 0, 1 };
	static int[] dy = { 0, -1, 1, 0 };
	static int[][] baseCamp; // 못 지나가는 경우 -1로 설정
	static ArrayList<Point> baseCampList;
	static Point[] destination;
	static Person[] people;
	
	public static class Person{
		int x, y, state; // 0: 출발 안 함, 1: 출발 2: 도착

		public Person(int x, int y, int state) {
			this.x = x;
			this.y = y;
			this.state = state;
		}

		@Override
		public String toString() {
			return "Person [x=" + x + ", y=" + y + ", state=" + state + "]";
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
			int x =Integer.parseInt(st.nextToken())-1;
			int y =Integer.parseInt(st.nextToken())-1;
			destination[i] = new Point(x, y);
		}
		
		people = new Person[M];
		while(goal != M) {
			move();
			if(T < M) {
				choice();
			}
			T++;
		}
		System.out.println(T);
	}
	
	public static void move() {
		ArrayList<Point> canNotGo = new ArrayList<>();
		for(int i = 0; i < M; i++) {
			if(people[i] == null || people[i].state == 2) continue;
			int minDis = Integer.MAX_VALUE;
			Point candidate = null;
			for(int j = 0; j < 4; j++) {
				int nx = people[i].x + dx[j];
				int ny = people[i].y + dy[j];
				if(!isRange(nx, ny)) continue;
				if(baseCamp[nx][ny] == -1) continue;
				int tmp = bfs(destination[i], nx, ny);
				if(minDis > tmp) {
					candidate = new Point(nx, ny);
					minDis = tmp;
				}
			}
			
			people[i].x = candidate.x;
			people[i].y = candidate.y;
			if(candidate.x == destination[i].x && candidate.y == destination[i].y) {
				people[i].state = 2;
				canNotGo.add(destination[i]);
				goal++;
			}
		}
		for(Point c : canNotGo) {
			baseCamp[c.x][c.y] = -1;
		}
	}
	
	public static void choice() {
		Point now = destination[T];
		int minDis = Integer.MAX_VALUE;
		Point candidate = null;
		for(Point b : baseCampList) {
			if(baseCamp[b.x][b.y] == -1) {
				continue;
			}
			int tmp = bfs(now, b.x, b.y);
			if(minDis > tmp) {
				candidate = b;
				minDis = tmp;
			}
		}
		baseCamp[candidate.x][candidate.y] = -1;
		people[T] = new Person(candidate.x, candidate.y, 1);
	}
	
	public static int bfs(Point des, int nowX, int nowY) {
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] {nowX, nowY, 0});
		boolean[][] visited = new boolean[N][N];
		visited[nowX][nowY] = true;
		
		while(!q.isEmpty()) {
			int[] now = q.poll();
			if(now[0] == des.x && now[1] == des.y) {
				return now[2];
			}
			for(int i = 0; i < 4; i++) {
				int nx = now[0] + dx[i];
				int ny = now[1] + dy[i];
				if(!isRange(nx, ny)) continue;
				if(visited[nx][ny]) continue;
				if(baseCamp[nx][ny] == -1) continue;
				
				q.add(new int[] {nx, ny, now[2]+1});
				visited[nx][ny] = true;
			}
		}
		return Integer.MAX_VALUE;
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}