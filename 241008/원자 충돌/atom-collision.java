import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

	static int N, M, K;
	static ArrayList<Atom>[][] map, tmp;
	static int[] dx = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dy = { 0, 1, 1, 1, 0, -1, -1, -1 };

	static class Atom {
		int m, s, d;

		public Atom(int m, int s, int d) {
			this.m = m;
			this.s = s;
			this.d = d;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		map = new ArrayList[N][N];
		tmp = new ArrayList[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = new ArrayList<>();
				tmp[i][j] = new ArrayList<>();
			}
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			map[x - 1][y - 1].add(new Atom(m, s, d));
		}

		for (int i = 0; i < K; i++) {
			move();
			event();
		}

		int ans = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (Atom a : map[i][j]) {
					ans += a.m;
				}
			}
		}
		System.out.println(ans);
	}

	public static void move() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (Atom a : map[i][j]) {
					int nx = (i + a.s * dx[a.d] + N * N) % N;
					int ny = (j + a.s * dy[a.d] + N * N) % N;
					tmp[nx][ny].add(a);
				}
			}
		}
	}

	public static void event() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tmp[i][j].size() > 1) {
					int wHap = 0, sHap = 0;
					int cnt = tmp[i][j].size();
					boolean flag = tmp[i][j].get(0).d % 2 == 0 ? true : false;
					boolean same = true;
					for (Atom a : tmp[i][j]) {
						wHap += a.m;
						sHap += a.s;
						boolean tmp = a.d % 2 == 0 ? true : false;
						if (tmp != flag) {
							same = false;
						}
					}
					tmp[i][j].clear();
					int start = same ? 0 : 1;
					if (wHap / cnt == 0)
						continue;
					for (int k = 0; k < 4; k++) {
						tmp[i][j].add(new Atom(wHap / 5, sHap / cnt, start + 2 * k));
					}
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j].clear();
				for (Atom a : tmp[i][j]) {
					map[i][j].add(a);
				}
				tmp[i][j].clear();
			}
		}
	}
}