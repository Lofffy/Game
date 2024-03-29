package model.effects;

import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int d) {
		super("Dodge", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {

		int speed = c.getSpeed();
		int pre = (int)(speed*(5.00/100.00));
		c.setSpeed(speed+pre);
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Dodge",c);

		double dom = c.getSpeed()*100*1.05;
		double nom = 1.05*105;
		c.setSpeed((int)(dom/nom));
	}

	public static void main(String[] args) {
		int z = 0 ;
	}
}
