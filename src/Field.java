import java.util.Random;

class Field {
    private final int rows;
    private final int cols;
    private final char[][] grid;
    private final Random random;
    private final Tank[] enemies;
    private static final char OBSTACLE = '#';

    public Field(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        this.random = new Random();
        this.enemies = new Tank[rows * cols];
        initializeField();
    }

    private void initializeField() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '.';
            }
        }
    }

    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void placeTank(Tank tank) {
        grid[tank.getX()][tank.getY()] = tank.getSymbol();
    }

    public void moveTank(Tank tank, String direction) {
        int x = tank.getX();
        int y = tank.getY();

        grid[x][y] = '.';

        int newX = x;
        int newY = y;


        switch (direction) {
            case "w": newX = Math.max(0, x - 1); break;
            case "s": newX = Math.min(rows - 1, x + 1); break;
            case "a": newY = Math.max(0, y - 1); break;
            case "d": newY = Math.min(cols - 1, y + 1); break;
        }

        if (grid[newX][newY] == '.') {
            tank.setPosition(newX, newY);
            grid[newX][newY] = tank.getSymbol();
            tank.setLastDirection(direction); // Запоминаем направление
        } else if (grid[newX][newY] == 'E') {
            System.out.println("Вы столкнулись с вражеским танком! Игра окончена.");
            tank.setAlive(false); // Игрок погибает
        } else if (grid[newX][newY] == '#') {
            System.out.println("Ход невозможен. Клетка занята препятствием.");
        } else {
            System.out.println("Ход невозможен. Клетка занята.");
        }


        grid[tank.getX()][tank.getY()] = tank.getSymbol();
    }



    public void playerShoot(Tank playerTank) {
        int x = playerTank.getX();
        int y = playerTank.getY();
        boolean hit = false;

        switch (playerTank.getLastDirection()) {
            case "w":
                for (int i = x - 1; i >= 0; i--) {
                    hit = processShot(i, y);
                    if (hit) break;
                }
                break;
            case "s":
                for (int i = x + 1; i < rows; i++) {
                    hit = processShot(i, y);
                    if (hit) break;
                }
                break;
            case "a":
                for (int i = y - 1; i >= 0; i--) {
                    hit = processShot(x, i);
                    if (hit) break;
                }
                break;
            case "d":
                for (int i = y + 1; i < cols; i++) {
                    hit = processShot(x, i);
                    if (hit) break;
                }
                break;
            default:
                System.out.println("Нет направления для стрельбы!");
                break;
        }

        if (!hit) {
            System.out.println("Выстрел не попал в цель.");
        }
    }

    private boolean processShot(int x, int y) {
        if (grid[x][y] == OBSTACLE) {
            System.out.println("Вы уничтожили препятствие!");
            grid[x][y] = '.';
            return true;
        } else if (grid[x][y] == 'E') {
            System.out.println("Вы уничтожили вражеский танк!");
            grid[x][y] = '.';


            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null && enemies[i].getX() == x && enemies[i].getY() == y) {
                    enemies[i].setAlive(false);
                    enemies[i] = null;
                    break;
                }
            }
            return true;
        }
        return false;
    }



    public void moveEnemies(Tank playerTank) {
        for (Tank enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                int x = enemy.getX();
                int y = enemy.getY();
                grid[x][y] = '.';

                String[] directions = {"w", "a", "s", "d"};
                String direction = directions[random.nextInt(4)];

                int newX = x;
                int newY = y;

                switch (direction) {
                    case "w": newX = Math.max(0, x - 1); break;
                    case "s": newX = Math.min(rows - 1, x + 1); break;
                    case "a": newY = Math.max(0, y - 1); break;
                    case "d": newY = Math.min(cols - 1, y + 1); break;
                }

                if (grid[newX][newY] == '.') {
                    enemy.setPosition(newX, newY);
                    grid[newX][newY] = enemy.getSymbol();
                } else if (grid[newX][newY] == 'P') {
                    System.out.println("Враг атаковал игрока! Игра окончена.");
                    playerTank.setAlive(false);
                    return;
                }
            }
        }
    }

    public boolean isPlayerAlive(Tank playerTank) {
        return playerTank.isAlive();
    }

    public boolean allEnemiesDestroyed() {
        for (Tank enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                return false;
            }
        }
        return true;
    }


    public void generateEnemies(int count) {
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            grid[x][y] = 'E';
            enemies[i] = new Tank(x, y, 'E');
        }
    }

    public void generateObstacles() {
        int obstacleCount = (rows * cols) / 10;
        for (int i = 0; i < obstacleCount; i++) {
            grid[random.nextInt(rows)][random.nextInt(cols)] = OBSTACLE;
        }
    }
}
