import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;


public class dbAndMenu {
    Scanner scanner = new Scanner(System.in);


    public static int printMenu() {
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
            try {
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
                            statement.executeUpdate(insertData(statement));
                        } catch (Exception E) {
                            System.out.println("Connection failed...");
                            E.printStackTrace();
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
            catch (Exception typeEx){
                typeEx.printStackTrace();
            }
        }
    }

    public static String insertData(Statement statement) {
        Scanner scanner1 = new Scanner(System.in);
        try {
            //Выбирается база данных menu, после выводится название всех таблиц и предлагается выбрать одну
            ResultSet resultSet = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = \"menu\"");
            System.out.println("Введите название таблицы из представленных для добавления данных: ");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("TABLE_NAME"));
            }
            String name = scanner1.nextLine();

            //Количество вводимых строк еще не до конца реализовано, и возможно не будет <3
            //System.out.print("Введите количество вводимых строк: ");
            //int amo = scanner1.nextInt();//Вводится кол-во строк, неплохо было бы сделать цикл, когда код снизу будет функционировать

            StringBuilder insertStatement = new StringBuilder("INSERT " + name + " (");
            String var;
            StringBuilder temp = new StringBuilder();
            resultSet = statement.executeQuery("SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + name + "' AND COLUMN_NAME != 'Id'");
            while (resultSet.next()) {
                System.out.print("Введите значение типа " + resultSet.getString(2) + " в столбец " + resultSet.getString(1) + ": ");
                if (resultSet.getString(2).contains("char")) { //если поле из запроса содержит в себе подстроку char,
                    var = "'" + scanner1.nextLine() + "'";              //то вокруг него ставятся кавычки, чтобы запрос выполнился
                } else {
                    var = scanner1.nextLine();
                }
                temp.append(var);
                insertStatement.append(resultSet.getString(1));
                if (resultSet.isLast()) {
                    insertStatement.append(") VALUES (").append(temp).append(")");
                } else {
                    insertStatement.append(", ");
                    temp.append(", ");
                }
            }
            return String.valueOf(insertStatement);
        } catch (Exception sq) {
            sq.printStackTrace();
        }
        return ("Error");
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
