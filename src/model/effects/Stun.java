package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int d) {
		super("Stun", d, EffectType.DEBUFF);
	}

	@Override
	void apply(Champion c) {
		c.getAppliedEffects().add(clone());
		c.setCondition(Condition.INACTIVE);
	}

	@Override
	void remove(Champion c) {
		RemoveEffect("Stun",c);
		c.setCondition(Condition.ACTIVE);

	}
}
