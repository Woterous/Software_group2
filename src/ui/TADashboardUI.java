package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * TA Dashboard - Teaching Assistant Home Page (Swing Version)
 * 4号成员负责：TA端核心页面
 */
public class TADashboardUI extends JFrame {

    private String username;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public TADashboardUI(String username) {
        this.username = username;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("TA Recruitment System - Dashboard");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 主面板
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 246, 250));
        
        // 创建内容面板
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 246, 250));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 246, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ========== 1. 欢迎区域 ==========
        panel.add(createWelcomePanel());
        panel.add(Box.createVerticalStrut(20));

        // ========== 2. 快捷入口按钮 ==========
        panel.add(createQuickAccessPanel());
        panel.add(Box.createVerticalStrut(20));

        // ========== 3. 申请状态区域 ==========
        panel.add(createStatusPanel());
        panel.add(Box.createVerticalStrut(20));

        // ========== 4. 推荐岗位区域 ==========
        panel.add(createRecommendationPanel());
        panel.add(Box.createVerticalStrut(20));

        // ========== 5. 底部退出按钮 ==========
        panel.add(createFooterPanel());

        return panel;
    }

    /**
     * 创建欢迎面板
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel welcomeLabel = new JLabel("Welcome back, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(44, 62, 80));

        JLabel roleLabel = new JLabel("Role: Teaching Assistant");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(127, 140, 141));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(welcomeLabel);
        textPanel.add(roleLabel);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    /**
     * 创建快捷入口面板
     */
    private JPanel createQuickAccessPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(245, 246, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // 我的资料按钮
        panel.add(createMenuButton("👤 My Profile", new Color(52, 152, 219), e -> openMyProfile()));
        
        // 浏览岗位按钮
        panel.add(createMenuButton("🔍 Browse Jobs", new Color(39, 174, 96), e -> browseJobs()));
        
        // 我的申请按钮
        panel.add(createMenuButton("📋 My Applications", new Color(155, 89, 182), e -> viewMyApplications()));

        return panel;
    }

    /**
     * 创建菜单按钮
     */
    private JButton createMenuButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 绘制圆角背景
                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // 绘制文字
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX, textY);
                
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(0, 110));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        
        // 悬停效果
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    /**
     * 创建申请状态面板
     */
    private JPanel createStatusPanel() {
        JPanel panel = createSectionPanel("📊 Current Application Status");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 状态项1 - Pending
        panel.add(createStatusItem("TA for EBU6304 - Software Engineering", "Pending", new Color(243, 156, 18)));
        panel.add(Box.createVerticalStrut(8));
        
        // 状态项2 - Accepted
        panel.add(createStatusItem("TA for EBU6301 - Data Structures", "Accepted", new Color(39, 174, 96)));
        panel.add(Box.createVerticalStrut(8));
        
        // 状态项3 - Rejected
        panel.add(createStatusItem("TA for EBU5201 - Programming Fundamentals", "Rejected", new Color(231, 76, 60)));

        return panel;
    }

    /**
     * 创建状态项
     */
    private JPanel createStatusItem(String jobTitle, String status, Color statusColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // 左侧：状态圆点 + 岗位名
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(new Color(248, 249, 250));

        // 状态圆点
        JLabel dot = new JLabel("●");
        dot.setForeground(statusColor);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel jobLabel = new JLabel(jobTitle);
        jobLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jobLabel.setForeground(new Color(44, 62, 80));

        leftPanel.add(dot);
        leftPanel.add(jobLabel);

        // 右侧：状态标签
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(statusColor);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), 30));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    /**
     * 创建推荐岗位面板
     */
    private JPanel createRecommendationPanel() {
        JPanel panel = createSectionPanel("🎯 Recommended Jobs For You");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createRecommendationItem("TA for EBU6305", "Database Systems", "2026-04-15"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createRecommendationItem("TA for EBU6302", "Computer Networks", "2026-04-10"));

        return panel;
    }

    /**
     * 创建推荐岗位项
     */
    private JPanel createRecommendationItem(String title, String course, String deadline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // 左侧信息
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        infoPanel.setBackground(new Color(248, 249, 250));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(44, 62, 80));

        JLabel metaLabel = new JLabel("📚 " + course + "  ·  ⏰ Deadline: " + deadline);
        metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        metaLabel.setForeground(new Color(231, 76, 60));

        infoPanel.add(titleLabel);
        infoPanel.add(metaLabel);

        // 右侧申请按钮
        JButton applyBtn = new JButton("Apply Now");
        applyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setBackground(new Color(52, 152, 219));
        applyBtn.setFocusPainted(false);
        applyBtn.setBorderPainted(false);
        applyBtn.setPreferredSize(new Dimension(100, 35));
        applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Applying for: " + title));

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(applyBtn, BorderLayout.EAST);

        return panel;
    }

    /**
     * 创建底部面板
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(245, 246, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(120, 45));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());

        panel.add(logoutBtn);
        return panel;
    }

    /**
     * 创建通用区块面板
     */
    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 标题
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 包装标题和内容
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(titleLabel);
        wrapper.add(Box.createVerticalStrut(15));

        return wrapper;
    }

    // ========== 事件处理方法 ==========

    private void openMyProfile() {
        JOptionPane.showMessageDialog(this, "Opening My Profile...");
    }

    private void browseJobs() {
        JobListingUI jobList = new JobListingUI(username);
        jobList.setVisible(true);
        this.dispose();
    }

    private void viewMyApplications() {
        JOptionPane.showMessageDialog(this, "Viewing My Applications...");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ========== Main方法（测试用） ==========
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new TADashboardUI("TA User").setVisible(true);
        });
    }
}
