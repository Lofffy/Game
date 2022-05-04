package model.world;

import java.awt.*;

public interface Damageable {
    Point getLocation();
    int getCurrentHp();
    void setCurrentHp(int hp);
}
