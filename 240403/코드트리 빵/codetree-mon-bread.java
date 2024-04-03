import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.Point;
import java.util.StringTokenizer;

public class Main {

	static int N, M, time, arrive; // 격자의 크기, 사람의 수
	static int[][] map;
	static boolean[][] checkMap; // 해당 칸으로 이동할 수 있는지 없는지 확인하는 배열
	static Person[] people; // 현재 사람의 위치
	static Point[] destination; // 목적지
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	static ArrayList<Point> canNotMove;
	static ArrayList<Point> baseCamp; // 베이스캠프 후보군
	
	static class Person{
		int x, y, state; // 0 : 출발 X, 1 : 가는중, 2 : 도착

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
	
	static class Node{
		int x, y, dis; // 0 : 출발 X, 1 : 가는중, 2 : 도착

		public Node(int x, int y, int dis) {
			this.x = x;
			this.y = y;
			this.dis = dis;
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		baseCamp = new ArrayList<>();
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] == 1) {
					baseCamp.add(new Point(i, j));
				}
			}
		}
		people = new Person[M];
		destination = new Point[M];
		checkMap = new boolean[N][N];
		for(int i = 0; i < N; i++) {
			Arrays.fill(checkMap[i], true);
		}
		arrive = 0; // 도착한 사람 수
		
		for(int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			destination[i] = new Point(x-1, y-1);
		}
		
//		for(int i = 0; i < M; i++) {
//			System.out.println(destination[i].x + " " + destination[i].y);
//		}
		time = 0;
		
		while(true) {
			
			time++;
			move();
			if(arrive == M) break;
			//System.out.println();
			if(time < M + 1) {
				choice();
			}
			//System.out.println(time);
//			for(Person p : people) {
//				System.out.println(p);
//			}
			
		}
		System.out.println(time);
	}
	
	public static int bfs(Point p, int idx) {
		Queue<Node> q = new LinkedList<>();
		q.add(new Node(p.x, p.y, 0));
		boolean visited[][] = new boolean[N][N];
		int ex = destination[idx].x, ey = destination[idx].y;
		int dis = -1;
		
		while(!q.isEmpty()) {
			Node now = q.poll();
			visited[now.x][now.y] = true;
			if(now.x == ex && now.y == ey) {
				dis = now.dis;
				break;
			}
			for(int i = 0; i < 4; i++) {
				int nx = now.x + dx[i];
				int ny = now.y + dy[i];
				if(inRange(nx, ny) && !visited[nx][ny] && checkMap[nx][ny]) {
					q.add(new Node(nx, ny, now.dis + 1));
				}
			}
		}
		return dis;
	}
	
	public static boolean inRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
	
	public static void move() { // 1. 격자에 있는 사람들은 모두 편의점 방향으로 1칸 이동
		canNotMove = new ArrayList<>();
		for(int i = 0; i < M; i++) {
			Person now = people[i];
			if(now == null || now.state == 2) continue; // 아직 격자에 올라오지 못했거나 도착했다면
			int minDis = Integer.MAX_VALUE, d = -1;
			for(int k = 0; k < 4; k++) {
				int nx = now.x + dx[k];
				int ny = now.y + dy[k];
				if(!inRange(nx, ny) || !checkMap[nx][ny]) continue;
				int ret = bfs(new Point(nx, ny), i);
				if(ret < minDis) {
					minDis = ret;
					d = k;
				}
			}
			
			int nx = now.x + dx[d];
			int ny = now.y + dy[d];
			// 도착했을 경우부터 구현
			people[i].x = nx;
			people[i].y = ny;
			//System.out.println(nx + " " + destination[i].x + " " + ny + " " + destination[i].y  + " " + i);
			if(nx == destination[i].x && ny == destination[i].y) {
				canNotMove.add(new Point(nx, ny));
				people[i].state = 2;
				arrive++;
				//System.out.println("arrive " + people[i]);
			}
		}
		
		for(Point c : canNotMove) {
			checkMap[c.x][c.y] = false;
		}
	}

	public static void choice() { // 3. 
		for(int i = 0; i < M; i++) {
			if(time >= i+1 && people[i] == null) { // 시간 조건에 맞고 아직 격자 위로 올라오지 않았다면
				int minDis = Integer.MAX_VALUE, minX = N, minY = N;
				for(Point d : baseCamp) {
					if (!checkMap[d.x][d.y]) continue; // 이미 선택된 베이스캠프라면 넘어감
					int ret = bfs(d, i); // 베이스 캠프마다 목적지까지의 이동 거리 구한 뒤 거리 행 열이 작은 베이스 캠프 값 저장 
					//System.out.println(i + " " +ret);
					if(ret < minDis || (ret == minDis && d.x <minX) || (ret == minDis && d.x == minX &&  d.y < minY)) {
						minDis = ret;
						minX = d.x;
						minY = d.y;
					}
				}
				people[i] = new Person(minX, minY, 1); // 선택한 베이스 캠프로 이동
				//System.out.println("ddd " + people[i] );
				checkMap[minX][minY] = false; // 선택한 베이스 캠프는 이동 X
			}
			if(time < i+1) return;
		}
	}
	
}