package model.world;

import model.effects.Stun;

import java.util.ArrayList;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange,
					int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		for (Champion target : targets)
			new Stun(2).apply(target);

	}

	@Override
	public int compareTo(Object o) {
		 return super.compareTo((Champion) o);
	}
}
