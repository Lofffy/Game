package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {
	static Condition temp ;
	static boolean Applied ;

	public Stun(int d) {
		super("Stun", d, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c)  {
		temp = c.getCondition();
		c.setCondition(Condition.INACTIVE);
		Applied = true ;
	}

	@Override
	public void remove(Champion c) {
		if (Applied) {
			RemoveEffect("Stun", c);
			c.setCondition(temp);

		}else
			c.setCondition(Condition.ACTIVE);
	}
}
