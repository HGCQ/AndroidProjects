package hgcq.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import hgcq.model.service.EventService;
import hgcq.model.service.MemberService;
import hgcq.model.service.PhotoService;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 서버와 통신 설정
 * 쿠키 저장 - saveCookie()
 * 쿠키 삭제 - deleteCookie()
 * 로그인 여부 확인 - isLogin()
 */
public class NetworkClient {
    private static NetworkClient instance;

    private final OkHttpClient okHttpClient;
    private final ClearableCookieJar cookieJar;
    private final Context app;
    private final SharedPrefsCookiePersistor sharedPrefsCookiePersistor;

    private final PhotoService photoService;
    private final MemberService memberService;
    private final EventService eventService;

    private final String serverIp = "서버 주소";

    private NetworkClient(Context context) {
        app = context.getApplicationContext();

        // 쿠키 설정
        sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(app);

        cookieJar = new PersistentCookieJar(new SetCookieCache()
                , sharedPrefsCookiePersistor);

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

    public ClearableCookieJar getCookieJar() {
        return cookieJar;
    }

    public Context getApp() {
        return app;
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

    public SharedPrefsCookiePersistor getSharedPrefsCookiePersistor() {
        return sharedPrefsCookiePersistor;
    }

    public void saveCookie() {
        NetworkClient client = NetworkClient.getInstance(app);

        List<Cookie> cookies = client.getCookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.parse(client.getServerIp())));
        sharedPrefsCookiePersistor.saveAll(cookies);

        for (Cookie ck : cookies) {
            Log.d("받아온 쿠키", "Name: " + ck.name() + " Value: " + ck.value());
        }

        List<Cookie> cookies1 = sharedPrefsCookiePersistor.loadAll();

        for (Cookie ck : cookies1) {
            Log.d("쿠키 저장", "Name: " + ck.name() + " Value: " + ck.value());
        }
    }

    public boolean isLogin() {
        NetworkClient client = NetworkClient.getInstance(app);

        List<Cookie> cookies = client.getCookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.parse(client.getServerIp())));

        return !cookies.isEmpty();
    }

    public void deleteCookie() {
        NetworkClient client = NetworkClient.getInstance(app);

        List<Cookie> cookies = client.getCookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.parse(client.getServerIp())));
        sharedPrefsCookiePersistor.removeAll(cookies);

        Log.d("쿠키 삭제", "성공");
    }
}