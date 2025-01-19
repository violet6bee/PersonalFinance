package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private final String POSTGRES_URL;

    public Database(String postgresUrl) {
        POSTGRES_URL = postgresUrl;
    }

    public boolean initDatabase() {
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            Statement st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS public.\"user\" (\n" +
                    "\tid int GENERATED ALWAYS AS IDENTITY NOT NULL,\n" +
                    "\tlogin varchar NOT NULL,\n" +
                    "\tpassword varchar NOT NULL,\n" +
                    "\tCONSTRAINT user_pk PRIMARY KEY (id),\n" +
                    "\tCONSTRAINT user_unique UNIQUE (login)\n" +
                    ");");

            st.execute("CREATE TABLE IF NOT EXISTS public.wallet (\n" +
                    "\tid int GENERATED ALWAYS AS IDENTITY NOT NULL,\n" +
                    "\tiduser int NOT NULL,\n" +
                    "\tCONSTRAINT wallet_pk PRIMARY KEY (id),\n" +
                    "\tCONSTRAINT wallet_user_fk FOREIGN KEY (id) REFERENCES public.\"user\"(id) ON DELETE CASCADE\n" +
                    ");");

            st.execute("CREATE TABLE IF NOT EXISTS public.category (\n" +
                    "\tid int GENERATED ALWAYS AS IDENTITY NOT NULL,\n" +
                    "\t\"name\" varchar NOT NULL,\n" +
                    "\tCONSTRAINT category_pk PRIMARY KEY (id)\n" +
                    ");");

            st.execute("CREATE TABLE IF NOT EXISTS public.\"transaction\" (\n" +
                    "\tid int GENERATED ALWAYS AS IDENTITY NOT NULL,\n" +
                    "\t\"type\" varchar NOT NULL,\n" +
                    "\tsum int NOT NULL,\n" +
                    "\tcategoryid int4 NOT NULL,\n" +
                    "\twallet int NOT NULL,\n" +
                    "\tCONSTRAINT transaction_pk PRIMARY KEY (id)\n" +
                    ");");

            st.execute("CREATE TABLE IF NOT EXISTS public.budget (\n" +
                    "\tid int GENERATED ALWAYS AS IDENTITY NOT NULL,\n" +
                    "\tidcategory int NOT NULL,\n" +
                    "\tiduser int NOT NULL,\n" +
                    "\tbudget int NOT NULL,\n" +
                    "\tCONSTRAINT budget_pk PRIMARY KEY (id)\n" +
                    ");");

            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Не удалась соединиться с базой данных");
            return false;
        }
        return true;
    }

    public int createUser(String password, String login) {
        int id;
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "INSERT INTO public.\"user\"\n" +
                    "(login, password)\n" +
                    "VALUES(?, ?) RETURNING id;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            id = 0;
        }
        return id;
    }

    public int getUSer(String password, String login) {
        int id;
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "SELECT id, login, \"password\"\n" +
                    "FROM public.\"user\"\n" +
                    "where login = ? and \"password\" = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            id = 0;
        }
        return id;
    }

    public int createCategory(String nameCategory) {
        int id;
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "INSERT INTO public.category\n" +
                    "(\"name\")\n" +
                    "VALUES(?) RETURNING id;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nameCategory);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            id = 0;
        }
        return id;
    }

    public int createWallet(int userId) {
        int id;
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "INSERT INTO public.wallet\n" +
                    "(\"iduser\")\n" +
                    "VALUES(?) RETURNING id;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            id = 0;
        }
        return id;
    }

    public int createTransaction(String type, int sum, int walletId, int categoryId) {
        int id;
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "INSERT INTO public.\"transaction\"\n" +
                    "(\"type\", sum, wallet, categoryid)\n" +
                    "VALUES(?, ?, ?, ?) RETURNING id;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, sum);
            preparedStatement.setInt(3, walletId);
            preparedStatement.setInt(4, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            id = 0;
        }
        return id;
    }

    public int createBudget(int categoryId, int userId, int budget) {
        int id;
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "INSERT INTO public.budget\n" +
                    "(idcategory, iduser, budget)\n" +
                    "VALUES(?, ?, ?) RETURNING id;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, budget);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            } else {
                id = 0;
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            id = 0;
        }
        return id;
    }

    public ArrayList<Category> getCategories(int userId) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(POSTGRES_URL);
            String sql = "select c.\"name\", t.\"type\", b.budget, sum(t.sum)\n" +
                    "from \"transaction\" t \n" +
                    "join category c on t.categoryid = c.id \n" +
                    "join wallet w on t.wallet = w.id\n" +
                    "left join budget b on c.id = b.idcategory\n" +
                    "where w.iduser = ?\n" +
                    "group by c.\"name\", t.\"type\", b.budget ;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var category = new Category(resultSet.getInt(4), resultSet.getString(1),
                        resultSet.getString(2), resultSet.getInt(3));
                categories.add(category);
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException _) {
        }
        return categories;
    }

}
