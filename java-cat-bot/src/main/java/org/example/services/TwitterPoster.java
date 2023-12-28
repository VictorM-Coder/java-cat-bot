package org.example.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.consumers.OAuthConsumerTwitter;
import org.example.exceptions.CatImageRequestException;
import org.example.exceptions.OAuthConsumerSignInTwitterException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TwitterPoster {
    private static final Logger logger = LogManager.getLogger(TwitterPoster.class);
    private static final String API_URL_POST_IMAGE = Dotenv.load().get("API_URL_POST_IMAGE");
    private static final String API_URL_POST_TWEET = Dotenv.load().get("API_URL_POST_TWEET");
    private static final OAuthConsumerTwitter consumer = OAuthConsumerTwitter.getInstance();

    public static void sendImage(){
        try {
            HttpResponse response = sendRequestToTwitterApi();
            logger.info(buildResponseSuccessText(response));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static HttpResponse sendRequestToTwitterApi() throws IOException, CatImageRequestException, OAuthConsumerSignInTwitterException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD)
                        .build())
                .build();
        String imageId = sendACatImageToTwitter(httpClient);
        return sendPostToTwitter(imageId, httpClient);
    }

    private static HttpResponse sendPostToTwitter(String imageId, HttpClient httpClient) throws IOException, OAuthConsumerSignInTwitterException {
        HttpPost httpPost = new HttpPost(API_URL_POST_TWEET);
        String requestBody = String.format("{\n" +
                "\t\"text\": \"\",\n" +
                "\t\"media\": {\n" +
                "\t\t\"media_ids\": [\"%s\"]\n" +
                "\t}\n" +
                "}", imageId);

        HttpEntity jsonEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(jsonEntity);
        consumer.sign(httpPost);
        return httpClient.execute(httpPost);
    }

    private static String sendACatImageToTwitter(HttpClient httpClient) throws IOException, CatImageRequestException, OAuthConsumerSignInTwitterException {
        HttpPost httpPost = new HttpPost(API_URL_POST_IMAGE);
        consumer.sign(httpPost);

        httpPost.setEntity(buildMultipartEntityWithCatImage());

        HttpResponse response = httpClient.execute(httpPost);
        return getImageIdFromResponse(response);
    }

    private static String getImageIdFromResponse(HttpResponse response) throws IOException {
        String jsonResponse = EntityUtils.toString(response.getEntity());
        return jsonResponse.substring(
                jsonResponse.indexOf("\"media_id\":") + 11,
                jsonResponse.indexOf(",\"media")
        );
    }

    private static HttpEntity buildMultipartEntityWithCatImage() throws IOException, CatImageRequestException {
        File imageFile = new File("catImage.jpg");
        BufferedImage randomCatImage = CatImageService.getRandomCatImage();
        ImageIO.write(randomCatImage, "jpg", imageFile);

        return MultipartEntityBuilder.create()
                .addPart("media", new FileBody(imageFile, ContentType.DEFAULT_BINARY))
                .build();
    }

    private static String buildResponseSuccessText(HttpResponse response) throws IOException {
        var httpResponseText = EntityUtils.toString(response.getEntity());
        var tweetLink = httpResponseText.substring(
                httpResponseText.indexOf("http"),
                httpResponseText.indexOf("\"}}")
        );
        return "Cat image posted! See here: " + tweetLink;
    }
}
