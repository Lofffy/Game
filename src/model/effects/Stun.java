package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {



	public Stun(int d) {
		super("Stun", d, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c)  {
		c.setCondition(Condition.INACTIVE);
	}

	@Override
	public void remove(Champion c) {
		if (c.getCondition().equals(Condition.INACTIVE))
			c.setCondition(Condition.ACTIVE);
	}
}
