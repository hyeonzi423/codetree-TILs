import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    static int N, M, K;
    static int dx[] = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int dy[] = {0, 1, 1, 1, 0, -1, -1, -1};
    static ArrayList<Atom> atoms;
    
    static class Atom{
        int x, y, m, s, d;

        public Atom(int x, int y, int m, int s, int d) {
            this.x = x;
            this.y = y;
            this.m = m;
            this.s = s;
            this.d = d;
        }
    }
    
    static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        atoms = new ArrayList<>();
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            atoms.add(new Atom(x-1, y-1, m, s, d));
        }    
    }
    
    static void move() {
        ArrayList<Atom>[][] map = new ArrayList[N][N];
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                map[i][j] = new ArrayList<>();
            }
        }
        
        for(Atom now : atoms) {
            int nx = (now.x + now.s*dx[now.d] + N*N)%N;
            int ny = (now.y + now.s*dy[now.d] + N*N)%N;
            map[nx][ny].add(new Atom(nx, ny, now.m, now.s, now.d));
        }
        
        atoms.clear();
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(map[i][j].size() == 0) continue;
                else if(map[i][j].size() == 1) {
                    atoms.add(map[i][j].get(0));
                }else {
                    boolean dir = map[i][j].get(0).d % 2 == 0 ? true : false;
                    boolean flag = dir;
                    int sumM = 0, sumS = 0;
                    for(Atom tmp : map[i][j]) {
                        sumM += tmp.m;
                        sumS += tmp.s;
                        if(dir != flag) continue;
                        flag = tmp.d%2 == 0 ? true : false;
                        
                    }
                    if (sumM < 5) continue;
                    if(dir == flag) {
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 0));
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 2));
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 4));
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 6));
                    }else {
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 1));
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 3));
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 5));
                        atoms.add(new Atom(i, j, sumM/5, sumS/map[i][j].size(), 7));
                    }
                }
            }
        }
    }
    
    static int remain() {
        int sum = 0;
        for(Atom now:atoms) {
            sum += now.m;
            //System.out.println(now);
        }
        return sum;
    }
    
    public static void main(String[] args) throws IOException {
        init();
        for(int i = 0; i < K; i++) {
            move();
        }
        int res = remain();
        System.out.println(res);
    }

}