package webapp;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class App {
    static int count = 0;

    public static void main(String[] args) {

        getHerokuAssignedPort();

        Map<String, Object> map = new HashMap<>();
        List<String> greetings = new ArrayList<>();


        get("/greet", (req, res) -> new ModelAndView(map, "hello.handlebars"),
                new HandlebarsTemplateEngine());


        post("/hello", (req, res) -> {

            // create the greeting message
            String name = req.queryParams("username");
            String lang = req.queryParams("lang");
            // add names to ArrayList
            greetings.add(name);
            //It remove emptyString to arrayList
            greetings.remove("");

            if (!lang.equals("") && !name.equals("")) {
                String greet = "";
                // create the greeting message
                switch (lang) {
                    case "English":
                        greet = "Hello " + name;
                        break;
                    case "Afrikaans":
                        greet = "More " + name;
                        break;
                    case "IsiXhosa":
                        greet = "Molweni " + name;
                        break;
                }
                count++;

                //Remove duplicates from ArrayList
                int current = 0;
                while (current < greetings.size()) {
                    int j = 0;
                    boolean isRemoved = false;
                    while (j < current) {
                        if (greetings.get(current).equals(greetings.get(j))) {
                            greetings.remove(current);
                            isRemoved = true;
                            break;
                        } else j++;
                    }
                    if (!isRemoved) current++;
                }
                // put it in the map which is passed to the template - the value will be merged into the template
                map.put("greeting", greet);
                map.put("count", count);
                map.put("language", lang);
                map.put("user", greetings);
            } else {
                map.put("greeting", "Enter name and select language.");
            }
            res.redirect("/greet");
            return new ModelAndView(map, "hello.handlebars");

        }, new HandlebarsTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
