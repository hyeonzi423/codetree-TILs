import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Main {

	static int N, M, K, ans;
	static int[][] map;
	static ArrayList<Integer>[][] virus;
	static int[] dx = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dy = { 0, 1, 1, 1, 0, -1, -1, -1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		ans = 0;

		map = new int[N][N];
		virus = new ArrayList[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				virus[i][j] = new ArrayList<>();
			}
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			int a = Integer.parseInt(st.nextToken());
			virus[x][y].add(a);
		}
		
		for(int i = 0; i < K; i++) {
			eat();
			spread();
		}
		count();
		System.out.println(ans);
	}

	public static void eat() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (virus[i][j].size() == 0)
					continue;

				Collections.sort(virus[i][j]);
				ArrayList<Integer> tmp = new ArrayList<>();
				int dead = 0;

				for (Integer v : virus[i][j]) {
					if (map[i][j] >= v) {
						map[i][j] -= v;
						tmp.add(v + 1);
					} else {
						dead += v;
					}
				}
				virus[i][j] = new ArrayList<>();
				for (Integer t : tmp) {
					virus[i][j].add(t);
				}
				map[i][j] += dead / 2;
			}
		}
	}

	public static void spread() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (Integer v : virus[i][j]) {
					if (v % 5 == 0) {
						for(int k = 0; k < 8; k++) {
							int nx = i + dx[k];
							int ny = j + dy[k];
							if(0 <= nx && nx < N && 0 <= ny && ny < N) {
								virus[nx][ny].add(1);
							}
						}
					}
				}
			}
		}
	}
	
	public static void count() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				ans += virus[i][j].size();
			}
		}
	}
}