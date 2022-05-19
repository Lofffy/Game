package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {
	static boolean isAlreadyRooted  = false;

	public Root(int d) {
		super("Root", d, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {

		if(!(c.getCondition().equals(Condition.INACTIVE)))
			c.setCondition(Condition.ROOTED);
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Root", c);
		if (c.getCondition().equals(Condition.INACTIVE))
			c.setCondition(Condition.INACTIVE);
		else if (c.getCondition().equals(Condition.ROOTED))
			c.setCondition(Condition.ROOTED);
		else
			c.setCondition(Condition.ACTIVE);
	}
}
