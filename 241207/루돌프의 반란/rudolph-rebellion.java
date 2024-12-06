import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, M, P, C, D, turn;
	static Point rudolf;
	static Santa[] santa;
	static int[][] map;
	static int[] score;
	static int[] rdx = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] rdy = { 0, 1, 1, 1, 0, -1, -1, -1 };
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	static class Santa {
		int idx, x, y, l, f; // 인덱스, 좌표, 살아있는지, 기절인지

		public Santa(int idx, int x, int y, int l, int f) {
			this.idx = idx;
			this.x = x;
			this.y = y;
			this.l = l;
			this.f = f;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); // 게임판
		M = Integer.parseInt(st.nextToken()); // 턴
		P = Integer.parseInt(st.nextToken()); // 산타
		C = Integer.parseInt(st.nextToken()); // 루돌프 힘
		D = Integer.parseInt(st.nextToken()); // 산타 힘

		st = new StringTokenizer(br.readLine());
		int x = Integer.parseInt(st.nextToken()) - 1;
		int y = Integer.parseInt(st.nextToken()) - 1;
		rudolf = new Point(x, y);

		santa = new Santa[P + 1];
		map = new int[N][N];
		score = new int[P + 1];
		for (int i = 1; i <= P; i++) {
			st = new StringTokenizer(br.readLine());
			int idx = Integer.parseInt(st.nextToken());
			x = Integer.parseInt(st.nextToken()) - 1;
			y = Integer.parseInt(st.nextToken()) - 1;
			santa[idx] = new Santa(idx, x, y, 1, 0); // 1 살아있음, 0 죽음
			map[x][y] = idx;
		}

		for (turn = 0; turn < M; turn++) {
			rodolfMove();
			santaMove();
			awake();
			for (int i = 1; i <= P; i++) {
				if(santa[i].l == 1) {
					score[i] += 1;
				}
			}
		}

		for (int i = 1; i <= P; i++) {
			System.out.print(score[i] + " ");
		}
	}

	public static void rodolfMove() {
		int min = Integer.MAX_VALUE;
		int idx = -1, row = -1, col = -1;

		for (Santa s : santa) {
			if (s == null || s.l == 0)
				continue;

			int dis = (int) (Math.pow((rudolf.x - s.x), 2) + Math.pow((rudolf.y - s.y), 2));
			if (min > dis || (min == dis && row < s.x) || (min == dis && row == s.x && col < s.y)) {
				idx = s.idx;
				min = dis;
				row = s.x;
				col = s.y;
			}
		}

		min = Integer.MAX_VALUE;
		int dir = -1;
		Santa s = santa[idx];
		for (int i = 0; i < 8; i++) {
			int nx = rudolf.x + rdx[i];
			int ny = rudolf.y + rdy[i];
			if (!isRange(nx, ny))
				continue;
			int dis = (int) (Math.pow((nx - s.x), 2) + Math.pow((ny - s.y), 2));
			if (min > dis) {
				min = dis;
				dir = i;
			}
		}

		int newX = rudolf.x + rdx[dir];
		int newY = rudolf.y + rdy[dir];
		rudolf.x = newX;
		rudolf.y = newY;

		if (map[rudolf.x][rudolf.y] != 0) {
			rudolfCrash(dir);
		}
	}

	public static void rudolfCrash(int dir) {
		int pushedSanta = map[rudolf.x][rudolf.y];
		score[pushedSanta] += C;

		int nx = rudolf.x + C * rdx[dir];
		int ny = rudolf.y + C * rdy[dir];

		map[rudolf.x][rudolf.y] = 0;
		santa[pushedSanta].f = 1;
		if (!isRange(nx, ny)) {
			santa[pushedSanta].l = 0;
		} else {
			if (map[nx][ny] != 0) {
				interaction(nx, ny, dir, pushedSanta, true);
			}
			santa[pushedSanta].x = nx;
			santa[pushedSanta].y = ny;
			map[nx][ny] = pushedSanta;
		}
	}

	public static void interaction(int x, int y, int dir, int dix, boolean flag) {
		int pushedSanta = map[x][y];
		map[x][y] = dix;
		int nx = -1, ny = -1;
		if (flag) {
			nx = x + rdx[dir];
			ny = y + rdy[dir];
		} else {
			nx = x + dx[dir];
			ny = y + dy[dir];
		}
		if (!isRange(nx, ny)) {
			santa[pushedSanta].l = 0;
		} else if (map[nx][ny] == 0) {
			map[nx][ny] = pushedSanta;
			santa[pushedSanta].x = nx;
			santa[pushedSanta].y = ny;
		} else if (map[nx][ny] != 0) {
			interaction(nx, ny, dir, pushedSanta, flag);
		}
	}

	public static void santaMove() {
		for (Santa s : santa) {
			if (s == null || s.l == 0 || s.f != 0)
				continue;

			int nowDis = (int) (Math.pow((rudolf.x - s.x), 2) + Math.pow((rudolf.y - s.y), 2));
			int dir = -1;

			for (int i = 0; i < 4; i++) {
				int nx = s.x + dx[i];
				int ny = s.y + dy[i];
				if (!isRange(nx, ny))
					continue;
				if (map[nx][ny] != 0)
					continue;
				int tmpdis = (int) (Math.pow((nx - rudolf.x), 2) + Math.pow((ny - rudolf.y), 2));
				if (nowDis > tmpdis) {
					dir = i;
					nowDis = tmpdis;
				}
			}

			if (dir == -1)
				continue;

			int nx = s.x + dx[dir];
			int ny = s.y + dy[dir];
			map[s.x][s.y] = 0;

			if (nx == rudolf.x && ny == rudolf.y) {
				score[s.idx] += D;
				dir = (dir + 2) % 4;
				nx = rudolf.x + D * dx[dir];
				ny = rudolf.y + D * dy[dir];
				if (!isRange(nx, ny)) {
					santa[s.idx].l = 0;
				} else {
					if (map[nx][ny] == 0) {
						map[nx][ny] = s.idx;
					} else {
						interaction(nx, ny, dir, s.idx, false);
					}
					santa[s.idx] = new Santa(s.idx, nx, ny, 1, 1);
				}
			} else {
				map[nx][ny] = s.idx;
				santa[s.idx] = new Santa(s.idx, nx, ny, 1, 0);
			}
		}
	}

	public static void awake() {
		for (Santa s : santa) {
			if (s == null || s.l == 0)
				continue;
			if(s.f == 2) {
				s.f = 0;
			}else if(s.f == 1) {
				s.f += 1;
			}
		}
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}