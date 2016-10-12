package com.epam.carrental.gui.view.hanlders;

import javax.swing.*;
import java.awt.*;

public interface  UserInputHandler {

    void saveInput();

    default JPanel prepareInputPanel(JTextField field, String name) {
        JLabel label = new JLabel(name);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
}
