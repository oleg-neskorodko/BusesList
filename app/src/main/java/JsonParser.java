import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    String input = "{\"id\":510,\"from_city\":{\"highlight\":1,\"id\":47,\"name\":\"Антрацит\"},\"from_date\":\"2017-09-21\",\"from_time\":\"04:00:00\",\"from_info\":\"по требованию\",\"to_city\":{\"highlight\":1,\"id\":29,\"name\":\"Киев\"},\"to_date\":\"2017-09-21\",\"to_time\":\"20:00:00\",\"to_info\":\"станция метро «Осокорки», возле въезда на платную стоянку \",\"info\":\"Антрацит – Артёмовск –Изюм – Харьков - Полтава-Киев\",\"price\":850,\"bus_id\":14,\"reservation_count\":0}";

    public JsonParser() throws JSONException {
    }

/*    public User getUser(String response) throws JSONException {
        JSONObject userJson = new JSONObject(response);
        long id = userJson.getLong("id");
        String name = userJson.getString("name");
        String nick = userJson.getString("screen_name");
        String location = userJson.getString("location");
        String description = userJson.getString("description");
        String imageUrl = userJson.getString("profile_image_url");
        int followersCount = userJson.getInt("followers_count");
        int followingCount = userJson.getInt("favourites_count");


        return new User(id, imageUrl, name, nick, description, location, followingCount, followersCount);
    }*/

    JSONObject userJson = new JSONObject(input);
    String to_date = userJson.getString("to_date");


    /*                JSONObject userJson = null;
                try {
                    userJson = new JSONObject(response);
                } catch (JSONException e) {
                Log.d("myLogs", "exception");
                    e.printStackTrace();
                }
                try {
                    to_date = userJson.getString("to_date");
                } catch (JSONException e) {
                Log.d("myLogs", "exception");
                    e.printStackTrace();
                }
                txtJson.setText(String.valueOf(to_date));*/

}
