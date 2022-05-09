package model.world;

import model.effects.EffectType;
import model.effects.Embrace;

import java.util.ArrayList;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion target : targets) {
			new Embrace(2).apply(target);
			for (int j = 0; j < target.getAppliedEffects().size(); j++) {
				if (target.getAppliedEffects().get(j).getType().equals(EffectType.DEBUFF)) {
					target.getAppliedEffects().remove(j);
					j--;
				}
			}
		}
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
