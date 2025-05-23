package org.example.view.forms;

import org.example.model.Psychotherapist;
import org.example.view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm extends JFrame {
//    private Psychotherapist user;
    private JPanel navigationPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private final Color NAV_BAR_COLOR = new Color(45, 62, 80); // tamnoplava
    private final Color BUTTON_COLOR = new Color(52, 73, 94); // malo svetlija plava
    private final Color BUTTON_HOVER_COLOR = new Color(93, 109, 126);
    private final Color CONTENT_BG_COLOR = Color.WHITE;

    public MainForm() {
        initialize();
        setVisible(true);
    }

    private void initialize() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((int) (screenWidth * 0.75), (int) (screenHeight * 0.75));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Clinic New Beginning");

        setLayout(new BorderLayout());

        navigationPanel = new JPanel();
        navigationPanel.setPreferredSize(new Dimension(getWidth() / 4, getHeight()));
        navigationPanel.setBackground(NAV_BAR_COLOR);
        navigationPanel.setLayout(new GridLayout(10, 1, 10, 10));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG_COLOR);

        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        initializeNavigationButtons();
        initializeContentPanels();
    }

    private void initializeNavigationButtons() {
        addButton("Therapists", "psychotherapists");
        addButton("Profile", "profile");
        addButton("New Clients", "clientApplications");
        addButton("Completed Sessions", "completedSessions");
        addButton("Upcoming Sessions", "upcomingSessions");
//        addButton("Notes & Tests", "notesTests");
        addButton("Publish Data", "dataPublishing");
        addButton("Payments & Debts", "paymentsDebts");

    }

    private void addButton(String title, String panelName) {
        JButton button = new JButton(title);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> cardLayout.show(contentPanel, panelName));

        // Hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        navigationPanel.add(button);
    }

    private void initializeContentPanels() {
//        contentPanel.add(createPanelWithLabel("Psychotherapist Overview"), "psychotherapists");
//        contentPanel.add(panelPsychotherapistOverview, "psychotherapist");
        createPanelPsychotherapistOverview();
//        contentPanel.add(createPanelWithLabel("My Profile"), "profile");
        createPanelPsychotherapistProfile();
//        contentPanel.add(createPanelWithLabel("New Client Applications"), "clientApplications");
        createPanelNewClientApplication();
//        contentPanel.add(createPanelWithLabel("Completed Sessions"), "completedSessions");
        createPanelCompletedSessions();
//        contentPanel.add(createPanelWithLabel("Upcoming Sessions"), "upcomingSessions");
        createPanelUpcomingSessions();
//        contentPanel.add(createPanelWithLabel("Notes and Tests"), "notesTests");
//        contentPanel.add(createPanelWithLabel("Data Publishing"), "dataPublishing");
        createPanelDataPublish();
//        contentPanel.add(createPanelWithLabel("Payments and Debts"), "paymentsDebts");
        createPanelPaymentsAndDebts();
        cardLayout.show(contentPanel, "psychotherapists");

    }

    private void createPanelPsychotherapistOverview(){
        JPanel panel = new PsychotherapistsOverviewPanel();
        contentPanel.add(panel, "psychotherapists");
        panel.setBackground(CONTENT_BG_COLOR);
    }
    private void createPanelPsychotherapistProfile(){
        JPanel panel = new PsychotherapistProfilePanel();
        contentPanel.add(panel, "profile");
        panel.setBackground(CONTENT_BG_COLOR);
    }
    private void createPanelNewClientApplication(){
        JPanel panel = new NewClientApplicationPanel();
        contentPanel.add(panel, "clientApplications");
        panel.setBackground(CONTENT_BG_COLOR);
    }
    private void createPanelCompletedSessions(){
        JPanel panel = new CompletedSessionsPanel();
        contentPanel.add(panel, "completedSessions");
        panel.setBackground(CONTENT_BG_COLOR);
    }
    private void createPanelUpcomingSessions(){
        JPanel panel = new UpcomingSessionsPanel();
        contentPanel.add(panel, "upcomingSessions");
        panel.setBackground(CONTENT_BG_COLOR);
    }
    private void createPanelDataPublish(){
        JPanel panel = new DataPublishPanel();
        contentPanel.add(panel, "dataPublishing");
        panel.setBackground(CONTENT_BG_COLOR);
    }
    private void createPanelPaymentsAndDebts(){
        JPanel panel = new PaymentsAndDebtsPanel();
        contentPanel.add(panel, "paymentsDebts");
        panel.setBackground(CONTENT_BG_COLOR);
    }

}
