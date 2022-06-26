package com.weiwei.fluent.http.cookie;

import androidx.annotation.NonNull;
import com.weiwei.fluent.http.cookie.cache.CookieCache;
import com.weiwei.fluent.http.cookie.persistence.CookiePersistDelegate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author weiwei
 *
 * franmontiel/PersistentCookieJar
 */
public class PersistentCookieJar implements CookieJar {

  private final CookieCache cache;
  private final CookiePersistDelegate persistDelegate;

  public PersistentCookieJar(CookieCache cache, CookiePersistDelegate persistDelegate) {
    this.cache = cache;
    this.persistDelegate = persistDelegate;

    this.cache.addAll(persistDelegate.loadAll());
  }

  private static List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
    List<Cookie> persistentCookies = new ArrayList<>();

    for (Cookie cookie : cookies) {
      if (cookie.persistent()) {
        persistentCookies.add(cookie);
      }
    }
    return persistentCookies;
  }

  private static boolean isCookieExpired(Cookie cookie) {
    return cookie.expiresAt() < System.currentTimeMillis();
  }

  @Override
  public synchronized void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
    cache.addAll(cookies);
    persistDelegate.saveAll(filterPersistentCookies(cookies));
  }

  @NonNull
  @Override
  public synchronized List<Cookie> loadForRequest(@NonNull HttpUrl url) {
    List<Cookie> cookiesToRemove = new ArrayList<>();
    List<Cookie> validCookies = new ArrayList<>();

    for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
      Cookie currentCookie = it.next();

      if (isCookieExpired(currentCookie)) {
        cookiesToRemove.add(currentCookie);
        it.remove();

      } else if (currentCookie.matches(url)) {
        validCookies.add(currentCookie);
      }
    }

    persistDelegate.removeAll(cookiesToRemove);

    return validCookies;
  }

  /**
   * Clear all the session cookies while maintaining the persisted ones.
   */
  public synchronized void clearSession() {
    cache.clear();
    cache.addAll(persistDelegate.loadAll());
  }

  /**
   * Clear all the cookies from persistence and from the cache.
   */
  public synchronized void clear() {
    cache.clear();
    persistDelegate.clear();
  }
}
