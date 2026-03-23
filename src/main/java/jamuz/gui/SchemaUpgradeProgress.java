/*
 * Copyright (C) 2026 phramusca / contributors
 *
 * Non-modal progress dialog during early DB schema upgrade (before main window).
 */
package jamuz.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * Small indeterminate progress UI so the app does not look frozen during schema upgrade.
 */
public final class SchemaUpgradeProgress {

    private JDialog dialog;
    private JLabel status;

    private SchemaUpgradeProgress() {
    }

    /**
     * Shows a non-modal dialog on the EDT. No-op if headless.
     */
    public static SchemaUpgradeProgress showOnEdt() {
        if (GraphicsEnvironment.isHeadless()) {
            return new SchemaUpgradeProgress();
        }
        final SchemaUpgradeProgress[] ref = new SchemaUpgradeProgress[1];
        try {
            SwingUtilities.invokeAndWait(() -> {
                SchemaUpgradeProgress ui = new SchemaUpgradeProgress();
                ref[0] = ui;
                ui.dialog = new JDialog((java.awt.Frame) null, "JaMuz", false);
                ui.dialog.setLayout(new BorderLayout(8, 8));
                JProgressBar bar = new JProgressBar();
                bar.setIndeterminate(true);
                ui.status = new JLabel(" ");
                ui.status.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8));
                bar.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 0, 8));
                ui.dialog.add(bar, BorderLayout.NORTH);
                ui.dialog.add(ui.status, BorderLayout.CENTER);
                ui.dialog.pack();
                ui.dialog.setLocationRelativeTo(null);
                ui.dialog.setAlwaysOnTop(true);
                ui.dialog.setVisible(true);
            });
        } catch (Exception ex) {
            return new SchemaUpgradeProgress();
        }
        return ref[0] != null ? ref[0] : new SchemaUpgradeProgress();
    }

    public void setMessage(final String msg) {
        if (status != null) {
            SwingUtilities.invokeLater(() -> status.setText(msg == null ? " " : msg));
        }
    }

    public void dispose() {
        if (dialog != null) {
            SwingUtilities.invokeLater(() -> {
                dialog.dispose();
                dialog = null;
                status = null;
            });
        }
    }
}
