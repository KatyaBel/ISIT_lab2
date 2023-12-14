import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class MainForm {
    private JFrame frame;
    private JPanel panel;
    private JLabel question;
    private JTextField answer;
    private JButton ok;

    public MainForm(Choice data, int index)  {
        frame = new JFrame("Система выбора тарифа");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 180);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        question.setText(data.questions.get(index));
        answer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        ok.setFocusPainted(false);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //чтение ответов, запись в facts
                JSONObject fact = new JSONObject();
                switch (index) {
                    case 0:
                        if (Objects.equals(answer.getText(), "да")) {
                            fact.put("often_talk", "yes");
                        } else {
                            fact.put("often_talk", "no");
                        }
                        data.facts.add(fact);
                        frame.dispose();
                        new MainForm(data, 1);
                        break;
                    case 1:
                        if (Objects.equals(answer.getText(), "да")) {
                            fact.put("long_talk", "yes");
                        } else {
                            fact.put("long_talk", "no");
                        }
                        data.facts.add(fact);
                        frame.dispose();
                        new MainForm(data, 2);
                        break;
                    case 2:
                        if (Objects.equals(answer.getText(), "да")) {
                            fact.put("comm", "yes");
                            data.facts.add(fact);
                            frame.dispose();
                            new MainForm(data, 3);
                        } else {
                            fact.put("comm", "no");
                            data.facts.add(fact);
                            frame.dispose();
                            try {
                                data.getResult();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (ParseException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        break;
                    case 3:
                        if (Objects.equals(answer.getText(), "часто")) {
                            fact.put("often_sms", "often");
                        } else if (Objects.equals(answer.getText(), "иногда")) {
                            fact.put("often_sms", "sometimes");
                        } else {
                            fact.put("often_sms", "rarely");
                        }
                        data.facts.add(fact);
                        frame.dispose();
                        try {
                            data.getResult();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }
            }
        });
    }
}