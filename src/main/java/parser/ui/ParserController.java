package parser.ui;

import parser.utils.YandexMapParsing;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ParserController {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Парсер яндекс карты");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 100);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("что ищем?");
        JLabel warning = new JLabel();
        JTextField textField = new JTextField(10);
        JButton search = new JButton("Искать");
        panel.add(warning);
        panel.add(label);
        panel.add(textField);
        panel.add(search);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);

        search.addActionListener((x) -> {
            search.setVisible(false);
            if (Objects.isNull(textField.getText()) || textField.getText().isEmpty()) {
                search.setVisible(true);
                warning.setText("Поле пусто,");
                return;
            }

            try {
                if (YandexMapParsing.parsing(textField.getText())) {
                    warning.setText("Поиск завершен успешно,");
                    textField.setText("");
                } else {
                    warning.setText("Во время поиска произошла ошибка,");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            search.setVisible(true);
        });
    }

}
