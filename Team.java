package proekt;

import java.util.ArrayList;
import java.util.List;

public class Team {

	private String name;
	private List<String> players;
	private int points;

	public Team(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.points = 0;
	}

	public String getName() {
		return name;
	}

	public List<String> getPlayers() {
		return players;
	}

	public int getPoints() {
		return points;
	}

	public void addPlayer(String player) {
		players.add(player);
	}

	public void addPoints(int points) {
		this.points += points;
	}

	@Override
	public String toString() {
		return name + " (Points: " + points + ")";
	}
}