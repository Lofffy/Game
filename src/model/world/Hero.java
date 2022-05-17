package model.world;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

import java.util.ArrayList;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		for (Champion target : targets) {
			Embrace e = new Embrace(2);
			e.apply(target);
			for (int j = 0; j < target.getAppliedEffects().size(); j++) {
				if (target.getAppliedEffects().get(j).getType().equals(EffectType.DEBUFF)) {
					target.getAppliedEffects().get(j).remove(target);
					j--;
				}
			}
			target.getAppliedEffects().add((Effect) new Embrace(2).clone());
		}
	}
	public int compareTo(Object o) {
		return super.compareTo((Champion) o);
	}

}
