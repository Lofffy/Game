package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {
	static boolean isAlreadyRooted  = false;

	public Root(int d) {
		super("Root", d, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		c.getAppliedEffects().add(clone());
		if(c.getCondition().equals(Condition.ROOTED))
			isAlreadyRooted = true;
		if(!(c.getCondition().equals(Condition.INACTIVE)))
		     c.setCondition(Condition.ROOTED);

	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Root",c);
		if(!isAlreadyRooted) {
			if (c.getCondition().equals(Condition.ROOTED))
				c.setCondition(Condition.ACTIVE);
		}


	}
}
