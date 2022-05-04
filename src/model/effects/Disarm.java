package model.effects;

import engine.Game;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Disarm extends Effect {


	public Disarm(int d) {
		super("Disarm", d, EffectType.DEBUFF);
	}

	@Override
	void apply(Champion c) {
		DamagingAbility d = new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SELFTARGET, 1, 50);
		c.getAbilities().add(d);
		c.setSpeed(0);
	}
	@Override
	void remove(Champion c) {

	}
	public static void loadDMG(String filepath,Champion c) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (true){
			String[] content = line.split(",");
			if(Objects.equals(c.getName(), content[1])) {
				c.setSpeed(Integer.parseInt(content[5]));
				break;
			}else
				br.readLine();
		}

	}
	public static void main(String[] args) {
		System.out.println("Fetch ya omar");
	}
}
