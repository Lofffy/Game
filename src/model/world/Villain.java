package model.world;

import java.util.ArrayList;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion target : targets) {
			if (target.getCurrentHP() < (target.getMaxHP() * .3))
				target.setCondition(Condition.KNOCKEDOUT);
		}
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
