package jamuz.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PasswordFieldWithToggle extends JPanel {
    private JPasswordField passwordField;
    private JToggleButton toggleButton;

    public PasswordFieldWithToggle() {
        setLayout(new GridBagLayout()); // Use GridBagLayout for flexibility

        passwordField = new JPasswordField();
        toggleButton = new JToggleButton("Show");

        toggleButton.addActionListener((ActionEvent e) -> {
            if (toggleButton.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show the password
                toggleButton.setText("Hide");
            } else {
                passwordField.setEchoChar('*'); // Hide the password
                toggleButton.setText("Show");
            }
        });

        passwordField.setEchoChar('*'); // Initialize as hidden

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Make the passwordField expandable in width

        add(passwordField, gbc);
        add(toggleButton);
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void setPassword(char[] password) {
        passwordField.setText(new String(password));
    }

    public void setToolTipText(String text) {
        toggleButton.setToolTipText(text);
        passwordField.setToolTipText(text);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Password Field With Toggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);

        PasswordFieldWithToggle passwordFieldWithToggle = new PasswordFieldWithToggle();
        frame.add(passwordFieldWithToggle);

        frame.setVisible(true);
    }

    public void setText(String text) {
        passwordField.setText(text);
    }

    public String getText() {
        return new String(passwordField.getPassword());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        toggleButton.setEnabled(enabled);
    }
    
    
}
