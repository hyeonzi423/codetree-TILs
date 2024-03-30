import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	static int N, M, K, C, ans;
	static int[][] tree, tmpTree, herbicide;
	static int dx[] = { -1, 0, 1, 0 };
	static int dy[] = { 0, 1, 0, -1 };
	static int hdx[] = { -1, -1, 1, 1 };
	static int hdy[] = { -1, 1, -1, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());

		tree = new int[N][N];
		herbicide = new int[N][N];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				tree[i][j] = Integer.parseInt(st.nextToken());
				if (tree[i][j] == -1) {
					tree[i][j] = -100;
				}
			}
		}
		ans = 0;
		for(int i = 0; i < M; i++) {
			grow();
			//System.out.println("grow");
			//print();
			spread();
			//System.out.println("spread");
			//print();
			choice();
			//System.out.println("choice");
			//print1();
			remove();
			//print();
			//System.out.println(ans + "!!!!!!!!!!!!!!");
		}
		System.out.println(ans);
	}

	public static boolean inRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}

	public static void grow() {
		tmpTree = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(tree[i][j] == -100) {
					tmpTree[i][j] = -100;
					continue;
				}
				else if (tree[i][j] <= 0) {
					tmpTree[i][j] = tree[i][j];
					continue;
				}
				int cnt = 0;
				for (int k = 0; k < 4; k++) {
					int nx = i + dx[k];
					int ny = j + dy[k];
					if (inRange(nx, ny) && tree[nx][ny] > 0) {
						cnt++;
					}
				}
				tmpTree[i][j] = tree[i][j] + cnt;
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tree[i][j] = tmpTree[i][j];
			}
		}
	}

	public static void spread() {
		tmpTree = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tree[i][j] <= 0) {
					continue;
				}
				else if(tree[i][j] == -100) {
					tmpTree[i][j] = -100;
					continue;
				}
				int cnt = 0;
				for (int k = 0; k < 4; k++) {
					int nx = i + dx[k];
					int ny = j + dy[k];
					if (inRange(nx, ny) && tree[nx][ny] == 0) {
						cnt++;
					}
				}
				if(cnt == 0) continue;
				int t = tree[i][j] / cnt;
				for (int k = 0; k < 4; k++) {
					int nx = i + dx[k];
					int ny = j + dy[k];
					if (inRange(nx, ny) && tree[nx][ny] == 0) {
						tmpTree[nx][ny] += t;
					}
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if(tmpTree[i][j] == -100) continue;
				tree[i][j] += tmpTree[i][j];
			}
		}
	}

	public static void choice() {
		tmpTree = new int[N][N];
		int max = 0, maxX = -1, maxY = -1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tree[i][j] <= 0 || tree[i][j] == -100)
					continue;
				int cnt = tree[i][j];
				for (int a = 0; a < 4; a++) {
					for (int b = 1; b <= K; b++) {
						int nx = i + hdx[a]*b;
						int ny = j + hdy[a]*b;
						if (!inRange(nx, ny) || tree[nx][ny] <= 0)
							break;
						else {
							cnt += tree[nx][ny];
						}
					}
				}
				tmpTree[i][j] = cnt;
				if (max < cnt) {
					max = cnt;
					maxX = i;
					maxY = j;
				}
			}
		}
		if(maxX == -1 && maxY == -1) return;
		ans += max;
		
//		System.out.println();
//        System.out.println(maxX + " " + maxY);
//        System.out.println();
		
		tree[maxX][maxY] = -(C+1);
		for (int a = 0; a < 4; a++) {
			for (int b = 1; b <= K; b++) {
				int nx = maxX + hdx[a]*b;
				int ny = maxY + hdy[a]*b;
				if (!inRange(nx, ny))
					break;
				else if(tree[nx][ny] == -100) {
					break;
				}
				else if(tree[nx][ny] <= 0) {
					tree[nx][ny] = -(C + 1);
					break;
				}
				else {
					tree[nx][ny] = -(C + 1);
				}
			}
		}
	}

	public static void remove() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tree[i][j] == -100) {
					continue;
				}
				else if (tree[i][j] < 0) {
					tree[i][j] += 1;
				}
			}
		}
	}
	
	public static void print() {
		System.out.println("tree");
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.toString(tree[i]));
		}
	}
	public static void print1() {
		System.out.println("tmp");
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.toString(tmpTree[i]));
		}
	}

}