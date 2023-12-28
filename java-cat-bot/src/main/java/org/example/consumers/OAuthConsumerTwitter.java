package org.example.consumers;

import io.github.cdimascio.dotenv.Dotenv;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpRequest;
import org.example.exceptions.OAuthConsumerSignInTwitterException;

public class OAuthConsumerTwitter {
    static final String CONSUMER_KEY = Dotenv.load().get("CONSUMER_KEY");
    static final String CONSUMER_SECRET = Dotenv.load().get("CONSUMER_SECRET");
    static final String ACCESS_TOKEN = Dotenv.load().get("ACCESS_TOKEN");
    static final String TOKEN_SECRET = Dotenv.load().get("TOKEN_SECRET");
    private final OAuthConsumer consumer;
    private static OAuthConsumerTwitter instance;

    private OAuthConsumerTwitter(){
        consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);
    }

    public static OAuthConsumerTwitter getInstance() {
        if (instance == null) {
           instance = new OAuthConsumerTwitter();
        }
        return instance;
    }

    public void sign(HttpRequest httpRequest) throws OAuthConsumerSignInTwitterException {
        try {
            consumer.sign(httpRequest);
        } catch(OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e){
            throw new OAuthConsumerSignInTwitterException();
        }
    }
}
