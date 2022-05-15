package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.*;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.*;
import model.world.*;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

public class Game {

	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player p1, Player p2) {
		this.firstLeaderAbilityUsed = false;
		this.secondLeaderAbilityUsed = false;
		this.firstPlayer = p1;
		this.secondPlayer = p2;
		turnOrder = new PriorityQueue(6);
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		placeChampions();
		placeCovers();
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public static void loadChampions(String filepath) throws IOException {
		availableChampions = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals("H")) {
				Hero h = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			} else if (content[0].equals("V")) {
				Villain h = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			} else if (content[0].equals("A")) {
				AntiHero h = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			}
			line = br.readLine();
		}
		br.close();
	}

	private static Ability getAbilityFromAvailable(String string) {
		for (Ability ability : availableAbilities) {
			if (ability.getName().equals(string))
				return ability;
		}
		return null;
	}

	public static void loadAbilities(String filepath) throws IOException {
		availableAbilities = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals("CC")) {
				availableAbilities.add(new CrowdControlAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), getEffect(content[7], Integer.parseInt(content[8]))));
			} else if (content[0].equals("DMG")) {
				availableAbilities.add(new DamagingAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), Integer.parseInt(content[7])));
			} else if (content[0].equals("HEL")) {
				availableAbilities.add(new HealingAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), Integer.parseInt(content[7])));
			}
			line = br.readLine();
		}
		br.close();
	}

	private static Effect getEffect(String name, int duration) {
		if (name.equals("Dodge"))
			return new Dodge(duration);
		if (name.equals("Disarm"))
			return new Disarm(duration);
		if (name.equals("Embrace"))
			return new Embrace(duration);
		if (name.equals("Stun"))
			return new Stun(duration);
		if (name.equals("Shield"))
			return new Shield(duration);
		if (name.equals("Shock"))
			return new Shock(duration);
		if (name.equals("PowerUp"))
			return new PowerUp(duration);
		if (name.equals("SpeedUp"))
			return new SpeedUp(duration);
		if (name.equals("Silence"))
			return new Silence(duration);
		if (name.equals("Root"))
			return new Root(duration);
		return null;
	}

	public void placeChampions() {
		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
			board[0][i + 1] = firstPlayer.getTeam().get(i);
			firstPlayer.getTeam().get(i).setLocation(new Point(0, i + 1));
			;
		}
		for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
			board[4][i + 1] = secondPlayer.getTeam().get(i);
			secondPlayer.getTeam().get(i).setLocation(new Point(4, i + 1));
			;
		}
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = (int) (Math.random() * 3) + 1;
			int y = (int) (Math.random() * 5);
			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}
	}

	public Champion getCurrentChampion() {

		if (turnOrder.size() == 0) {
			for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
				if (firstPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
					turnOrder.insert(firstPlayer.getTeam().get(i));
			}
			for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
				if (secondPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
					turnOrder.insert(secondPlayer.getTeam().get(i));
			}
		}
		return (Champion) this.turnOrder.peekMin();

	}


	public Player checkGameOver() {
		int counter = 0;
		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
			if (firstPlayer.getTeam().get(i).getCondition() == Condition.KNOCKEDOUT)
				counter++;
		}
		if (counter == 3)
			return secondPlayer;

		counter = 0;

		for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
			if (secondPlayer.getTeam().get(i).getCondition() == Condition.KNOCKEDOUT)
				counter++;
		}
		if (counter == 3)
			return firstPlayer;

		return null;
	}

	public void move(Direction d) throws UnallowedMovementException {
		if (d == Direction.UP) {
			if (getCurrentChampion().getLocation().x != 4) {
				if (board[getCurrentChampion().getLocation().x + 1][getCurrentChampion().getLocation().y] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x + 1, getCurrentChampion().getLocation().y);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					board[getCurrentChampion().getLocation().x + 1][getCurrentChampion().getLocation().y] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");

		} else if (d == Direction.DOWN) {
			if (getCurrentChampion().getLocation().x == 0) {
				if (board[getCurrentChampion().getLocation().x - 1][getCurrentChampion().getLocation().y] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x - 1, getCurrentChampion().getLocation().y);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					board[getCurrentChampion().getLocation().x - 1][getCurrentChampion().getLocation().y] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");

		} else if (d == Direction.LEFT) {
			if (getCurrentChampion().getLocation().y == 0) {
				if (board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y - 1] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x, getCurrentChampion().getLocation().y - 1);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y - 1] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");

		} else if (d == Direction.RIGHT) {
			if (getCurrentChampion().getLocation().y == 4) {
				if (board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y + 1] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x, getCurrentChampion().getLocation().y + 1);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y + 1] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");
		}
	}

	public void attack(Direction d) {

	}
	public ArrayList<Damageable> validTarget(Champion champion) {
		ArrayList<Damageable> validT = new ArrayList<>();
		for (int i = 0; i < getBoardheight(); i++) {
			for (int j = 0; j < getBoardwidth(); j++) {
				if (Math.abs(champion.getLocation().x - i) == 1 && Math.abs(champion.getLocation().y - j) == 1 && getBoard()[i][j] != null)
					validT.add((Damageable) getBoard()[i][j]);
				if (Math.abs(champion.getLocation().x - i) == 1 && Math.abs(champion.getLocation().y - j) == 0 && getBoard()[i][j] != null)
					validT.add((Damageable) getBoard()[i][j]);
				if (Math.abs(champion.getLocation().x - i) == 0 && Math.abs(champion.getLocation().y - j) == 1 && getBoard()[i][j] != null)
					validT.add((Damageable) getBoard()[i][j]);
			}
		}
		return validT;
	}

	public void castAbility(Ability a) throws NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException, AbilityUseException {
		if (a.getManaCost() <= getCurrentChampion().getMana() && a.getRequiredActionPoints() <= getCurrentChampion().getCurrentActionPoints()) {
			if (a.getCurrentCooldown() == 0) {
				for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
					if (getCurrentChampion().getAppliedEffects().get(i).getName().equals("Silence"))
						return;
				}
			}else
				throw new AbilityUseException();
		}else
			throw new NotEnoughResourcesException();

		ArrayList<Damageable> target = new ArrayList<>();

		switch (a.getCastArea()) {
			case SELFTARGET -> {
				target.add(getCurrentChampion());
				a.execute(target);
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
			}
			case SURROUND -> {
				target = validTarget(getCurrentChampion());
				if (a instanceof DamagingAbility) {
					a.execute(target);
				}
				if (a instanceof CrowdControlAbility) {
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Cover) {
							target.remove(i);
							i--;
						}
					}
					a.execute(target);
				}
				if (a instanceof HealingAbility) {
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Cover) {
							target.remove(i);
							i--;
						}
					}
					a.execute(target);
				}
			}
			case TEAMTARGET -> {
				if (a instanceof DamagingAbility) {
					if (firstPlayer.getTeam().contains(getCurrentChampion())) {
						target.addAll(secondPlayer.getTeam());
					} else
						target.addAll(firstPlayer.getTeam());
				} else if (a instanceof HealingAbility) {
					if (firstPlayer.getTeam().contains(getCurrentChampion())) {
						target.addAll(firstPlayer.getTeam());
					} else
						target.addAll(secondPlayer.getTeam());
				} else {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.BUFF)) {
						if (firstPlayer.getTeam().contains(getCurrentChampion())) {
							target.addAll(firstPlayer.getTeam());
						} else
							target.addAll(secondPlayer.getTeam());
					} else {
						if (firstPlayer.getTeam().contains(getCurrentChampion())) {
							target.addAll(secondPlayer.getTeam());
						} else
							target.addAll(firstPlayer.getTeam());
					}
				}
			}
		}
	}



	public void castAbility(Direction d, Ability a) throws NotEnoughResourcesException, InvalidTargetException {
		if (a.getManaCost() <= getCurrentChampion().getMana() && a.getRequiredActionPoints() <= getCurrentChampion().getCurrentActionPoints() && a.getCurrentCooldown() == 0) {
			for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
				if (getCurrentChampion().getAppliedEffects().get(i).getName().equals("Silence"))
					return;
			}
		} else
			throw new NotEnoughResourcesException();

		ArrayList<Damageable> target = new ArrayList<>();
		switch (d) {
			case UP -> {
				if (a instanceof DamagingAbility) {
					for (int i = 1; i <= a.getCastRange(); i++) {
						if (getCurrentChampion().getLocation().x + i < getBoardheight()) {
							if (getBoard()[getCurrentChampion().getLocation().x + i][getCurrentChampion().getLocation().y] instanceof Champion) {
								if (firstPlayer.getTeam().contains(getCurrentChampion())) {
									if (!(firstPlayer.getTeam().contains((Champion) getBoard()[getCurrentChampion().getLocation().x + i][getCurrentChampion().getLocation().y]))) {
										target.add((Damageable) getBoard()[getCurrentChampion().getLocation().x + i][getCurrentChampion().getLocation().y]);
									} else
										continue;
								} else if (!(secondPlayer.getTeam().contains((Champion) getBoard()[getCurrentChampion().getLocation().x + i][getCurrentChampion().getLocation().y]))) {
									target.add((Damageable) getBoard()[getCurrentChampion().getLocation().x + i][getCurrentChampion().getLocation().y]);
								} else
									continue;

							} else
								target.add((Damageable) getBoard()[getCurrentChampion().getLocation().x + i][getCurrentChampion().getLocation().y]);
						} else
							throw new InvalidTargetException();
					}
				} else if (a instanceof HealingAbility) {

				}
			}
		}
	}

	public boolean myTeam() {
		return firstPlayer.getTeam().contains(getCurrentChampion());
	}

	public ArrayList<Champion> AntiHeroCheck(Player firstPlayer, Player secondPlayer) {
		ArrayList<Champion> targets = new ArrayList<>();
		targets.addAll(firstPlayer.getTeam());
		targets.addAll(secondPlayer.getTeam());
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).equals(firstPlayer.getLeader()) || targets.get(i).equals(secondPlayer.getLeader())) {
				targets.remove(i);
				i--;
			}
		}
		return targets;
	}

	public void useLeaderAbility() throws CloneNotSupportedException, LeaderAbilityAlreadyUsedException {
		ArrayList<Champion> targets = new ArrayList<>();
		if (!firstLeaderAbilityUsed) {
			if (getCurrentChampion().equals(firstPlayer.getLeader())) {
				if (getCurrentChampion() instanceof Hero)
					targets.addAll(firstPlayer.getTeam());
				if (getCurrentChampion() instanceof Villain)
					targets.addAll(secondPlayer.getTeam());
				else
					targets = AntiHeroCheck(firstPlayer, secondPlayer);

				getCurrentChampion().useLeaderAbility(targets);
				firstLeaderAbilityUsed = true;
			}
		} else
			throw new LeaderAbilityAlreadyUsedException();

		if (!secondLeaderAbilityUsed) {
			if (getCurrentChampion().equals(secondPlayer.getLeader())) {
				if (getCurrentChampion() instanceof Hero)
					targets.addAll(secondPlayer.getTeam());
				if (getCurrentChampion() instanceof Villain)
					targets.addAll(firstPlayer.getTeam());
				else
					targets = AntiHeroCheck(firstPlayer, secondPlayer);
			}
			getCurrentChampion().useLeaderAbility(targets);
			secondLeaderAbilityUsed = true;
		} else
			throw new LeaderAbilityAlreadyUsedException();
	}

}

