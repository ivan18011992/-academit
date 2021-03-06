package academit.kashirin.view;

import academit.kashirin.TemperatureView;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class View implements TemperatureView {
    private JTextField input = new JTextField("", 1);
    private String[] items = {"Цельсий", "Кельвин", "Фаренгейт"};
    private JComboBox<String> comboBoxInput = new JComboBox<>(items);
    private JComboBox<String> comboBoxOutput = new JComboBox<>(items);
    private JLabel output = new JLabel("");
    private ActionListener controller;

    public View() {
    }

    @Override
    public Double getInputTemperature() {
        try {
            return Double.parseDouble(input.getText());
        } catch (NumberFormatException e) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Введите корректные данные", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public void setOutputTemperature(Double temperature) {
        if (temperature == null) {
            output.setText("");
        } else {
            output.setText(Double.toString(temperature));
        }
    }

    @Override
    public int getInputScale() {
        return comboBoxInput.getSelectedIndex();
    }

    @Override
    public int getOutputScale() {
        return comboBoxOutput.getSelectedIndex();
    }

    @Override
    public void setController(ActionListener controller) {
        this.controller = controller;
    }

    public void initialize() {
        SwingUtilities.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = 900;
            int height = 100;
            int locationX = (screenSize.width - width) / 2;
            int locationY = (screenSize.height - height) / 2;

            JFrame frame = new JFrame("Перевод температуры");
            frame.setBounds(locationX, locationY, width, height);
            frame.setMinimumSize(new Dimension(900, 100));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Container container = frame.getContentPane();
            container.setLayout(new GridLayout(0, 3));
            JLabel labelInput = new JLabel("Введите значение температуры:");
            container.add(labelInput);
            container.add(input);
            container.add(comboBoxInput);
            JLabel labelOutput = new JLabel("Конвертированное значение температуры:");
            container.add(labelOutput);
            container.add(output);
            container.add(comboBoxOutput);
            JButton button = new JButton("Расчет");
            button.addActionListener(controller);
            frame.add(button);

            frame.setVisible(true);
        });
    }
}