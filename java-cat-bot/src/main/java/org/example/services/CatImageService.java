package org.example.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.CatImageRequestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatImageService {
    private CatImageService(){}
    private static final Logger logger = LogManager.getLogger(CatImageService.class);
    private static final String CAT_API_URL = Dotenv.load().get("CAT_API_URL");
    public static BufferedImage getRandomCatImage() throws CatImageRequestException, IOException {
        HttpURLConnection connection = sendRequest();
        String imageUrl = getImageUrlFromResponse(connection);

        return convertUrlToImage(imageUrl);
    }

    private static BufferedImage convertUrlToImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }

    private static HttpURLConnection sendRequest() throws CatImageRequestException {
        try {
            URL apiUrl = new URL(CAT_API_URL);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            logger.error("get cat image fail");
            throw new CatImageRequestException();
        }
    }

    private static String getImageUrlFromResponse(HttpURLConnection connection) throws CatImageRequestException {
        try(BufferedReader connectionReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = connectionReader.readLine()) != null) {
                response.append(inputLine);
            }

            return response.substring(
                    response.indexOf("http"),
                    response.indexOf(".jpg") + 4
            );
        } catch (Exception e) {
            logger.error("build url of cat image fail");
            throw new CatImageRequestException();
        }
    }
}
