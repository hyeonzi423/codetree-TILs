import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, M, ans;
    static int[][] map;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};
    static Dice dice;

    public static class Dice {
        int x, y, up, front, right, direction;

        public Dice(int x, int y, int up, int front, int right, int direction) {
            this.x = x;
            this.y = y;
            this.up = up;
            this.front = front;
            this.right = right;
            this.direction = direction;
        }

        public static void rotateRight(Dice dice){
            int tmp = dice.up;
            dice.up = 7 - dice.right;
            dice.right = tmp;
        }

        public static void rotateLeft(Dice dice){
            int tmp = dice.right;
            dice.right = 7 - dice.up;
            dice.up = tmp;
        }

        public static void rotateUp(Dice dice){
            int tmp = dice.front;
            dice.front = 7 - dice.up;
            dice.up = tmp;
        }

        public static void rotateDown(Dice dice){
            int tmp = dice.up;
            dice.up = 7 - dice.front;
            dice.front = tmp;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dice = new Dice(0, 0, 1, 2, 3, 1);
        ans = 0;

        for(int i = 0; i < M; i++){
            move(i);
            getScore();
        }
        System.out.println(ans);
    }

    public static void move(int turn){
        int nx, ny, nd;

        if(turn == 0){
            dice.x += dx[1];
            dice.y += dy[1];
            dice.rotateRight(dice);
        }else{
            int diceDown = 7 - dice.up;
            if(diceDown > map[dice.x][dice.y]){
                dice.direction = (dice.direction + 1) % 4;
            } else if (diceDown < map[dice.x][dice.y]) {
                dice.direction = (dice.direction + 3) % 4;
            }
            nx = dice.x + dx[dice.direction];
            ny = dice.y + dy[dice.direction];
            if(nx < 0 || ny < 0 || nx >= N || ny >= N){
                dice.direction = (dice.direction + 2) % 4;
                nx = dice.x + dx[dice.direction];
                ny = dice.y + dy[dice.direction];
            }
            dice.x = nx;
            dice.y = ny;
            if(dice.direction == 0){
                dice.rotateUp(dice);
            }else if (dice.direction == 1){
                dice.rotateRight(dice);
            } else if (dice.direction == 2) {
                dice.rotateDown(dice);
            }else{
                dice.rotateLeft(dice);
            }
        }
    }

    public static void getScore(){
        Queue<int[]> q = new LinkedList<int[]>();
        boolean[][] visited = new boolean[N][N];
        q.add(new int[]{dice.x, dice.y});
        visited[dice.x][dice.y] = true;
        int origin = map[dice.x][dice.y];

        while(!q.isEmpty()){
            int[] cur = q.poll();
            ans += origin;

            for (int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i];
                int ny = cur[1] + dy[i];
                if(nx < 0 || ny < 0 || nx >= N || ny >= N){ continue;}
                if(visited[nx][ny] || map[nx][ny] != origin){
                    continue;
                }
                visited[nx][ny] = true;
                q.add(new int[]{nx, ny});
            }
        }
    }
}
