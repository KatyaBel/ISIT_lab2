import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Choice {

    //создаем массив facts
    JSONArray facts = new JSONArray();

    //создаем список вопросов
    ArrayList<String> questions = new ArrayList<>(Arrays.asList
            ("Вы разговариваете по телефону каждый день? да/нет",
                    "Вы долго говорите по телефону? да/нет",
                    "Вы общаетесь через смс? да/нет",
                    "Как часто вы пишите смс? редко/иногда/часто"));

    public Choice() throws IOException {
        //очищаем файл facts
        FileWriter fw = new FileWriter("src/facts.json");
        fw.write("");
        fw.close();

        //открываем форму с 1 вопросом
        new MainForm(this, 0);
    }

    public void getResult() throws IOException, ParseException {
        //запись массива в файл
        JSONObject j_all_facts = new JSONObject();
        j_all_facts.put("facts", facts);
        FileWriter fw = new FileWriter("src/facts.json");
        fw.write(String.valueOf(j_all_facts));
        fw.close();
        fw.close();

        //из файла rules запись в массив rules
        FileReader fr = new FileReader("src/rules.json");
        Object all_rules = new JSONParser().parse(fr);
        fr.close();
        JSONObject j_all_rules = (JSONObject) all_rules;
        JSONArray rules = (JSONArray) j_all_rules.get("rules");

        //начинаем цикл
        boolean suit_rule;
        do {
            suit_rule = false;

            //из файла facts запись в cur_facts
            fr = new FileReader("src/facts.json");
            Object all_facts = new JSONParser().parse(fr);
            fr.close();
            JSONObject j_all_cur_facts = (JSONObject) all_facts;
            JSONArray cur_facts = (JSONArray) j_all_cur_facts.get("facts");

            //проходимся по правилам
            for (int i = 0; i < rules.size(); i++) {
                //из файла facts запись в facts
                j_all_facts = (JSONObject) rules.get(i);
                facts = (JSONArray) j_all_facts.get("facts");
                int count = 0;

                //проходимся по фактам правила
                for (int j = 0; j < facts.size(); j++) {
                    count += isExist(cur_facts, (JSONObject) facts.get(j));
                }
                if (count == facts.size()) {
                    JSONArray dependencies = (JSONArray) j_all_facts.get("dependencies");
                    if (isExist(cur_facts, (JSONObject) dependencies.get(0)) == 0) {

                        //проверка, есть ли per min в файле facts
                        HashMap map = new HashMap<>();
                        map.put("tarif", "per-minute");

                        //если НЕ (есть и пытаемся записать ben)
                        if (!(Objects.equals(dependencies.get(0).toString(), "{\"tarif\":\"beneficial\"}") && isExist(cur_facts, new JSONObject(map)) == 1)) {
                            suit_rule = true;

                            //запись нового факта
                            cur_facts.add(dependencies.get(0));
                            j_all_cur_facts.put("facts", cur_facts);

                            fw = new FileWriter("src/facts.json");
                            fw.write(String.valueOf(j_all_cur_facts));
                            fw.close();
                        }
                    }
                }
            }
        } while (suit_rule);

        //вывод результата
        fr = new FileReader("src/facts.json");
        Object all_facts = new JSONParser().parse(fr);
        fr.close();
        j_all_facts = (JSONObject) all_facts;
        facts = (JSONArray) j_all_facts.get("facts");
        new ResultForm(facts);
    }

    public static int isExist(JSONArray ja, JSONObject jo) {
        if (ja.toString().contains(jo.toString())) {
            return 1;
        } else {
            return 0;
        }
    }
}
