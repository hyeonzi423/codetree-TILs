import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Main {
	static int N, M, K; // 격자 크기, 플레이어의 수, 라운드의 수
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static ArrayList<Integer>[][] gun;
	static Player[][] map;
	static Player[] players;
	static int[] score;
	
	static class Player{
		int x, y, d, s, g, num;

		public Player(int x, int y, int d, int s, int g, int num) {
			this.x = x;
			this.y = y;
			this.d = d;
			this.s = s; // 초기 능력치
			this.g = g;
			this.num = num;
		}

		@Override
		public String toString() {
			return "Player [x=" + x + ", y=" + y + ", d=" + d + ", s=" + s + ", g=" + g + ", num=" + num + "]";
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		score = new int[M];
		players = new Player[M];
		map = new Player[N][N];
		gun = new ArrayList[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				gun[i][j] = new ArrayList<>();
			}
		}
		
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				int tmp = Integer.parseInt(st.nextToken());
				if(tmp > 0) {
					gun[i][j].add(tmp);
				}
			}
		}
		
		for(int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			
			players[i] = new Player(x-1, y-1, d, s, 0, i);
			map[x-1][y-1] = players[i];
		}
		
		for(int i = 0; i < K; i++) {
			move();
//			for(Player pl : players) {
//				System.out.println(pl);
//			}
		}
		
		for(Integer s : score) {
			System.out.print(s + " ");
		}
	}

	private static void move() {
		for(int i = 0; i < M; i++) {
			Player p = players[i];
			int nx = p.x + dx[p.d];
			int ny = p.y + dy[p.d];
			if(!inRange(nx, ny)) { // 범위 밖이면 반대로 
				p.d = (p.d + 2)%4;
				nx = p.x + dx[p.d];
				ny = p.y + dy[p.d];
			}
			if(map[nx][ny] == null) { // 이동한 공간에 플레이어가 없다면 총 확인하고 줍기
				int gMax = choiceGun(nx, ny, p.g);
				players[i] = new Player(nx, ny, p.d, p.s, gMax, p.num);
				map[p.x][p.y] = null;
				map[nx][ny] = players[i];
				
			}else { // 플레이어가 있다면 싸움
				Player player1 = map[nx][ny]; // 먼저 온 플레이어
				Player player2 = new Player(nx, ny, p.d, p.s, p.g, p.num);
				map[p.x][p.y] = null;
				
				int s1 = player1.s + player1.g;
				int s2 = player2.s + player2.g;
				
				if(s1 < s2 || ((s1 == s2)&&(player1.s < player2.s))) {
					score[player2.num] += s2 - s1; // 포인트 획득
					gun[nx][ny].add(player1.g); // 진 플레이어는 총 내려 놓기
					player1.g = 0;
					int gMax = choiceGun(nx, ny, player2.g); // 이긴 플레이어가 총 새로 고르기
					if(gMax != player2.g) { // 새로 고른 총이 원래 총과 다르다면 원래 총 내려 놓기
						gun[nx][ny].add(player2.g);
						player2.g = gMax;
					}
					player1 = loser_move(player1);
					gMax = choiceGun(player1.x, player1.y, player1.g); // 이긴 플레이어가 총 새로 고르기
					if(gMax != player1.g) { // 새로 고른 총이 원래 총과 다르다면 원래 총 내려 놓기
						player1.g = gMax;
					}
					
					players[player1.num] = player1;
					players[player2.num] = player2;
					
					map[nx][ny] = player2;
					map[player1.x][player1.y] = player1;
					
				}else if(s1 > s2 || ((s1 == s2)&&(player1.s > player2.s))) {
					score[player1.num] += s1 - s2; // 포인트 획득
					gun[nx][ny].add(player2.g); // 진 플레이어는 총 내려 놓기
					player2.g = 0;
					int gMax = choiceGun(nx, ny, player1.g); // 이긴 플레이어가 총 새로 고르기
					if(gMax != player1.g) { // 새로 고른 총이 원래 총과 다르다면 원래 총 내려 놓기
						gun[nx][ny].add(player1.g);
						player1.g = gMax;
					}
					player2 = loser_move(player2);
					gMax = choiceGun(player2.x, player2.y, player2.g); // 이긴 플레이어가 총 새로 고르기
					if(gMax != player2.g) { // 새로 고른 총이 원래 총과 다르다면 원래 총 내려 놓기
						player2.g = gMax;
					}
					
					players[player1.num] = player1;
					players[player2.num] = player2;
					
					map[nx][ny] = player1;
					map[player2.x][player2.y] = player2;
				}
			}
		}
	}
	
	private static boolean inRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
	
	private static Player loser_move(Player p) {
		boolean flag = false;
		int nx = p.x + dx[p.d];
		int ny = p.y + dy[p.d];
		if(!inRange(nx, ny) || map[nx][ny] != null) { 
			for(int i = 0; i < 3; i++) {
				p.d = (p.d + 1)%4;
				nx = p.x + dx[p.d];
				ny = p.y + dy[p.d];
				if(inRange(nx, ny) && map[nx][ny] == null) {
					flag = true;
					break;
				}
			}
		}else {
			flag = true;
		}
		
		if(flag == true) {
			p = new Player(nx, ny, p.d, p.s, p.g, p.num);
		}
		return p;
	}
	
	private static int choiceGun(int x, int y, int pg) {
		int gMax = pg;
		for(Integer g : gun[x][y]) {
			gMax = Math.max(gMax, g);
		}
		return gMax;
	}
	
}