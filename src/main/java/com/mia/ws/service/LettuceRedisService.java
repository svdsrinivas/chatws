package com.mia.ws.service;

import org.springframework.stereotype.Service;

import com.mia.ws.config.AppDefaults;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

@Service
public class LettuceRedisService {

	static StatefulRedisConnection<String, String> connection;
	static StatefulRedisPubSubConnection<String, String> pubSubConnection;
	
	static {
		
		RedisURI redisUri= RedisURI.Builder.redis(AppDefaults.REDIS_HOST).withPort(AppDefaults.REDIS_PORT).build();
		
		RedisClient client = RedisClient.create(redisUri);
		
		connection = client.connect();
		pubSubConnection = client.connectPubSub();
		
	}
	
	 public void set(String key, String value){
         String result = connection.sync().set(key, value);
         System.out.println(result);
  }
 
  public String get(String key){
         String result = connection.sync().get(key);
         return result;
  }
 
  public void subscribe(RedisPubSubListener<String, String> redisPubSubListener, String... channels){
         pubSubConnection.addListener(redisPubSubListener);
        
         RedisPubSubCommands<String, String> sync = pubSubConnection.sync();
        
         sync.subscribe(channels);
  }

  public void unsubscribe(RedisPubSubListener<String, String> redisPubSubListener){
        
         pubSubConnection.removeListener(redisPubSubListener);
        
  }
 
  public void publish(String channel, String message){
         connection.sync().publish(channel, message);
  }
 
  public void publish(String[] channels, String message) {
	  
         for( String channel : channels ){
                connection.sync().publish(channel, message);
         }
        
  }
	
}
