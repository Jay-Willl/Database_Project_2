package com.example.project_x.apis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Top_2 {
    static String url = "jdbc:postgresql://localhost:5432/project2";
    static String username = "checker";
    static String password = "123456";
    static Connection connection = null;
    static ResultSet resultSet;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("If you want to import data");
            System.out.println("please enter 1");
            int go = in.nextInt();
            if (go == 1) {
                truncate();
                initialize();
                stockIn();
                placeOrder();
                updateOrder();
                deleteOrder();
            }
            centerInsert(9,"Sustech?");
            System.out.println(centerSelect());
            centerUpdate(9,"SusTech!");
            System.out.println(centerSelect());
            centerDelete(9);
            System.out.println(centerSelect());
            System.out.println("------getAllStaffCount------");

            System.out.println(getAllStaffCount());
            System.out.println("------getContractCount------");

            System.out.println(getContractCount());
            System.out.println("------getOrderCount------");

            System.out.println(getOrderCount());
            System.out.println("------getNeverSoldProductCount------");

            System.out.println(getNeverSoldProductCount());
            System.out.println("------getFavoriteProductModel------");
            System.out.println(getFavoriteProductModel());
            System.out.println("------getProductByNumber------");
            String product_code = "";
            //product_code = in.next();
            //System.out.println(getProductByNumber(product_code));
            System.out.println("------getContractInfo------");
            String contract_number = "";
            //contract_number = in.next();
            //System.out.println(getContractInfo(contract_number));
            System.out.println("------getAvgStockByCenter------");

            System.out.println(getAvgStockByCenter());

            System.out.println("------getOrderListByThree------");
            String contract_number2 = "";
            //contract_number2 = in.next();
            System.out.println(getOrderListByThree("CSE0000101",11411706,"PhotoBox60"));
            System.out.println("------getOrderListBySalesman------");
            //int num = in.nextInt();
            //System.out.println(getOrderListBySalesman(num));
            System.out.println("------getOrderListByTwo------");
            //String model = in.next();
            System.out.println(getOrderListByTwo("CSE0000101", 11411706));
            System.out.println("------getBestSalesman------");
            System.out.println(getBestSalesman());
            System.out.println("------getMVPModel------");
            System.out.println(getMVPModel());
            System.out.println("------getProfits------");
            System.out.println(getPureProfits());
            System.out.println("------getBill------");
            System.out.println(getBill());

        } catch (IllegalArgumentException | SQLException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public static void initialize() throws SQLException, IOException {
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        String center = "insert into center(center_id, supply_center, director_number) values (1, 'America', 11311024); " +
                "insert into center(center_id, supply_center, director_number) values (2, 'Eastern China', 11812905); " +
                "insert into center(center_id, supply_center, director_number) values (3, 'Asia', 11510024); " +
                "insert into center(center_id, supply_center, director_number) values (4, 'Southern China', 11710914); " +
                "insert into center(center_id, supply_center, director_number) values (5, 'Northern China', 11511203); " +
                "insert into center(center_id, supply_center, director_number) values (6, 'Europe', 11710621); " +
                "insert into center(center_id, supply_center, director_number) values (7, 'Southwestern China', 12010201); " +
                "insert into center(center_id, supply_center, director_number) values (8, '\"Hong Kong, Macao and Taiwan regions of China\"', 11311624);";
        Statement statement = connection.createStatement();
        statement.execute(center);
        connection.commit();

        HashMap<String, Integer> lookup = new HashMap<>();
        lookup.put("America", 1);
        lookup.put("Eastern China", 2);
        lookup.put("Asia", 3);
        lookup.put("Southern China", 4);
        lookup.put("Northern China", 5);
        lookup.put("Europe", 6);
        lookup.put("Southwestern China", 7);
        lookup.put("\"Hong Kong, Macao and Taiwan regions of China\"", 8);
        String line;
        String[] parts;

        // enterprise
        PreparedStatement preparedStatementEnterprise = connection.prepareStatement(
                "insert into enterprise(enterprise_id, client_enterprise, country, city, center_id, industry)"
                        + " values(?,?,?,?,?,?)");
        BufferedReader infileEnterprise = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\enterprise.csv"));
        infileEnterprise.skip(44);
        while((line = infileEnterprise.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                if(!Objects.equals(parts[4], "\"Hong Kong")){
                    preparedStatementEnterprise.setInt(1, Integer.parseInt(parts[0]));
                    preparedStatementEnterprise.setString(2, parts[1]);
                    preparedStatementEnterprise.setString(3, parts[2]);
                    preparedStatementEnterprise.setString(4, parts[3]);
                    preparedStatementEnterprise.setInt(5, lookup.get(parts[4]));
                    preparedStatementEnterprise.setString(6, parts[5]);
                    preparedStatementEnterprise.addBatch();
                }else{
                    preparedStatementEnterprise.setInt(1, Integer.parseInt(parts[0]));
                    preparedStatementEnterprise.setString(2, parts[1]);
                    preparedStatementEnterprise.setString(3, parts[2]);
                    preparedStatementEnterprise.setString(4, parts[3]);
                    preparedStatementEnterprise.setInt(5, lookup.get(parts[4] + "," + parts[5]));
                    preparedStatementEnterprise.setString(6, parts[6]);
                    preparedStatementEnterprise.addBatch();
                }
            }
        }
        preparedStatementEnterprise.executeBatch();
        preparedStatementEnterprise.close();

        // model
        PreparedStatement preparedStatementModel = connection.prepareStatement(
                "insert into model(model_id, product_code, product_model, product_name, unit_price)"
                        + "values(?,?,?,?,?)");
        BufferedReader infileModel = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\model.csv"));
        infileModel.skip(32);
        while((line = infileModel.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                preparedStatementModel.setInt(1, Integer.parseInt(parts[0]));
                preparedStatementModel.setString(2, parts[1]);
                preparedStatementModel.setString(3, parts[2]);
                preparedStatementModel.setString(4, parts[3]);
                preparedStatementModel.setInt(5, Integer.parseInt(parts[4]));
                preparedStatementModel.addBatch();
            }
        }
        preparedStatementModel.executeBatch();
        preparedStatementModel.close();

        // staff
        PreparedStatement preparedStatementStaff = connection.prepareStatement(
                "insert into staff(staff_id, staff_number, staff_name, gender, age, mobile_phone, center_id, type)"
                        + "values(?,?,?,?,?,?,?,?)");
        BufferedReader infileStaff = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\staff.csv"));
        infileStaff.skip(59);
        while((line = infileStaff.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                if(!Objects.equals(parts[5], "\"Hong Kong")){
                    preparedStatementStaff.setInt(1, Integer.parseInt(parts[0]));
                    preparedStatementStaff.setInt(2, Integer.parseInt(parts[4]));
                    preparedStatementStaff.setString(3, parts[1]);
                    preparedStatementStaff.setString(4, parts[3]);
                    preparedStatementStaff.setInt(5, Integer.parseInt(parts[2]));
                    preparedStatementStaff.setString(6, parts[6]);
                    preparedStatementStaff.setInt(7, lookup.get(parts[5]));
                    preparedStatementStaff.setString(8, parts[7]);
                    preparedStatementStaff.addBatch();
                }else{
                    preparedStatementStaff.setInt(1, Integer.parseInt(parts[0]));
                    preparedStatementStaff.setInt(2, Integer.parseInt(parts[4]));
                    preparedStatementStaff.setString(3, parts[1]);
                    preparedStatementStaff.setString(4, parts[3]);
                    preparedStatementStaff.setInt(5, Integer.parseInt(parts[2]));
                    preparedStatementStaff.setString(6, parts[7]);
                    preparedStatementStaff.setInt(7, lookup.get(parts[5] + "," + parts[6]));
                    preparedStatementStaff.setString(8, parts[8]);
                    preparedStatementStaff.addBatch();
                }
            }
        }
        preparedStatementStaff.executeBatch();
        preparedStatementStaff.close();


        connection.commit();
        connection.close();
    }

    //
    public static String truncate(){
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select truncate()";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("truncate"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //center
    public static void centerInsert(int center_id, String supply_center){
        String sql;

        sql = "Insert into center(center_id, supply_center) values (?, ?)";
        try {
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, center_id);
            preparedStatement.setString(2, supply_center);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String centerSelect(){
        String sql;
        StringBuilder sb = new StringBuilder();
        sql = "select * from center";
        try {
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getInt("center_id")).append("\t");
                sb.append(resultSet.getString("supply_center")).append("\t");
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void centerDelete(int center_id){
        String sql;
        sql = "delete from center where center_id = ?";
        try {
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, center_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void centerUpdate(int center_id, String supply_center){
        String sql;
        sql = "update center set supply_center = ? where center_id = ?";
        try {
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supply_center);
            preparedStatement.setInt(2, center_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void stockInSingle(int id, String supply_center, String product_model, int supply_staff, Date date, int purchase_price, int quantity){
        String sql;
        sql = "Insert into inventory(inventory_id, supply_center, product_model, supply_staff_number, store_date, purchase_price, model_quantity, model_sold) values (?, ?, ?, ?, ?, ?, ?, 0)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, supply_center);
            preparedStatement.setString(3, product_model);
            preparedStatement.setInt(4, supply_staff);
            preparedStatement.setDate(5, date);
            preparedStatement.setInt(6, purchase_price);
            preparedStatement.setInt(7, quantity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void placeOrderSingle(String contract_num, String enterprise, String product_model, int quantity, int contract_manager,
                                        Date contract_date, Date estimated_delivery_date, Date lodgement_date, int salesman_num, String contract_type) {
        String sql, sql1;
        sql = "Insert into orders(contract_number, enterprise, product_model, order_quantity, estimated_delivery_date" +
                ", lodgement_date, salesman_number) values (?, ?, ?, ?, ?, ?, ?)";
        sql1 = "Insert into contract(contract_number, client_enterprise, contract_date, contract_manager_number, contract_type) values (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement.setString(1, contract_num);
            preparedStatement.setString(2, enterprise);
            preparedStatement.setString(3, product_model);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setDate(5, estimated_delivery_date);
            preparedStatement.setDate(6, lodgement_date);
            preparedStatement.setInt(7, salesman_num);
            preparedStatement.executeUpdate();

            preparedStatement1.setString(1, contract_num);
            preparedStatement1.setString(2, enterprise);
            preparedStatement1.setDate(3, contract_date);
            preparedStatement1.setInt(4, contract_manager);
            preparedStatement1.setString(5, contract_type);
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateOrderSingle(String contract_number, String product_model, int salesman_number,
                                         int quantity, Date esDate, Date loDate) {
        String sql;
        sql = "update orders set order_quantity = ?, estimated_delivery_date = ?, lodgement_date = ? " +
                "where contract_number = ? and product_model = ? and salesman_number = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setDate(2, esDate);
            preparedStatement.setDate(3, loDate);
            preparedStatement.setString(4, contract_number);
            preparedStatement.setString(5, product_model);
            preparedStatement.setInt(6, salesman_number);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderSingle(String contract_number, int salesman_number, int seq) {
        String sql0,sql1;
        sql0 = "select * from orders where contract_number = ? and salesman_number = ? order by estimated_delivery_date, product_model";
        sql1 = "delete from orders where contract_number = ? and salesman_number = ? and product_model = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql0);
            preparedStatement.setString(1, contract_number);
            preparedStatement.setInt(2, salesman_number);
            resultSet = preparedStatement.executeQuery();
            int cnt = 1;
            while(resultSet.next()){
                if (cnt == seq){
                    String model = resultSet.getString("product_model");
                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                    preparedStatement1.setString(1, contract_number);
                    preparedStatement1.setInt(2, salesman_number);
                    preparedStatement1.setString(3, model);
                    preparedStatement1.executeUpdate();
                    break;
                }
                cnt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void stockIn()throws SQLException, IOException{
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infileInventory = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\in_stoke_test.csv"));
        infileInventory.skip(73);
        while((line = infileInventory.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                if (!Objects.equals(parts[1],"\"Hong Kong")){
                    parts[4] = parts[4].replace('/', '-');
                    stockInSingle(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]), Date.valueOf(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
                }else {
                    parts[5] = parts[5].replace('/', '-');
                    stockInSingle(Integer.parseInt(parts[0]),parts[1] + "," + parts[2], parts[3], Integer.parseInt(parts[4]), Date.valueOf(parts[5]), Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
                }
            }
        }
        connection.close();
    }

    public static void placeOrder() throws SQLException, IOException{
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infilePlaceOrder = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\task2_test_data_final_public.csv"));
        infilePlaceOrder.skip(145);
        while((line = infilePlaceOrder.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                String num = parts[0].substring(3);
                parts[5] = parts[5].replace('/', '-');
                parts[6] = parts[6].replace('/', '-');
                parts[7] = parts[7].replace('/', '-');
                placeOrderSingle(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Date.valueOf(parts[5]), Date.valueOf(parts[6]),
                        Date.valueOf(parts[7]), Integer.parseInt(parts[8]), parts[9]);
            }
        }
        connection.close();
    }

    public static void updateOrder() throws SQLException, IOException{
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infileUpdateOrder = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\update_final_test.csv"));
        infileUpdateOrder.skip(79);
        while((line = infileUpdateOrder.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                parts[4] = parts[4].replace('/', '-');
                parts[5] = parts[5].replace('/', '-');
                updateOrderSingle(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Date.valueOf(parts[4]), Date.valueOf(parts[5]));
            }
        }
        connection.close();
    }

    public static void deleteOrder() throws SQLException, IOException{
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infileDeleteOrder = new BufferedReader(new FileReader( "C:\\Users\\Lenovo\\Desktop\\Database\\project # 2\\basic part\\data\\delete_final.csv"));
        infileDeleteOrder.skip(22);
        while((line = infileDeleteOrder.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                deleteOrderSingle(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
        }
        connection.close();
    }
    //6
    public static String getAllStaffCount(){
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select getAllStaffCount()";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("getAllStaffCount"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //7
    public static String getContractCount(){
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select getContractCount()";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                sb.append(resultSet.getInt("getContractCount"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //8
    public static String getOrderCount(){
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select getOrderCount()";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                sb.append(resultSet.getInt("getOrderCount"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //9
    public static String getNeverSoldProductCount(){
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select getNeverSoldProductCount()";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                sb.append(resultSet.getInt("getNeverSoldProductCount"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //10
    public static String getFavoriteProductModel() {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select getFavoriteProductModel()";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                sb.append(resultSet.getString("getFavoriteProductModel"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //11
    public static String getAvgStockByCenter() {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select supply_center, round((cast(sum(model_quantity-model_sold) as numeric) / cast(count(product_model) as numeric)), 1) as round " +
                "from inventory group by supply_center order by supply_center desc;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("supply_center")).append("\t");
                sb.append(resultSet.getString("round"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //12
    public static String getProductByNumber(String product_code) {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select supply_center, product_code, m.product_model, purchase_price,(model_quantity - model_sold) as quantity " +
                "from inventory i join model m on i.product_model = m.product_model " +
                "where product_code = ? order by supply_center, product_model;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product_code);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("supply_center")).append("\t");
                sb.append(resultSet.getString("product_code")).append("\t");
                sb.append(resultSet.getString("product_model")).append("\t");
                sb.append(resultSet.getString("purchase_price")).append("\t");
                sb.append(resultSet.getInt("quantity"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //13
    public static String getContractInfo(String contract_number) {
        StringBuilder sb = new StringBuilder();
        String sql1,sql2;
        sql1 = "select contract_number, staff_name, client_enterprise, supply_center from center cen join " +
                "(select contract_number, staff_name, client_enterprise, center_id from contract c join staff s " +
                "    on c.contract_manager_number = s.staff_number where contract_number = ?) as k " +
                "on k.center_id = cen.center_id;";

        sql2 = "select o.product_model as product_model, staff_name as name, order_quantity as quantity, unit_price, estimated_delivery_date, lodgement_date from orders o " +
                "    join staff s on o.salesman_number = s.staff_number " +
                "    join model m on m.product_model = o.product_model " +
                "    where contract_number = ?;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, contract_number);
            resultSet = preparedStatement1.executeQuery();
            while (resultSet.next()) {
                sb.append("contract_number: ").append(resultSet.getString("contract_number")).append("\n");
                sb.append("staff_name: ").append(resultSet.getString("staff_name")).append("\n");
                sb.append("enterprise: ").append(resultSet.getString("client_enterprise")).append("\n");
                sb.append("supply_center: ").append(resultSet.getString("supply_center")).append("\n");
            }
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setString(1, contract_number);
            ResultSet resultSet1 = preparedStatement2.executeQuery();
            while (resultSet1.next()){
                sb.append(resultSet1.getString("product_model")).append("\t");
                sb.append(resultSet1.getString("name")).append("\t");
                sb.append(resultSet1.getString("quantity")).append("\t");
                sb.append(resultSet1.getString("unit_price")).append("\t");
                sb.append(resultSet1.getString("estimated_delivery_date")).append("\t");
                sb.append(resultSet1.getString("lodgement_date"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getOrderListByContractNum(String contract_number) {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select * from orders where contract_number = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, contract_number);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("contract_number")).append("\t");
                sb.append(resultSet.getString("product_model")).append("\t");
                sb.append(resultSet.getInt("order_quantity")).append("\t");
                sb.append(resultSet.getString("estimated_delivery_date")).append("\t");
                sb.append(resultSet.getString("lodgement_date")).append("\t");
                sb.append(resultSet.getInt("salesman_number")).append("\t");
                sb.append(resultSet.getString("enterprise"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //other
    public static String getOrderListByThree(String contract_number,int salesman_number, String model) {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select * from orders where product_model = ? and contract_number = ? and salesman_number = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, model);
            preparedStatement.setString(2, contract_number);
            preparedStatement.setInt(3, salesman_number);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("contract_number")).append("\t");
                sb.append(resultSet.getString("product_model")).append("\t");
                sb.append(resultSet.getInt("order_quantity")).append("\t");
                sb.append(resultSet.getString("estimated_delivery_date")).append("\t");
                sb.append(resultSet.getString("lodgement_date")).append("\t");
                sb.append(resultSet.getInt("salesman_number")).append("\t");
                sb.append(resultSet.getString("enterprise"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getOrderListByTwo(String contract_number,int salesman_number) {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select * from orders where contract_number = ? and salesman_number = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, contract_number);
            preparedStatement.setInt(2, salesman_number);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("contract_number")).append("\t");
                sb.append(resultSet.getString("product_model")).append("\t");
                sb.append(resultSet.getInt("order_quantity")).append("\t");
                sb.append(resultSet.getString("estimated_delivery_date")).append("\t");
                sb.append(resultSet.getString("lodgement_date")).append("\t");
                sb.append(resultSet.getInt("salesman_number")).append("\t");
                sb.append(resultSet.getString("enterprise"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getOrderListBySalesman(int salesman_number) {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select * from orders where salesman_number = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, salesman_number);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("contract_number")).append("\t");
                sb.append(resultSet.getString("product_model")).append("\t");
                sb.append(resultSet.getInt("order_quantity")).append("\t");
                sb.append(resultSet.getString("estimated_delivery_date")).append("\t");
                sb.append(resultSet.getString("lodgement_date")).append("\t");
                sb.append(resultSet.getInt("salesman_number")).append("\t");
                sb.append(resultSet.getString("enterprise"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getBestSalesman() {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select staff_id, staff_name, gender, age, mobile_phone, center_id, salesman_number,count as orders_num from\n" +
                "(select count, salesman_number from\n" +
                "(select count(salesman_number) as count , salesman_number from orders group by salesman_number) as a\n" +
                "order by count desc limit 1) as b join staff s\n" +
                "on b.salesman_number = s.staff_number;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getInt("staff_id")).append("\t");
                sb.append(resultSet.getString("staff_name")).append("\t");
                sb.append(resultSet.getString("gender")).append("\t");
                sb.append(resultSet.getInt("age")).append("\t");
                sb.append(resultSet.getString("mobile_phone")).append("\t");
                sb.append(resultSet.getInt("center_id")).append("\t");
                sb.append(resultSet.getString("salesman_number")).append("\t");
                sb.append(resultSet.getInt("orders_num"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static String getMVPModel() {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select a.product_model as model,sum,unit_price, (sum*unit_price) as total_price from\n" +
                "(select product_model, sum(model_sold) as sum from inventory group by product_model) as a join model m\n" +
                "on a.product_model = m.product_model order by total_price desc limit 1;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("model")).append("\t");
                sb.append(resultSet.getInt("sum")).append("\t");
                sb.append(resultSet.getInt("unit_price")).append("\t");
                sb.append(resultSet.getInt("total_price"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }
    public static String getPureProfits() {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select supply_center,sum((unit_price*inventory.model_sold)) - sum(purchase_price) as lirun\n" +
                "from inventory join model m on inventory.product_model = m.product_model group by supply_center;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("supply_center")).append("\t");
                sb.append(resultSet.getInt("lirun"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static String getBill() {
        StringBuilder sb = new StringBuilder();
        String sql;
        sql = "select supply_center,sum((unit_price*inventory.model_sold)) - sum(purchase_price) as jinlinrun,sum((unit_price*inventory.model_sold)) as profits,\n" +
                "       sum(purchase_price) cost\n" +
                "from inventory join model m on inventory.product_model = m.product_model group by supply_center;";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("supply_center")).append("\t");
                sb.append(resultSet.getInt("jinlinrun")).append("\t");
                sb.append(resultSet.getInt("profits")).append("\t");
                sb.append(resultSet.getInt("cost"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }



}