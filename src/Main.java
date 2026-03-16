

import ui.TADashboardUI;

import javax.swing.*;

/**
 * TA Recruitment System - Main Entry (Swing Version)
 * 4号成员负责：TA端核心页面
 */
public class Main {

    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 在事件调度线程中启动GUI
        SwingUtilities.invokeLater(() -> {
            TADashboardUI dashboard = new TADashboardUI("TA User");
            dashboard.setVisible(true);
        });
    }
}
