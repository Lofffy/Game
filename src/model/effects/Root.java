package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root(int d) {
		super("Root", d, EffectType.DEBUFF);
	}

	@Override
	void apply(Champion c) {
		c.getAppliedEffects().add(clone());
		if(!(c.getCondition().equals(Condition.INACTIVE)))
		c.setCondition(Condition.ROOTED);

	}

	@Override
	void remove(Champion c) {
		RemoveEffect("Root",c);

		if(c.getCondition().equals(Condition.ROOTED))
			c.setCondition(Condition.ACTIVE);


	}
}
