package hgcq.config;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import hgcq.model.service.EventService;
import hgcq.model.service.MemberService;
import hgcq.model.service.PhotoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    private static NetworkClient instance;


    private static OkHttpClient okHttpClient;
    private static ClearableCookieJar cookieJar;

    private static PhotoService photoService;
    private static MemberService memberService;
    private static EventService eventService;

    private static final String serverIp = "http://192.168.35.193:8080";

    private NetworkClient(Context context) {
        cookieJar = new PersistentCookieJar(new SetCookieCache()
                , new SharedPrefsCookiePersistor(context));

        // Http 메시지 로그
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        // Http 커넥션 설정
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // 서버와 연결
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverIp)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        photoService = retrofit.create(PhotoService.class);
        memberService = retrofit.create(MemberService.class);
        eventService = retrofit.create(EventService.class);
    }

    public static synchronized NetworkClient getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkClient(context);
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public ClearableCookieJar getCookieJar() {
        return cookieJar;
    }

    public String getServerIp() {
        return serverIp;
    }

    public PhotoService getPhotoService() {
        return photoService;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public EventService getEventService() {
        return eventService;
    }
}