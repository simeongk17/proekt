package proekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TournamentGUI extends JFrame {
    private final TournamentManager manager;
    private final JTable standingsTable;
    private final JTable matchesTable;

    public TournamentGUI() {
        
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        manager = new TournamentManager();
        

        standingsTable = createStandingsTable();
        matchesTable = createMatchesTable();
        

        JPanel mainPanel = new JPanel(new BorderLayout());
        

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablesPanel.add(new JScrollPane(standingsTable));
        tablesPanel.add(new JScrollPane(matchesTable));
        

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton("Add Team", this::addTeam));
        buttonPanel.add(createButton("Add Match", this::addMatch));
        buttonPanel.add(createButton("Refresh", e -> refreshTables()));
        

        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        refreshTables();
    }

    private JTable createStandingsTable() {
        return new JTable(new DefaultTableModel(
            new Object[]{"Position", "Team", "Points"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private JTable createMatchesTable() {
        return new JTable(new DefaultTableModel(
            new Object[]{"Team 1", "Score", "Team 2", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    private void addTeam(ActionEvent e) {
        String teamName = JOptionPane.showInputDialog(this, "Enter team name:");
        if (teamName != null && !teamName.trim().isEmpty()) {
            try {
                manager.addTeam(new Team(teamName));
                refreshTables();
                JOptionPane.showMessageDialog(this, "Team added successfully!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void addMatch(ActionEvent e) {
        if (manager.getTeams().size() < 2) {
            showError("You need at least 2 teams to create a match!");
            return;
        }

        JDialog dialog = new JDialog(this, "Add New Match", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        JComboBox<Team> team1Combo = new JComboBox<>(manager.getTeams().toArray(new Team[0]));
        JComboBox<Team> team2Combo = new JComboBox<>(manager.getTeams().toArray(new Team[0]));
        JSpinner score1Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner score2Spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        formPanel.add(new JLabel("Team 1:"));
        formPanel.add(team1Combo);
        formPanel.add(new JLabel("Team 2:"));
        formPanel.add(team2Combo);
        formPanel.add(new JLabel("Score 1:"));
        formPanel.add(score1Spinner);
        formPanel.add(new JLabel("Score 2:"));
        formPanel.add(score2Spinner);
        formPanel.add(new JLabel("Date (dd/MM/yyyy):"));
        formPanel.add(dateField);
        
 
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(ev -> {
            try {
                Team team1 = (Team) team1Combo.getSelectedItem();
                Team team2 = (Team) team2Combo.getSelectedItem();
                
                if (team1.equals(team2)) {
                    throw new IllegalArgumentException("A team cannot play against itself!");
                }
                
                int score1 = (Integer) score1Spinner.getValue();
                int score2 = (Integer) score2Spinner.getValue();
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                
                manager.addMatch(new Match(team1, team2, score1, score2, date));
                refreshTables();
                dialog.dispose();
                
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void refreshTables() {

        DefaultTableModel standingsModel = (DefaultTableModel) standingsTable.getModel();
        standingsModel.setRowCount(0);
        
        List<Team> standings = manager.getStandings();
        for (int i = 0; i < standings.size(); i++) {
            Team team = standings.get(i);
            standingsModel.addRow(new Object[]{i + 1, team.getName(), team.getPoints()});
        }
        

        DefaultTableModel matchesModel = (DefaultTableModel) matchesTable.getModel();
        matchesModel.setRowCount(0);
        
        for (Match match : manager.getMatches()) {
            matchesModel.addRow(new Object[]{
                match.getTeam1().getName(),
                match.getScoreTeam1() + " - " + match.getScoreTeam2(),
                match.getTeam2().getName(),
                match.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TournamentGUI gui = new TournamentGUI();
            gui.setVisible(true);
        });
    }
}