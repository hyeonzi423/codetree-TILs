import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int N, M, K, turn;
	static int[] dx = { 0, 1, -1, 0 };
	static int[] dy = { 1, 0, 0, -1 };
	static int[] hx = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] hy = { 0, 1, 1, 1, 0, -1, -1, -1 };
	static int[][] map, record, attackRecord;
	static Candidate Attacker, Victim;

	public static class Candidate implements Comparable<Candidate> {
		int x, y, p, r;

		public Candidate(int x, int y, int p, int r) {
			this.x = x;
			this.y = y;
			this.p = p;
			this.r = r;
		}

		@Override
		public int compareTo(Candidate o) {
			if (this.p != o.p)
				return Integer.compare(this.p, o.p);
			if (this.r != o.r)
				return Integer.compare(o.r, this.r);
			if ((this.x + this.y) != (o.x + o.y))
				return Integer.compare((o.x + o.y), (this.x + this.y));
			return Integer.compare(o.y, this.y);
		}
	}

	public static class Point {
		int x, y, t;

		public Point(int x, int y, int t) {
			this.x = x;
			this.y = y;
			this.t = t;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		map = new int[N][M];
		record = new int[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (turn = 0; turn < K; turn++) {
			choice();
			laser();
			build();
		}
		find();
	}

	public static void choice() {
		ArrayList<Candidate> remain = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] > 0) {
					remain.add(new Candidate(i, j, map[i][j], record[i][j]));
				}
			}
		}
		attackRecord = new int[N][M];
		Collections.sort(remain);
		Attacker = remain.get(0);
		Victim = remain.get(remain.size() - 1);
		map[Attacker.x][Attacker.y] += N + M;
		record[Attacker.x][Attacker.y] = turn+1;
	}

	public static void laser() {
		boolean flag = false;
		Point[][] track = new Point[N][M];
		Queue<Point> q = new LinkedList<>();
		q.add(new Point(Attacker.x, Attacker.y, 0));

		while (!q.isEmpty()) {
			Point now = q.poll();
			if (now.x == Victim.x && now.y == Victim.y) {
				flag = true;
			}
			for (int i = 0; i < 4; i++) {
				int nx = (now.x + dx[i] + N) % N;
				int ny = (now.y + dy[i] + M) % M;
				if(map[nx][ny] <= 0) continue;
				if (track[nx][ny] == null || (track[nx][ny] != null && track[nx][ny].t > now.t + 1)) {
					track[nx][ny] = new Point(now.x, now.y, now.t + 1);
					q.add(new Point(nx, ny, now.t + 1));
				}
			}
		}
		if (flag) {
			int power = map[Attacker.x][Attacker.y];
			int sx = Victim.x;
			int sy = Victim.y;
			map[sx][sy] -= power;

			while (true) {
				int nx = track[sx][sy].x;
				int ny = track[sx][sy].y;
				if (nx == Attacker.x && ny == Attacker.y)
					break;
				map[nx][ny] -= power / 2;
				attackRecord[nx][ny] = 1;
				sx = nx;
				sy = ny;
			}
		} else {
			bomb();
		}
	}

	public static void bomb() {
		int power = map[Attacker.x][Attacker.y];
		map[Victim.x][Victim.y] -= power;
		for (int i = 0; i < 8; i++) {
			int nx = (Victim.x + hx[i] + N) % N;
			int ny = (Victim.y + hy[i] + M) % M;
			if(nx == Attacker.x && ny == Attacker.y) continue;
			if(nx == Victim.x && ny == Victim.y) continue;
			map[nx][ny] -= power / 2;
			attackRecord[nx][ny] = 1;
		}
	}

	public static void build() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (i == Attacker.x && j == Attacker.y)
					continue;
				if (i == Victim.x && j == Victim.y)
					continue;
				if (map[i][j] > 0 && attackRecord[i][j] == 0) {
					map[i][j]++;
				}
				if(map[i][j] < 0) map[i][j] = 0;
			}
		}
	}

	public static void find() {
		int max = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				max = Math.max(max, map[i][j]);
			}
		}
		System.out.println(max);
	}

	public static boolean isRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < M;
	}
}