package com.weiwei.fluent.http.cookie.persistence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import okhttp3.Cookie;

/**
 * @author weiwei
 */
@SuppressLint("CommitPrefEdits")
public class SharedPrefsCookiePersistDelegate implements CookiePersistDelegate {

  private final SharedPreferences sharedPreferences;

  public SharedPrefsCookiePersistDelegate(Context context) {
    this(context.getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE));
  }

  public SharedPrefsCookiePersistDelegate(SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
  }

  private static String createCookieKey(Cookie cookie) {
    return (cookie.secure() ? "https" : "http") + "://" + cookie.domain() + cookie.path() + "|" + cookie.name();
  }

  @Override
  public List<Cookie> loadAll() {
    List<Cookie> cookies = new ArrayList<>(sharedPreferences.getAll().size());

    for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
      String serializedCookie = (String) entry.getValue();
      Cookie cookie = new SerializableCookie().decode(serializedCookie);
      if (cookie != null) {
        cookies.add(cookie);
      }
    }
    return cookies;
  }

  @Override
  public void saveAll(Collection<Cookie> cookies) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    for (Cookie cookie : cookies) {
      editor.putString(createCookieKey(cookie), new SerializableCookie().encode(cookie));
    }
    editor.commit();
  }

  @Override
  public void removeAll(Collection<Cookie> cookies) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    for (Cookie cookie : cookies) {
      editor.remove(createCookieKey(cookie));
    }
    editor.commit();
  }

  @Override
  public void clear() {
    sharedPreferences.edit().clear().commit();
  }
}
