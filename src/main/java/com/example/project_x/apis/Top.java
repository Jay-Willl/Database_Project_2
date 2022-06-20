package com.example.project_x.apis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Top {

    public Top() throws SQLException, IOException, ClassNotFoundException {
        connection = DriverManager.getConnection(url, username, password);
    }
    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(url, username, password);



            initialize();
            stockIn();

//            System.out.println(getContractCount());
//            System.out.println(getOrderCount());
//            System.out.println(getNeverSoldProductCount());


//            int produceTaskMaxNumber = 20;
//            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 60L, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3));
////            for (int i = 0; i < produceTaskMaxNumber; i++) {
////                String work = "work@ " + i;
////                System.out.println("put ：" + work);
////            }
//
//            threadPoolExecutor.execute(new task6());
//            threadPoolExecutor.execute(new task7());
//            threadPoolExecutor.execute(new task8());
//            threadPoolExecutor.execute(new task9());
//            threadPoolExecutor.execute(new task10());
//            threadPoolExecutor.shutdown();
            // stockInConcurrent();
            // stockIn();

            // placeOrderConcurrent();
            // placeOrder();

            // updateOrderConcurrent();
            // updateOrder();

            // deleteOrderConcurrent();
            // deleteOrder();


            //stockIn();

//            System.out.println(getContractInfo("CSE0000101"));
//            System.out.println(getAllStaffCount());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static class getAllStaffCountThread implements Runnable{
        @Override
        public void run() {
            System.out.println(getAllStaffCount());
        }
    }

    public static class getOrderCountThread implements Runnable{
        @Override
        public void run() {
            System.out.println(getOrderCount());
        }
    }

    public static class getContractCountThread implements Runnable{
        @Override
        public void run() {
            System.out.println(getContractCount());
        }
    }

    public static class getNeverSoldProductCountThread implements Runnable{
        @Override
        public void run() {
            System.out.println(getNeverSoldProductCount());
        }
    }

    public static class getFavoriteProductModelThread implements Runnable{
        @Override
        public void run() {
            System.out.println(getFavoriteProductModel());
        }
    }

    public static class getAvgStockByCenterThread implements Runnable{
        @Override
        public void run() {
            getAvgStockByCenter();
        }
    }

    public static class getProductByNumberThread implements Runnable{
        private String product_code;

        @Override
        public void run() {
            getProductByNumber(product_code);
        }
    }

    public static class getContractInfoThread implements Runnable{
        private String contract_number;

        @Override
        public void run() {
            getContractInfo(contract_number);
        }
    }

    public static void initialize() throws SQLException, IOException, ClassNotFoundException {
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
        BufferedReader infileEnterprise = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/data/enterprise.csv"));
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
        BufferedReader infileModel = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/data/model.csv"));
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
        BufferedReader infileStaff = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/data/staff.csv"));
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

    //center
    public static void centerInsert(int center_id, String supply_center){
        String sql;
        sql = "Insert into center(center_id, supply_center) values (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, center_id);
            preparedStatement.setString(2, supply_center);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void centerSelect(){
        String sql;
        sql = "select * from center";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void centerDelete(int center_id){
        String sql;
        sql = "delete from center where center_id = ?";
        try {
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supply_center);
            preparedStatement.setInt(2, center_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void stockIn()throws SQLException, IOException{
        long time1 = System.nanoTime();
        String line;
        String[] parts;

        BufferedReader infileInventory = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task1_in_stoke_test_data_publish.csv"));
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
        long time2 = System.nanoTime();
        System.out.println("stock in takes " + (time2-time1)/1000000000D + " seconds");
    }

//    public static void stockIn()throws SQLException, IOException{
//        long time1 = System.nanoTime();
//        String line;
//        String[] parts;
//
//        BufferedReader infileInventory = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task1_in_stoke_test_data_publish.csv"));
//        infileInventory.skip(73);
//        while((line = infileInventory.readLine()) != null){
//            parts = line.split(",");
//            if(parts.length > 1){
//                if (!Objects.equals(parts[1],"\"Hong Kong")){
//                    stockInSingle(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
//                }else {
//                    stockInSingle(Integer.parseInt(parts[0]),parts[1] + "," + parts[2], parts[3], Integer.parseInt(parts[4]), parts[5], Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
//                }
//            }
//        }
//        connection.close();
//        long time2 = System.nanoTime();
//        System.out.println("stock in takes " + (time2-time1)/1000000000D + " seconds");
//    }

    public static void stockInSingle(int id, String supply_center, String product_model, int supply_staff, Date data, int purchase_price, int quantity){
        String sql;
        sql = "Insert into inventory(inventory_id, supply_center, product_model, supply_staff_number, store_date, purchase_price, model_quantity, model_sold) values (?, ?, ?, ?, ?, ?, ?, 0)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, supply_center);
            preparedStatement.setString(3, product_model);
            preparedStatement.setInt(4, supply_staff);
            preparedStatement.setDate(5, data);
            preparedStatement.setInt(6, purchase_price);
            preparedStatement.setInt(7, quantity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void stockInConcurrent() throws SQLException, IOException, InterruptedException {
        long time1 = System.nanoTime();
        String line;
        String[] parts;

        BufferedReader infileInventory = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task1_in_stoke_test_data_publish.csv"));
        infileInventory.skip(73);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 200, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
        while((line = infileInventory.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                if (!Objects.equals(parts[1],"\"Hong Kong")){
                    parts[4] = parts[4].replace('/', '-');
                    threadPoolExecutor.execute(new stockInThread(connection,Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]), Date.valueOf(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6])));
                }else {
                    parts[5] = parts[5].replace('/', '-');
                    threadPoolExecutor.execute(new stockInThread(connection, Integer.parseInt(parts[0]),parts[1] + "," + parts[2], parts[3], Integer.parseInt(parts[4]), Date.valueOf(parts[5]), Integer.parseInt(parts[6]), Integer.parseInt(parts[7])));
                }
            }
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        long time2 = System.nanoTime();
        System.out.println("stock in concurrently takes "+ (time2-time1)/1000000000D + " seconds");
    }

    public static class stockInThread implements Runnable {

        Connection connection;
        private int id;
        private String supply_center;
        private String product_model;
        private int supply_staff;
        private Date data;
        private int purchase_price;
        private int quantity;


        public stockInThread(Connection connection, int id, String supply_center, String product_model, int supply_staff, Date data, int purchase_price, int quantity) throws SQLException {
            this.connection = connection;
            this.id = id;
            this.supply_center = supply_center;
            this.product_model = product_model;
            this.supply_staff = supply_staff;
            this.data = data;
            this.purchase_price = purchase_price;
            this.quantity = quantity;
        }

        @Override
        public void run() {
            stockInSingle(id, supply_center, product_model, supply_staff, data, purchase_price, quantity);
        }
    }

    public static void placeOrder() throws SQLException, IOException{
        long time1 = System.nanoTime();
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infilePlaceOrder = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task2_test_data_publish.csv"));
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
        long time2 = System.nanoTime();
        System.out.println("place order takes " + (time2-time1)/1000000000D + " seconds");
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

    public static void placeOrderConcurrent() throws IOException, InterruptedException {
        long time1 = System.nanoTime();
        String line;
        String[] parts;
        BufferedReader infilePlaceOrder = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task2_test_data_publish.csv"));
        infilePlaceOrder.skip(145);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 600, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
        int cnt = 0;
        while((line = infilePlaceOrder.readLine()) != null){
            parts = line.split(",");
            if(parts.length > 1){
                String num = parts[0].substring(3);
                cnt = Integer.parseInt(num);
                threadPoolExecutor.execute(new placeOrderThread(connection, parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Date.valueOf(parts[5]), Date.valueOf(parts[6]),
                        Date.valueOf(parts[7]), Integer.parseInt(parts[8]), parts[9]));
            }
            cnt++;
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        long time2 = System.nanoTime();
        System.out.println("place order concurrently takes "+ (time2-time1)/1000000000D + " seconds");

    }

    public static class placeOrderThread implements Runnable{
        Connection connection;
        private String contract_num;
        private String enterprise;
        private String product_model;
        private int quantity;
        private int contract_manager;
        private Date contract_date;
        private Date estimated_delivery_date;
        private Date lodgement_date;
        private int salesman_num;
        private String contract_type;


        public placeOrderThread(Connection connection, String contract_num, String enterprise, String product_model, int quantity, int contract_manager, Date contract_date, Date estimated_delivery_date, Date lodgement_date, int salesman_num, String contract_type) {
            this.connection = connection;
            this.contract_num = contract_num;
            this.enterprise = enterprise;
            this.product_model = product_model;
            this.quantity = quantity;
            this.contract_manager = contract_manager;
            this.contract_date = contract_date;
            this.estimated_delivery_date = estimated_delivery_date;
            this.lodgement_date = lodgement_date;
            this.salesman_num = salesman_num;
            this.contract_type = contract_type;
        }

        @Override
        public void run() {
            placeOrderSingle(contract_num, enterprise, product_model, quantity, contract_manager, contract_date, estimated_delivery_date, lodgement_date, salesman_num, contract_type);
        }
    }



    public static void updateOrder() throws SQLException, IOException{
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infileUpdateOrder = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task34_update_test_data_publish.tsv"));
        infileUpdateOrder.skip(91);
        while((line = infileUpdateOrder.readLine()) != null){
            parts = line.split(" ");
            if(parts.length > 1){
                updateOrderSingle(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Date.valueOf(parts[4]), Date.valueOf(parts[5]));
            }
        }
        connection.close();
    }

    public static class updateOrderThread implements Runnable{
        Connection connection;
        private String contract_number;
        private String product_model;
        private int salesman_number;
        private int quantity;
        private Date esDate;
        private Date loDate;

        public updateOrderThread(Connection connection, String contract_number, String product_model, int salesman_number, int quantity, Date esDate, Date loDate) {
            this.connection = connection;
            this.contract_number = contract_number;
            this.product_model = product_model;
            this.salesman_number = salesman_number;
            this.quantity = quantity;
            this.esDate = esDate;
            this.loDate = loDate;
        }

        @Override
        public void run() {
            updateOrderSingle(contract_number, product_model, salesman_number, quantity, esDate, loDate);
        }
    }

    public static void updateOrderSingle(String contract_number, String product_model, int salesman_number,
                            int quantity, Date esDate, Date loDate) {
        String sql;
        sql = "update orders set order_quantity = ?, estimated_delivery_date = ?, lodgement_date = ? " +
                "where contract_number = ?, product_model = ?, salesman_number = ?";
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

    public static void updateOrderConcurrent() throws IOException, InterruptedException {
        long time1 = System.nanoTime();
        String line;
        String[] parts;
        BufferedReader infileUpdateOrder = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task34_update_test_data_publish.tsv"));
        infileUpdateOrder.skip(91);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 300, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
        while((line = infileUpdateOrder.readLine()) != null){
            parts = line.split(" ");
            if(parts.length > 1){
                threadPoolExecutor.execute(new updateOrderThread(connection, parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Date.valueOf(parts[4]), Date.valueOf(parts[5])));
            }
        }

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        long time2 = System.nanoTime();
        System.out.println("place order concurrently takes "+ (time2-time1)/1000000000D + " seconds");
    }


    public static void deleteOrder() throws SQLException, IOException{
        connection = DriverManager.getConnection(url, username, password);
        String line;
        String[] parts;

        BufferedReader infileDeleteOrder = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task34_delete_test_data_publish.tsv"));
        infileDeleteOrder.skip(28);
        while((line = infileDeleteOrder.readLine()) != null){
            parts = line.split(" ");
            if(parts.length > 1){
                deleteOrderSingle(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
        }
        connection.close();
    }

    public static void deleteOrderSingle(String contract_number, int salesman_number, int seq) {
        String sql0,sql1;
        sql0 = "select * from orders where contract_number = ? and salesman_number = ? order by estimated_delivery_date, product_model";
        sql1 = "delete from orders where contract_number = ?, salesman_number = ?, product_model = ?";
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

    public static void deleteOrderConcurrent() throws IOException, InterruptedException {
        long time1 = System.nanoTime();
        String line;
        String[] parts;

        BufferedReader infileDeleteOrder = new BufferedReader(new FileReader( "/Users/blank/repo/project_x/src/main/java/com/example/project_x/apis/release-testcase1/task34_delete_test_data_publish.tsv"));
        infileDeleteOrder.skip(28);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 300, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
        while((line = infileDeleteOrder.readLine()) != null){
            parts = line.split(" ");
            if(parts.length > 1){
                threadPoolExecutor.execute(new deleteOrderThread(connection, parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
        }

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        long time2 = System.nanoTime();
        System.out.println("place order concurrently takes "+ (time2-time1)/1000000000D + " seconds");

    }

    public static class deleteOrderThread implements Runnable{
        Connection connection;
        private String contract_number;
        private int salesman_number;
        private int seq;

        public deleteOrderThread(Connection connection, String contract_number, int salesman_number, int seq) {
            this.connection = connection;
            this.contract_number = contract_number;
            this.salesman_number = salesman_number;
            this.seq = seq;
        }

        @Override
        public void run() {
            deleteOrderSingle(contract_number, salesman_number, seq);
        }
    }

    //6
    public static String getAllStaffCount(){
        String sql;
        sql = "select getAllStaffCount()";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            String res = "";
            while(resultSet.next()){
                res = res + " / " + resultSet.getString("getallstaffcount");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    //7
    public static int getContractCount(){
        String sql;
        sql = "select getContractCount()";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            int res = 0;
            while(resultSet.next()){
                res = resultSet.getInt("getcontractcount");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //8
    public static int getOrderCount(){
        String sql;
        sql = "select getOrderCount()";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            int res = 0;
            while(resultSet.next()){
                res = resultSet.getInt("getordercount");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //9
    public static int getNeverSoldProductCount(){
        String sql;
        sql = "select getNeverSoldProductCount()";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            int res = 0;
            while(resultSet.next()){
                res = resultSet.getInt("getneversoldproductcount");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //10
    public static String getFavoriteProductModel() {
        String sql;
        sql = "select getFavoriteProductModel()";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            String res = "";
            while(resultSet.next()){
                res = resultSet.getString("getfavoriteproductmodel");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    //11
    public static void getAvgStockByCenter() {
        String sql;
        sql = "select supply_center, round((cast(sum(model_quantity) as numeric) / cast(count(product_model) as numeric)), 1)" +
                "from inventory group by supply_center order by supply_center desc;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //12
    public static void getProductByNumber(String product_code) {
        String sql;
        sql = "select supply_center, product_code, m.product_model, purchase_price,(model_quantity - model_sold)" +
                "from inventory i join model m on i.product_model = m.product_model where product_code = ? order by supply_center, product_model;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product_code);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //13
    public static String getContractInfo(String contract_number) {
        String sql1,sql2;
        sql1 = "select contract_number, staff_name, client_enterprise, supply_center from center cen join" +
                "(select contract_number, staff_name, client_enterprise, center_id from contract c join staff s" +
                "    on c.contract_manager_number = s.staff_number where contract_number = ?) as a on a.center_id = cen.center_id;";
        sql2 = "select o.product_model, staff_name, order_quantity, unit_price, estimated_delivery_date, lodgement_date from orders o" +
                "    join staff s on o.salesman_number = s.staff_number" +
                "    join model m on m.product_model = o.product_model" +
                "    where contract_number = ?;";
        try {
            PreparedStatement Statement1 = connection.prepareStatement(sql1);
            PreparedStatement Statement2 = connection.prepareStatement(sql2);
            Statement1.setString(1, contract_number);
            Statement2.setString(1, contract_number);
            ResultSet resultSet1 = Statement1.executeQuery();
            ResultSet resultSet2 = Statement2.executeQuery();
            contractinfo contractinfo = new contractinfo();
            while(resultSet1.next()){
                contractinfo.setContract_number(resultSet1.getString("contract_number"));
                contractinfo.setClient_enterprise(resultSet1.getString("client_enterprise"));
                contractinfo.setStaff_name(resultSet1.getString("staff_name"));
                contractinfo.setSupply_center(resultSet1.getString("supply_center"));
            }
            while(resultSet2.next()){
                contractinfo.setProduct_model(resultSet2.getString("product_model"));
                contractinfo.setOrder_quantity(resultSet2.getString("order_quantity"));
                contractinfo.setUnit_price(resultSet2.getString("unit_price"));
                contractinfo.setEstimated_delivery_time(resultSet2.getString("estimated_delivery_date"));
                contractinfo.setLodgement_date(resultSet2.getString("lodgement_date"));
            }
            return contractinfo.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "-1";
    }
    static String url = "jdbc:postgresql://localhost:5432/project_2";
    static String username = "***";
    static String password = "***";
    public static Connection connection;
    public static ResultSet resultSet;
}
