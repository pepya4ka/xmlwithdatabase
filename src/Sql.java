import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashSet;

public class Sql {

    public static void createXMLDocumentWithSQL() {
        Connection con = null;//хранит соединение с БД
        Statement stmt = null;//хранит и выполняет SQL запросы
        ResultSet res = null;//получает результаты выполнения SQL запросов

        try {
            //Динамическая регистрация драйвера SQLite
            Driver d = (Driver) Class.forName("org.sqlite.JDBC").newInstance();

            //создание подключение к БД по пути, указанному в url
            String url = "jdbc:sqlite:c:\\DataBase.db";
            con = DriverManager.getConnection(url);

            //подготовка SQL запроса
            String sql = "SELECT * FROM Person";
            stmt = con.createStatement();

            //выполнение SQL запроса
            res = stmt.executeQuery(sql);

            //обработка результатов
            Person person;
            HashSet<Person> people = new HashSet<>();
            while (res.next()) {
                person = new Person();
                person.DepCode = res.getString("DepCode");
                person.DepJob = res.getString("DepJob");
                person.Description = res.getString("Description");
//                System.out.println(res.getString("id") + ", " + res.getObject("DepJob"));
                people.add(person);
            }

            Xml.createXMLDocument(people);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) res.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void updateDataBase(HashSet<Person> hashSet) {
        Connection con = null;//хранит соединение с БД
        Statement stmt = null;//хранит и выполняет SQL запросы
        ResultSet res = null;//получает результаты выполнения SQL запросов

        //Динамическая регистрация драйвера SQLite
        try {
            Driver d = (Driver) Class.forName("org.sqlite.JDBC").newInstance();

            //создание подключение к БД по пути, указанному в url
            String url = "jdbc:sqlite:d:\\DataBase.db";
            con = DriverManager.getConnection(url);

            Person[] people;
            people = hashSet.toArray(new Person[hashSet.size()]);

            for (int i = 0; i < people.length; i++) {
                String sql = "SELECT * FROM Person";
                stmt = con.createStatement();
                res = stmt.executeQuery(sql);

                while (res.next()) {
                    if (!(res.getString("DepCode").equals(people[i].DepCode)
                            && res.getString("DepJob").equals(people[i].DepJob))) {
                        sql = "DELETE FROM Person WHERE DepCode = '"
                                + res.getString("DepCode") + "' AND DepJob = '"
                                + res.getString("DepJob") + "'";
                        stmt = con.createStatement();
                        stmt.executeUpdate(sql);
                    }
                }
            }

            for (int i = 0; i < people.length; i++) {

                String sql = "SELECT * FROM Person WHERE DepCode = '" + people[i].DepCode
                        + "' AND DepJob = '" + people[i].DepJob + "'";

                stmt = con.createStatement();
                res = stmt.executeQuery(sql);

                if (res.next()) {
                    sql = "UPDATE Person" +
                            "SET Description = '" + people[i].Description + "'" +
                            "WHERE Person.DepCode = '" + people[i].DepCode + "'" +
                            "AND Person.DepJob = '" + people[i].DepJob + "'";
                    stmt = con.createStatement();
                    res = stmt.executeQuery(sql);
                } else {
                    sql = "INSERT INTO Person (DepCode, DepJob, Description) " +
                            "values('" + people[i].DepCode + "', '" + people[i].DepJob + "', '" + people[i].Description + "')";
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                }
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
