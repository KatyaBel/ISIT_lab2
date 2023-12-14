import org.json.simple.JSONArray;
import javax.swing.*;

public class ResultForm {
    private JFrame frame;
    private JPanel panel;
    private JLabel tarif;
    private JLabel way;
    public ResultForm(JSONArray facts)  {
        frame = new JFrame("Результат");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        StringBuilder facts_res = new StringBuilder();
        facts_res.append("<html>Факты:<br/>");
        for (int i = 0; i < facts.size(); i++) {
            if (i != facts.size() - 1) {
                facts_res.append(facts.get(i)).append("<br/>");
            } else {
                String res = facts.get(i).toString().split(":")[1];
                res = translate(res.substring(1, res.length()-2));
                tarif.setText("Тариф: "+res);
            }
        }
        facts_res.append("</html>");
        way.setText(String.valueOf(facts_res));
    }
    private String translate(String word) {
        word = word.replace("per-minute", "Поминутный");
        word = word.replace("beneficial", "Выгодный");
        word = word.replace("standart", "Стандарт");
        word = word.replace("on touch", "На связи");
        word = word.replace("message", "Месседж");
        word = word.replace("premium", "Премиум");
        word = word.replace("minutes", "минут");
        word = word.replace("sms", "смс");
        return word;
    }
}