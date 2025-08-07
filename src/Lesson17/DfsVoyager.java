package Lesson17;

public class DfsVoyager implements Voyager {
    @Override
    public int lookupIslands(int[][] map) {
        if (map == null || map.length == 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 1) {
                    dfs(map, i, j);
                    count++;
                }
            }
        }
        return count;
    }

    private void dfs(int[][] map, int i, int j) {
        if (i < 0 || j < 0 || i >= map.length || j >= map[0].length || map[i][j] != 1) {
            return;
        }
        map[i][j] = 2;
        dfs(map, i + 1, j);
        dfs(map, i - 1, j);
        dfs(map, i, j + 1);
        dfs(map, i, j - 1);
    }
}
