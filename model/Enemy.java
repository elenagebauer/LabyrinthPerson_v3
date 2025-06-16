package model;

public class Enemy {
    private int enemyX;
    private int enemyY;

    public Enemy (int startX, int startY) {
        this.enemyX = startX;
        this.enemyY = startY;
    }
    public int getX() {
        return enemyX;
    }
    public int getY() {
        return enemyY;
    }

    public void setEnemyX(int enemyX) {
        this.enemyX = enemyX;
    }

    public void setEnemyY(int enemyY) {
        this.enemyY = enemyY;
    }
}
