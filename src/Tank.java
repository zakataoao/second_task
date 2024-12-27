class Tank {
    private int x, y;
    private final char symbol;
    private boolean alive = true;
    private String lastDirection = "w";

    public Tank(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public char getSymbol() { return symbol; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void setLastDirection(String direction) { this.lastDirection = direction; }
    public String getLastDirection() { return lastDirection; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}
