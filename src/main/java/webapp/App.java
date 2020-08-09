package webapp;

//import org.jdbi.v3.core.Handle;
//import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class App {
    static int count = 0;

    public static void main(String[] args) {

        port(getHerokuAssignedPort());

        Map<String, Object> map = new HashMap<>();
        List<String> greetings = new ArrayList<>();
//        String dbDiskURL = "jdbc:h2:file:./greetdb";
//        String dbMemoryURL = "jdbc:h2:mem:greetdb";
//
//        Jdbi jdbi = Jdbi.create(dbDiskURL, "sa", "");
//
//        // get a handle to the database
//        Handle handle = jdbi.open();
//
//       // create the table if needed
//        handle.execute("create table if not exists greet ( id integer identity, name varchar(50), counter int )");
//
//        // add a user to the database
//        handle.execute("insert into greet (name) values (?)", username);
//
//// get all the usernames from the database
//        List<String> names = handle.createQuery("select name from greet")
//                .mapTo(String.class)
//                .list();
//
//// get a count of all the users in the database
//        int count = handle.select("select count(*) from greet")
//                .mapTo(int.class)
//                .findOnly();
//
//
//// check if a user exists
//        int counts = handle.select("select count(*) from greet where name = ?", userName)
//                .mapTo(int.class)
//                .findOnly();

//         get("greeted/:username", (req, res) ->new ModelAndView(map, "hello.handlebars"),
//                 new HandlebarsTemplateEngine());

        get("/", (req, res) -> new ModelAndView(map, "hello.handlebars"),
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
                map.put("userName", greetings);
            } else {
                map.put("greeting", "Enter name and select language.");
            }
            res.redirect("/");
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
