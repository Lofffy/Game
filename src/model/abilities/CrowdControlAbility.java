package model.abilities;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class CrowdControlAbility extends Ability {

	private Effect effect;

	public CrowdControlAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int actionsRequired, Effect effect) {
		super(name, manaCost, baseCooldown, castRange, castArea, actionsRequired);
		this.effect = effect;
	}

	public Effect getEffect() {
		return effect;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		for (Damageable target:targets) {
			if (target instanceof Champion ) {
				this.effect.apply((Champion) target);
			}
		}
	}
}
