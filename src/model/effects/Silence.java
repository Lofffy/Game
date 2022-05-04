package model.effects;

import model.abilities.Ability;
import model.world.Champion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Silence extends Effect {

	public Silence(int d) {
		super("Silence", d, EffectType.DEBUFF);
	}

	@Override
	void apply(Champion c) {
		for (Ability a : c.getAbilities()) {
			c.getAbilities().remove(a);
		}
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() + 2);
		c.setCurrentActionPoints(c.getCurrentActionPoints() + 2);
	}

	@Override
	void remove(Champion c) {


		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() - 2);
		c.setCurrentActionPoints(c.getCurrentActionPoints() - 2);
	}


}
