package ui;

import model.Job;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**hhh
 * Job Listing Page - Available TA Positions (Swing Version)
 * 4号成员负责：TA端核心页面 - 岗位列表
 */
public class JobListingUI extends JFrame {

    private String username;
    private JPanel jobListContainer;
    private JTextField searchField;
    private JComboBox<String> filterCombo;

    public JobListingUI(String username) {
        this.username = username;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("TA Recruitment System - Available Jobs");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 246, 250));

        // 内容面板
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
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // ========== 1. 标题区域 ==========
        panel.add(createTitlePanel());
        panel.add(Box.createVerticalStrut(20));

        // ========== 2. 搜索和筛选栏 ==========
        panel.add(createSearchFilterPanel());
        panel.add(Box.createVerticalStrut(20));

        // ========== 3. 岗位列表区域 ==========
        jobListContainer = new JPanel();
        jobListContainer.setLayout(new BoxLayout(jobListContainer, BoxLayout.Y_AXIS));
        jobListContainer.setBackground(new Color(245, 246, 250));
        jobListContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        loadJobList(getSampleJobs());
        
        panel.add(jobListContainer);
        panel.add(Box.createVerticalStrut(20));

        // ========== 4. 底部按钮 ==========
        panel.add(createFooterPanel());

        return panel;
    }

    /**
     * 创建标题面板
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 246, 250));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("📋 Available TA Positions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Find and apply for teaching assistant positions");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(subtitleLabel);
        
        return panel;
    }

    /**
     * 创建搜索和筛选面板
     */
    private JPanel createSearchFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 搜索区域
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        searchField.setPreferredSize(new Dimension(300, 40));

        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setPreferredSize(new Dimension(100, 40));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> performSearch());

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        // 筛选区域
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterLabel.setForeground(new Color(85, 85, 85));

        filterCombo = new JComboBox<>(new String[]{"All Jobs", "Deadline Soon", "Recently Added", "Popular"});
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterCombo.setPreferredSize(new Dimension(150, 35));
        filterCombo.addActionListener(e -> applyFilter());

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);

        panel.add(searchPanel, BorderLayout.CENTER);
        panel.add(filterPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * 创建岗位卡片
     */
    private JPanel createJobCard(Job job) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 悬停效果
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(52, 152, 219), 2, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 230, 230), 1, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        // 左侧内容
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // 标题行
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(job.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));

        // 截止日期标签
        JLabel deadlineLabel = new JLabel("⏰ " + job.getDeadline());
        deadlineLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deadlineLabel.setForeground(new Color(231, 76, 60));
        deadlineLabel.setOpaque(true);
        deadlineLabel.setBackground(new Color(253, 242, 242));
        deadlineLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        titleRow.add(titleLabel, BorderLayout.WEST);
        titleRow.add(deadlineLabel, BorderLayout.EAST);

        // 课程信息
        JLabel courseLabel = new JLabel("📚 " + job.getCourseName());
        courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseLabel.setForeground(new Color(85, 85, 85));
        courseLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // 要求
        JLabel reqLabel = new JLabel("📝 Requirements: " + job.getRequirements());
        reqLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reqLabel.setForeground(new Color(102, 102, 102));

        // 描述
        JLabel descLabel = new JLabel(job.getDescription());
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(136, 136, 136));
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        contentPanel.add(titleRow);
        contentPanel.add(courseLabel);
        contentPanel.add(reqLabel);
        contentPanel.add(descLabel);

        // 右侧按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JButton detailsBtn = new JButton("View Details");
        detailsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsBtn.setForeground(new Color(52, 152, 219));
        detailsBtn.setBackground(Color.WHITE);
        detailsBtn.setFocusPainted(false);
        detailsBtn.setPreferredSize(new Dimension(120, 38));
        detailsBtn.setMaximumSize(new Dimension(120, 38));
        detailsBtn.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 1, true));
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsBtn.addActionListener(e -> showJobDetails(job));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(detailsBtn);
        buttonPanel.add(Box.createVerticalStrut(10));

        JButton applyBtn = new JButton("Apply Now");
        applyBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setBackground(new Color(39, 174, 96));
        applyBtn.setFocusPainted(false);
        applyBtn.setPreferredSize(new Dimension(120, 38));
        applyBtn.setMaximumSize(new Dimension(120, 38));
        applyBtn.setBorderPainted(false);
        applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyBtn.addActionListener(e -> applyForJob(job));

        buttonPanel.add(applyBtn);
        buttonPanel.add(Box.createVerticalGlue());

        card.add(contentPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 加载岗位列表
     */
    private void loadJobList(List<Job> jobs) {
        jobListContainer.removeAll();

        if (jobs.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(new Color(245, 246, 250));
            emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel emptyLabel = new JLabel("😕 No jobs found");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            emptyLabel.setForeground(new Color(149, 165, 166));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel hintLabel = new JLabel("Try adjusting your search or filter criteria");
            hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            hintLabel.setForeground(new Color(189, 195, 199));
            hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            emptyPanel.add(Box.createVerticalStrut(50));
            emptyPanel.add(emptyLabel);
            emptyPanel.add(Box.createVerticalStrut(10));
            emptyPanel.add(hintLabel);

            jobListContainer.add(emptyPanel);
        } else {
            for (Job job : jobs) {
                jobListContainer.add(createJobCard(job));
                jobListContainer.add(Box.createVerticalStrut(12));
            }
        }

        jobListContainer.revalidate();
        jobListContainer.repaint();
    }

    /**
     * 创建底部面板
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 246, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backBtn = new JButton("← Back to Dashboard");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(149, 165, 166));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setPreferredSize(new Dimension(180, 42));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> backToDashboard());

        JLabel countLabel = new JLabel("Showing all available positions");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(127, 140, 141));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(backBtn, BorderLayout.WEST);
        panel.add(countLabel, BorderLayout.CENTER);

        return panel;
    }

    // ========== 业务逻辑方法 ==========

    private void performSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        List<Job> allJobs = getSampleJobs();
        List<Job> filtered = new ArrayList<>();
        
        for (Job job : allJobs) {
            if (job.getTitle().toLowerCase().contains(keyword) ||
                job.getCourseName().toLowerCase().contains(keyword)) {
                filtered.add(job);
            }
        }
        
        loadJobList(filtered);
        System.out.println("Searching for: " + keyword);
    }

    private void applyFilter() {
        String filter = (String) filterCombo.getSelectedItem();
        List<Job> jobs = getSampleJobs();
        // 简化处理：仅展示效果
        loadJobList(jobs);
        System.out.println("Applying filter: " + filter);
    }

    private void showJobDetails(Job job) {
        JTextArea textArea = new JTextArea(
            "Job Title: " + job.getTitle() + "\n\n" +
            "Course: " + job.getCourseName() + "\n\n" +
            "Deadline: " + job.getDeadline() + "\n\n" +
            "Requirements: " + job.getRequirements() + "\n\n" +
            "Description: " + job.getDescription()
        );
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setBackground(new Color(248, 249, 250));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Job Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyForJob(Job job) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to apply for:\n" + job.getTitle() + "?",
            "Confirm Application",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Application submitted successfully!\nGood luck! 🎉",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Applied for job: " + job.getTitle());
        }
    }

    private void backToDashboard() {
        TADashboardUI dashboard = new TADashboardUI(username);
        dashboard.setVisible(true);
        this.dispose();
    }

    /**
     * 示例岗位数据
     */
    private List<Job> getSampleJobs() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("TA for EBU6304", "EBU6304 - Software Engineering", "2026-04-01", "Strong Java skills, OOP knowledge", "Assist in lab sessions, grade assignments, hold office hours"));
        jobs.add(new Job("TA for EBU6301", "EBU6301 - Data Structures", "2026-03-25", "Knowledge of algorithms and data structures", "Help students with assignments, conduct tutorials"));
        jobs.add(new Job("TA for EBU6305", "EBU6305 - Database Systems", "2026-04-15", "SQL experience, database design", "Assist in database labs, grade projects"));
        jobs.add(new Job("TA for EBU5201", "EBU5201 - Programming Fundamentals", "2026-03-30", "Basic programming skills, patience", "Help beginners with coding basics"));
        jobs.add(new Job("TA for EBU6302", "EBU6302 - Computer Networks", "2026-04-10", "Networking protocols knowledge", "Assist in network lab sessions"));
        return jobs;
    }
}
