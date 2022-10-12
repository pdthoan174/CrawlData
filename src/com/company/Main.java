package com.company;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException {

        ArrayList<MobileItems> listMobiles = new ArrayList<>();

        Document doc = Jsoup.connect("https://tamanhhospital.vn/chuyen-khoa/hoi-suc-cap-cuu-icu/#ckhoa_chuyengia").get();
        Elements elements = doc.getElementsByClass("item_post");

        // dung thu vien Jsoup de xoa the html trong string
        Whitelist whitelist = Whitelist.none();
        String cleanStr = Jsoup.clean("yourText", whitelist);

        for (Element e : elements){
            MobileItems item = new MobileItems();
            //item.setPrice(Jsoup.clean(e.childNode(0).childNode(0).childNode(1).childNode(2).childNode(0).childNode(0).outerHtml(),whitelist));

            // dung REGEX de xoa the html trong string
            //item.setTitle(e.childNode(0).childNode(0).childNode(1).childNode(0).childNode(0).outerHtml().replaceAll("<[^>]*>", ""));

            // dung thu vien Jsoup de xoa the html trong string
            String name = e.childNode(1).childNode(3).childNode(1).childNode(1).attr("title");

            String imageUrl = e.childNode(1).childNode(1).childNode(1).childNode(1).attr("src");
            String description = Jsoup.clean(e.childNode(1).childNode(3).childNode(1).childNode(5).outerHtml(),whitelist);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

//            item.setNameDoctor(convertString(name));
            item.setNameDoctor(convertString(name));
            item.setImageUrl(imageUrl);
            item.setDescription(description);
            item.setPhone(randomPhoneNumber());
            item.setPassword("$2a$10$kBHhLQxrvHTDr1B4p204xuvNq3EShwLoI0xdCEE3HTf3h/KamZufe");
            item.setRole("DOCTOR");
            item.setDob("1974-09-27");
            item.setCreatDate(timeStamp);
//            item.setDetailUrl(e.child(0).attr("href"));
            listMobiles.add(item);
        }

        //Chuyển listMobiles thành JSON text
        Gson gson = new Gson();
        String jsonData = gson.toJson(listMobiles);

        System.out.println(jsonData);
        for (int i = 0; i < listMobiles.size(); i++){
            System.out.println("\n"+i + " Image: " + listMobiles.get(i).getImageUrl());
            System.out.println(i + " Name: " + listMobiles.get(i).getNameDoctor());
            System.out.println(i + " Phone: " + listMobiles.get(i).getPhone());
            System.out.println(i + " Description: " + listMobiles.get(i).getDescription());
            System.out.println(i + " Pass: " + listMobiles.get(i).getPassword());
            System.out.println(i + " Role: " + listMobiles.get(i).getRole());
            System.out.println(i + " DOB: " + listMobiles.get(i).getDob());
            System.out.println(i + " Create: " + listMobiles.get(i).getCreatDate());
        }
//        insertData(listMobiles);
//        delete();
          insertDoctor();
    }

    // function convert string GS.TS.BS NGÔ QUÝ CHÂU -> Ngô Quý Châu
    public static String convertString(String name){
        int index = name.indexOf(" ");
        name = name.substring(index, name.length()).toLowerCase();

        char[] charArr = name.toCharArray();
        for (int i = 0; i<charArr.length;i++){
            if (charArr[i] == ' '){
                charArr[i + 1] = Character.toUpperCase(charArr[i + 1]);
            }
        }
        name = String.valueOf(charArr);
        return name;
    }


    // +84987654321
    public static String randomPhoneNumber(){
        int value = 0;
//        int max = 100000000;
        int min = 1000000;
        int max = 9999999;

        String countryCode = "+84";
        String viettel = "98";
        String phoneNumber ="";

        Random generator = new Random();
        value = generator.nextInt((max - min) + 1) + min;
        phoneNumber = countryCode + viettel + value;
        return phoneNumber;
    }

    public static void insertData(ArrayList<MobileItems> listData) {
        try {
            String url = "jdbc:mysql://localhost:3306/encare";
            String username = "root";
            String password = "123456";

            for (int i = 0; i < listData.size(); i++){
                String avatar = listData.get(i).getImageUrl();
                String dob = listData.get(i).getDob();
                String create = listData.get(i).getCreatDate();
                String des = listData.get(i).getDescription();
                String name = listData.get(i).getNameDoctor();
                String pass = listData.get(i).getPassword();
                String phone = listData.get(i).getPhone();
                String role = listData.get(i).getRole();

                String insert = "INSERT INTO encare.account (avatar,birthday,create_date, description, name, password, phone, role)\n" +
                        "VALUES (?,?,?,?,?,?,?,?);";

                Connection connection = DriverManager.getConnection(url, username, password);

                PreparedStatement pstmt = connection.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1,avatar);
                pstmt.setString(2,dob);
                pstmt.setString(3,create);
                pstmt.setString(4,des);
                pstmt.setString(5,name);
                pstmt.setString(6,pass);
                pstmt.setString(7,phone);
                pstmt.setString(8,role);

                pstmt.executeUpdate();
//                Statement statement = connection.createStatement();
//                statement.executeQuery(insert);

                System.out.println("Insert successfully");
            }
            //INSERT INTO table_name (column1, column2, column3, ...)
            //VALUES (value1, value2, value3, ...);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void delete(){
        try {
            String url = "jdbc:mysql://localhost:3306/encare";
            String username = "root";
            String password = "123456";
            Connection connection = DriverManager.getConnection(url, username, password);

            Statement st = connection.createStatement();
            for (int i = 59; i <= 64; i++){
                String sql = "DELETE FROM encare.account where `account_id` = '"+i+"';";
                st.execute(sql);
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    public static void insertDoctor(){
        try {
            String url = "jdbc:mysql://localhost:3306/encare";
            String username = "root";
            String password = "123456";

            int count = 5;
            int rating = 5;

            // 41 - 47
            int accountId = 41;
            int categoryId = 12;
            int hospitalId = 1;

            for (int i = 65; i <= 69; i++){
                Random generator = new Random();
                hospitalId = generator.nextInt((10 - 1) + 1) + 1;

                String insert = "INSERT INTO encare.doctor (count_rating, rating, account_id, category_id, hospital_id)\n" +
                        "VALUES (?,?,?,?,?);";

                Connection connection = DriverManager.getConnection(url, username, password);

                PreparedStatement pstmt = connection.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1,count);
                pstmt.setInt(2,rating);
                pstmt.setInt(3,i);
                pstmt.setInt(4,categoryId);
                pstmt.setInt(5,hospitalId);

                pstmt.executeUpdate();
//                Statement statement = connection.createStatement();
//                statement.executeQuery(insert);
            }
            //INSERT INTO table_name (column1, column2, column3, ...)
            //VALUES (value1, value2, value3, ...);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Insert Doctor Successfully");
    }


}
