package model.effects;

import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int d) {
		super("Dodge", d, EffectType.BUFF);
	}

	@Override
	void apply(Champion c) {
		int rand =(int)(Math.random()*2);
		if(rand==1);//Unknown'


		int speed = c.getSpeed();
		int pre = (int)(speed*(5.00/100.00));
		c.setSpeed(speed+pre);
	}

	@Override
	void remove(Champion c) {
		double dom = c.getSpeed()*100*1.05;
		double nom = 1.05*105;
		c.setSpeed((int)(dom/nom));
	}
}
