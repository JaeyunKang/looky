package com.looky.app;

/**
 * Created by kangjaeyun on 2017. 6. 8..
 */

public class AppConfig {

    private static String ip = "http://52.78.60.61/";

    // Server user login url
    public static String URL_LOGIN = ip + "login.php";

    // Server user register url
    public static String URL_REGISTER = ip + "register.php";
    public static String URL_SOCIAL_REGISTER = ip + "socialRegister.php";

    // Update gender&age url
    public static String URL_GENDER_UPDATE = ip + "genderUpdate.php";
    public static String URL_AGE_UPDATE = ip + "ageUpdate.php";

    // Get item name&link url
    public static String URL_GET_ITEM_NAME = ip + "getItemName.php";
    public static String URL_GET_ITEM_LINK = ip + "getItemLink.php";

    // Rating url
    public static String URL_RATING_DB = ip + "ratingDB.php";

    // Feedback url
    public static String URL_FEEDBACK = ip + "feedback.php";

    // Deleting liked-item url
    public static String URL_DELETE = ip + "deleteLike.php";

    // Getting number of like url
    public static String URL_GET_LIKE_NUM = ip + "getLikeNum.php";

    // Filtering like&chart view url
    public static String URL_FILTER_LIKE = ip + "filterLike.php";
    public static String URL_FILTER_CHART = ip + "filterChart.php";

    // Prediction url
    public static String URL_PREDICTION = ip + "prediction.php";

    // Update check
    public static String URL_UPDATE_CHECK = ip + "updateCheck.php";

}