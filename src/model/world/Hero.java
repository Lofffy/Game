package model.world;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.Stun;

import java.util.ArrayList;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActionsPerTurn, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActionsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {

		for (int i = 0; i < targets.size(); i++) {
			for (int j = 0; j < targets.get(i).getAppliedEffects().size(); j++) {
				if (targets.get(i).getAppliedEffects().get(j).getType().equals(EffectType.DEBUFF)) {
					targets.get(i).getAppliedEffects().get(j).remove(targets.get(i));
					targets.get(i).getAppliedEffects().remove(j);
					j--;
				}
			}
			Embrace e = new Embrace(2);
			e.apply(targets.get(i));
			targets.get(i).getAppliedEffects().add(e);
		}
	}
	public int compareTo(Object o) {
		return super.compareTo((Champion) o);
	}

}
