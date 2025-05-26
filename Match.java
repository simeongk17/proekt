package proekt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
	public class Match  {
	    private Team team1;
	    private Team team2;
	    private int scoreTeam1;
	    private int scoreTeam2;
	    private LocalDate date;

	    public Match(Team team1, Team team2, int scoreTeam1, int scoreTeam2, LocalDate date) {
	        this.team1 = team1;
	        this.team2 = team2;
	        this.scoreTeam1 = scoreTeam1;
	        this.scoreTeam2 = scoreTeam2;
	        this.date = date;
	    }

	    public Team getTeam1() {
	        return team1;
	    }

	    public Team getTeam2() {
	        return team2;
	    }

	    public int getScoreTeam1() {
	        return scoreTeam1;
	    }

	    public int getScoreTeam2() {
	        return scoreTeam2;
	    }

	    public LocalDate getDate() {
	        return date;
	    }

	    public void updateScore(int scoreTeam1, int scoreTeam2) {
	        this.scoreTeam1 = scoreTeam1;
	        this.scoreTeam2 = scoreTeam2;
	    }

	    @Override
	    public String toString() {
	        return team1.getName() + " " + scoreTeam1 + " - " + scoreTeam2 + " " + team2.getName() + 
	               " (" + date.format(DateTimeFormatter.ofPattern("date")) + ")";
	    }
	}