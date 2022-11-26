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
        return scanner1.nextInt();
    }

    public static String createTable(Connection conn){
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("Введите название таблицы: ");
        String name = scanner1.nextLine();
        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE " + name + " (Id INT PRIMARY KEY AUTO_INCREMENT, ");
        System.out.print("Введите количество столбцов: ");
        int colAmount = scanner1.nextInt();
        for (int i = 0; i < colAmount; i++) {
            System.out.println("Введите название столбца: ");
            scanner1.nextLine();
            String colName = scanner1.nextLine();
            System.out.println("Выберите тип данных: ");
            System.out.print("""
                    1. int
                    2. float
                    3. nvarchar
                    """);
            int ch1 = scanner1.nextInt();
            String colType = switch (ch1) {
                case 1 -> " INT";
                case 2 -> " FLOAT";
                case 3 -> " VARCHAR(20)";
                default -> "";
            };
            sqlRequest.append(colName).append(colType);
            if (i == colAmount-1){
                sqlRequest.append(")");
            }
            else {
                sqlRequest.append(", ");
            }
        }
        return sqlRequest.toString();
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Connection successful");
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
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
                    }
                    break;
                case 2:
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                        Connection conn = getConnection();
                        Statement statement = conn.createStatement();
                        System.out.println(insertData(conn, statement));
                    }
                    catch (Exception E){
                        System.out.println("Connection failed...");
                        System.out.println(E);
                    }
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

    public static String insertData(Connection conn, Statement statement) {
        Scanner scanner1 = new Scanner(System.in);
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = \"menu\"");
            System.out.println("Введите название таблицы из представленных для добавления данных: ");
            while(resultSet.next()){
                System.out.println(resultSet.getString("TABLE_NAME"));
            }
            String name = scanner1.nextLine();


            resultSet = statement.executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME LIKE '" + name +"'");
            int counter = 0;
            while(resultSet.next())
                counter++;
            System.out.print("Введите количество вводимых строк: ");
            int amo = scanner1.nextInt();
            resultSet = statement.executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + name + "'");
            for(int i = 0; i<amo;i++){

                System.out.print("Введите значение в столбец " + resultSet.getString(4));

            }
            statement.executeUpdate("INSERT cum (qwe, rty, uio) VALUES (1215 , 2.1, 'finish')");
            return String.valueOf(counter);
        }
        catch (Exception sq){
            System.out.println(sq);
        }
        return("Error");
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
