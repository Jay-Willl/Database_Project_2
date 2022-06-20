package com.example.project_x;

import com.example.project_x.apis.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
public class ProjectXApplication {

    public static com.example.project_x.apis.Top Top;
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Top = new Top();
//        com.example.project_x.apis.Top.initialize();
//        com.example.project_x.apis.Top.stockIn();
//        com.example.project_x.apis.Top.placeOrder();

        SpringApplication.run(ProjectXApplication.class, args);
    }

}
