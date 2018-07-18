import org.jsoup.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class CommentScraper {


    public static String YOUTUBE_COMMENTS_URL = "https://www.youtube.com/all_comments?v=";
    public static String YOUTBE_COMMENTS_AJAX_URL = "https://www.youtube.com/comment_ajax";



    public String getReq(String youtubeId) {
        String targetUrl = YOUTUBE_COMMENTS_URL + youtubeId;

        System.out.println(targetUrl);
        HttpURLConnection connection = null;
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return  result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Bad Youtube ID";
    }

    public String postReq(String youtubeId, String xsrfToken, String pageToken) throws IOException {

        String targetUrl = YOUTBE_COMMENTS_AJAX_URL;
        String urlParameters = "action_load_comments=1&order_by_time=True&filter=jBjXVrS8nXs&order_menu=True";

        String updatedURL = targetUrl + "?" + urlParameters;
        URL url = null;
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        String boundary = "------------" + System.currentTimeMillis();

        try {
            url = new URL(updatedURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
            urlConnection.setRequestProperty("user-agent", "USER_AGENT");
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            String data = URLEncoder.encode("video_id", "UTF-8")
                    + "=" + URLEncoder.encode(youtubeId, "UTF-8");

            data += "&" + URLEncoder.encode("session_token", "UTF-8") + "="
                    + URLEncoder.encode(xsrfToken, "UTF-8");

            data += "&" + URLEncoder.encode("page_token", "UTF-8") + "="
                    + URLEncoder.encode(pageToken, "UTF-8");

            urlConnection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data);
            System.out.println(data);
            System.out.println(data.length());
            wr.flush();
            stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
            String result = reader.readLine();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                stream = urlConnection.getErrorStream();

            }

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        return null;

    }



    String findToken(String html, String key, int numChars) {
        int beg_pos = html.indexOf(key) + key.length() + numChars;
        int end_pos = html.indexOf("\"", beg_pos);

        return html.substring(beg_pos, end_pos);
    }


    public Comment[] extractComment(String html) {

        return null;
    }


    public static void main(String[] args) {

        CommentScraper cs = new CommentScraper();
        String youtubeId = "Exf8wpYeMUc";
        String html = cs.getReq(youtubeId);
        String xsrfToken = cs.findToken(html,"XSRF_TOKEN", 4);
        String pageToken = cs.findToken(html, "data-token", 2);
        System.out.println(html);
        System.out.println(xsrfToken);
        System.out.println(pageToken);

        try {
            cs.postReq(youtubeId, xsrfToken, pageToken);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
