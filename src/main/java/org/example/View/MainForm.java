package org.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm extends JFrame {

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        addButton("Pregled psihoterapeuta", "psihoterapeuti");
        addButton("Moj profil", "profil");
        addButton("Prijave novih klijenata", "prijaveKlijenata");
        addButton("Održane seanse", "odrzaneSeanse");
        addButton("Buduće seanse", "buduceSeanse");
        addButton("Beleške i testovi", "beleskeTestovi");
        addButton("Objavljivanje podataka", "objavljivanje");
        addButton("Uplate i dugovanja", "uplateDugovanja");
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

        // Hover efekat
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
        contentPanel.add(createPanelWithLabel("Pregled psihoterapeuta"), "psihoterapeuti");
        contentPanel.add(createPanelWithLabel("Moj profil"), "profil");
        contentPanel.add(createPanelWithLabel("Prijave novih klijenata"), "prijaveKlijenata");
        contentPanel.add(createPanelWithLabel("Održane seanse"), "odrzaneSeanse");
        contentPanel.add(createPanelWithLabel("Buduće seanse"), "buduceSeanse");
        contentPanel.add(createPanelWithLabel("Beleške i testovi"), "beleskeTestovi");
        contentPanel.add(createPanelWithLabel("Objavljivanje podataka"), "objavljivanje");
        contentPanel.add(createPanelWithLabel("Uplate i dugovanja"), "uplateDugovanja");

        cardLayout.show(contentPanel, "psihoterapeuti");
    }

    private JPanel createPanelWithLabel(String text) {
        JPanel panel = new JPanel();
        panel.setBackground(CONTENT_BG_COLOR);
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 20));
        panel.add(label);
        return panel;
    }
}
