package model.world;

import java.util.ArrayList;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	void useLeaderAbility(ArrayList<Champion> targets) {
		for (int i = 0; i < targets.size(); i++) {

		}
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
