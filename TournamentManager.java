package proekt;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TournamentManager  {
    private List<Team> teams;
    private CustomLinkedList<Match> matches;
	private int score1;
	private int score2;

    public TournamentManager() {
        teams = new ArrayList<>();
        matches = new CustomLinkedList<>();
    }

    public void addTeam(Team team) throws IllegalArgumentException {
        if (teams.stream().anyMatch(t -> t.getName().equalsIgnoreCase(team.getName()))) {
            throw new IllegalArgumentException("Ima obtor s tova ime");
        }
        teams.add(team);
    }

    public void removeTeam(Team team) {
        matches.removeIf(match -> match.getTeam1().equals(team) || match.getTeam2().equals(team));
        teams.remove(team);
    }

    public void addMatch(Match match) throws IllegalArgumentException {
        for (Match m : matches) {
            if ((m.getTeam1().equals(match.getTeam1()) && m.getTeam2().equals(match.getTeam2()) && 
                 m.getDate().equals(match.getDate())) ||
                (m.getTeam1().equals(match.getTeam2()) && m.getTeam2().equals(match.getTeam1()) && 
                 m.getDate().equals(match.getDate()))) {
                throw new IllegalArgumentException("Tazi sreshta veche q ima");
            }
        }
        
        matches.add(match);
        updateStandings(match);
    }

    private void updateStandings(Match match) {
        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        setScore1(match.getScoreTeam1());
        setScore2(match.getScoreTeam2());
        team1.addPoints(-team1.getPoints());
        team2.addPoints(-team2.getPoints());
        for (Match m : matches) {
            if (m.getTeam1().equals(team1) || m.getTeam2().equals(team1) || 
                m.getTeam1().equals(team2) || m.getTeam2().equals(team2)) {
                updateTeamPoints(m);
            }
        }
    }

    private void updateTeamPoints(Match match) {
        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        int score1 = match.getScoreTeam1();
        int score2 = match.getScoreTeam2();

        if (score1 > score2) {
            team1.addPoints(3);
        } else if (score1 < score2) {
            team2.addPoints(3);
        } else {
            team1.addPoints(1);
            team2.addPoints(1);
        }
    }

    public List<Team> getTeams() {
        return teams;
    }

    public CustomLinkedList<Match> getMatches() {
        return matches;
    }

    public List<Team> getStandings() {
        List<Team> sortedTeams = new ArrayList<>(teams);
        insertionSort(sortedTeams);
        return sortedTeams;
    }

    private void insertionSort(List<Team> teams) {
        for (int i = 1; i < teams.size(); i++) {
            Team key = teams.get(i);
            int j = i - 1;

            while (j >= 0 && teams.get(j).getPoints() < key.getPoints()) {
                teams.set(j + 1, teams.get(j));
                j = j - 1;
            }
            teams.set(j + 1, key);
        }
    }

    public List<Match> searchMatchesByTeam(Team team) {
        List<Match> result = new ArrayList<>();
        for (Match match : matches) {
            if (match.getTeam1().equals(team) || match.getTeam2().equals(team)) {
                result.add(match);
            }
        }
        return result;
    }

    public List<Match> searchMatchesByDate(LocalDate date) {
        List<Match> result = new ArrayList<>();
        for (Match match : matches) {
            if (match.getDate().equals(date)) {
                result.add(match);
            }
        }
        return result;
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static TournamentManager loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (TournamentManager) ois.readObject();
        }
    }

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}
}