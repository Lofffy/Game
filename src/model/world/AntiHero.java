package model.world;

import java.util.ArrayList;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	void useLeaderAbility(ArrayList<Champion> targets) {

	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
