package model.abilities;

import model.world.Champion;
import model.world.Damageable;

import java.util.ArrayList;

public class HealingAbility extends Ability {

	private int healAmount;

	public HealingAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int actionsRequired, int healAmount) {
		super(name, manaCost, baseCooldown, castRange, castArea, actionsRequired);
		this.healAmount = healAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		for (Damageable target:targets) {
			target.setCurrentHP(target.getCurrentHP()+this.healAmount);
		}
		setCurrentCooldown(getBaseCooldown());
	}
}
