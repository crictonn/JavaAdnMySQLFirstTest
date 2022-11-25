import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;


public class dbAndMenu {
    Scanner scanner = new Scanner(System.in);
    public static int printMenu(){
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("1. Создание таблицы");
        System.out.println("2. Добавление данных");
        System.out.println("3. Изменение данных");
        System.out.println("4. Удаление данных");
        System.out.println("5. Выход");
        System.out.println("Выбор: ");
        int choice = scanner1.nextInt();
        return choice;
    }

    public static String createTable(Connection conn){
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("Введите название таблицы: ");
        String name = scanner1.nextLine();
        String sqlRequest = "CREATE TABLE " + name + " (Id INT PRIMARY KEY AUTO_INCREMENT, ";
        System.out.print("Введите количество столбцов: ");
        int colAmount = scanner1.nextInt();
        for (int i = 0; i < colAmount; i++) {
            System.out.println("Введите название столбца: ");
            scanner1.nextLine();
            String colName = scanner1.nextLine();
            System.out.println("Выберите тип данных: ");
            System.out.print("1. int\n" +
                    "2. float\n" +
                    "3. nvarchar\n");
            int ch1 = scanner1.nextInt();
            String colType = "";
            switch (ch1){
                case 1: colType = " INT"; break;
                case 2: colType = " FLOAT"; break;
                case 3: colType = " VARCHAR(20)"; break;

            }
            sqlRequest = sqlRequest + colName + colType;
            if (i == colAmount-1){
                sqlRequest = sqlRequest + ")";
            }
            else {
                sqlRequest = sqlRequest + ", ";
            }
        }
        return sqlRequest;
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Connection successful");
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }



        boolean TR = true;
        while (TR) {
            switch (printMenu()) {
                case 1:
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                        try (Connection conn = getConnection()) {
                            Statement statement = conn.createStatement();
                            statement.executeUpdate(createTable(conn));
                        }
                    } catch (Exception ex) {
                        System.out.println("Connection failed...");

                        System.out.println(ex);
                    }
                    break;
                case 2:
                    System.out.println("Case 2 worked");
                    break;
                case 3:
                    System.out.println("Case 3 worked");
                    break;
                case 4:
                    System.out.println("Case 4 worked");
                    break;
                case 5:
                    TR = false;
            }

        }
    }

    public static Connection getConnection() throws SQLException, IOException {

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }
}
