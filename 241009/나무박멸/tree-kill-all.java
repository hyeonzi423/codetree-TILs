import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

	static int N, M, K, C, ans;
	static int[][] map, tmpMap, med;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	static int[] sdx = {-1, 1, 1, -1};
	static int[] sdy = {1, 1, -1, -1};

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		ans = 0;

		map = new int[N][N];
		tmpMap = new int[N][N];
		med = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int i = 0; i < M; i++) {
			grow();
			expand();
			candidate();
			remove();
		}
		System.out.println(ans);
	}

	public static void grow() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (map[i][j] == 0 || map[i][j] == -1) continue;
				for(int k = 0; k < 4; k++) {
					int nx = i + dx[k];
					int ny = j + dy[k];
					if(inRange(nx, ny) && map[nx][ny] > 0) {
						map[i][j]++;
					}
				}
			}
		}
	}
	
	public static void expand() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (map[i][j] <= 0) continue;
				int cnt = 0;
				ArrayList<Integer> dir = new ArrayList<>();
				for(int k = 0; k < 4; k++) {
					int nx = i + dx[k];
					int ny = j + dy[k];
					if(inRange(nx, ny) && map[nx][ny] == 0 && med[nx][ny] == 0) {
						cnt++;
						dir.add(k);
					}
				}
				for(int d : dir) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					tmpMap[nx][ny] += map[i][j]/cnt;
				}
			}
		}
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				map[i][j] += tmpMap[i][j];
				tmpMap[i][j] = 0;
			}
		}
	}
	
	public static void candidate() {
	    int max = 0, mx = -1, my = -1;

	    for(int i = 0; i < N; i++) {
	        for(int j = 0; j < N; j++) {
	            if(map[i][j] <= 0) continue;
	            int tmp = map[i][j];
	            for(int k = 0; k < 4; k++) {
	                for(int d = 1; d <= K; d++) {
	                    int nx = i + d*sdx[k];
	                    int ny = j + d*sdy[k];
	                    if(!inRange(nx, ny) || map[nx][ny] <= 0) {
	                        break;
	                    } 
	                    tmp += map[nx][ny];
	                }
	            }
	            if(tmp > max) {
	                mx = i;
	                my = j;
	                max = tmp;
	            }
	        }
	    }
	    ans += max;
		if(mx == -1) return;	
	    med[mx][my] = C + 1;
	    map[mx][my] = 0;
	    for(int k = 0; k < 4; k++) {
	        for(int d = 1; d <= K; d++) {
	            int nx = mx + d*sdx[k];
	            int ny = my + d*sdy[k];
	            if(!inRange(nx, ny) || map[nx][ny] == -1) {
	                break;
	            } 
	            if(map[nx][ny] == 0) {
	            	med[nx][ny] = C + 1;
	            	break;
	            }
	            if(map[nx][ny] > 0) {
	                med[nx][ny] = C + 1;
	                map[nx][ny] = 0;
	            }
	        }
	    }
	}

	
	public static void remove() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(map[i][j] == -1) {
					continue;
				}if(med[i][j] > 0) {
					med[i][j]--;
				}
			}
		}
	}

	public static boolean inRange(int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
}