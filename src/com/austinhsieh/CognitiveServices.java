package com.austinhsieh;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CognitiveServices {
    String key;
    CloseableHttpClient httpClient;

    public CognitiveServices(String key) {
        httpClient = HttpClients.createDefault();
        this.key = key;
    }

    public JsonArray postLocalToEmotionAPI(BufferedImage image) {
        HttpPost httpPost = new HttpPost("https://api.projectoxford.ai/emotion/v1.0/recognize");
        //"https://api.projectoxford.ai/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=true"
        httpPost.setHeader("Content-Type", "application/octet-stream");
        httpPost.setHeader("Ocp-Apim-Subscription-Key", key);

        ByteArrayEntity entity = null;
        entity = new ByteArrayEntity(imageToByteArray(image));
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());

            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(str).getAsJsonArray();
            closeResponse(response);
            return jsonArray;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] imageToByteArray(BufferedImage image) {
        byte[] imageInByte = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageInByte;
    }

    public JsonObject postWebToEmotionAPI(String imageURL) {
        HttpPost httpPost = new HttpPost(
        "https://api.projectoxford.ai/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=true");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Ocp-Apim-Subscription-Key", key);

        StringEntity entity = null;
        try {
            entity = new StringEntity("{\"url\":\"" + imageURL + "\"}");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(str.substring(1, str.length() - 1)).getAsJsonObject();
            closeResponse(response);
            return object;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void closeResponse(CloseableHttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
