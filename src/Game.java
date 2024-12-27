import java.util.Scanner;

class Game {
    private final Field field;
    private final Tank playerTank;
    private boolean isGameOver;

    public Game(int rows, int cols, int enemyCount) {
        field = new Field(rows, cols);
        playerTank = new Tank(0, 0, 'P');
        field.placeTank(playerTank);
        field.generateEnemies(enemyCount);
        field.generateObstacles();
        isGameOver = false;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (!isGameOver) {
            field.display();
            System.out.print("Введите команду (w/a/s/d/f): ");
            String command = scanner.nextLine();
            handlePlayerCommand(command);
            if (!isGameOver) {
                field.moveEnemies(playerTank);
                checkGameState();
            }
        }
        scanner.close();
    }

    private void handlePlayerCommand(String command) {
        switch (command) {
            case "w":
            case "a":
            case "s":
            case "d":
                field.moveTank(playerTank, command);
                break;
            case "f":
                field.playerShoot(playerTank);
                break;
            default:
                System.out.println("Неверная команда!");
        }
        checkGameState();
    }

    private void checkGameState() {
        if (!field.isPlayerAlive(playerTank)) {
            System.out.println("Игра окончена! Вы проиграли.");
            isGameOver = true;
        } else if (field.allEnemiesDestroyed()) {
            System.out.println("Поздравляем! Вы выиграли.");
            isGameOver = true;
        }
    }
}
