package Lesson17;

import java.util.LinkedList;

public class BfsVoyager implements Voyager{
    @Override
    public int lookupIslands(int[][] map) {
        if (map == null || map.length == 0) return 0;
        int count = 0;
        int rows = map.length;
        int cols = map[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 1) {
                    bfs(map, i, j, rows, cols);
                    count++;
                }
            }
        }
        return count;
    }

    private void bfs(int[][] map, int i, int j, int rows, int cols) {
        LinkedList<int[]> list = new LinkedList<>();
        list.add(new int[]{i, j});
        map[i][j] = 2;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!list.isEmpty()) {
            int[] cell = list.poll();
            for (int[] dir : directions) {
                int x = cell[0] + dir[0];
                int y = cell[1] + dir[1];
                if (x >= 0 && x < rows && y >= 0 && y < cols && map[x][y] == 1) {
                    list.add(new int[]{x, y});
                    map[x][y] = 2;
                }
            }
        }
    }
}
