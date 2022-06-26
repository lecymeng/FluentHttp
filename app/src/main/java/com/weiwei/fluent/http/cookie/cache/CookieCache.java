package com.weiwei.fluent.http.cookie.cache;

import java.util.Collection;
import okhttp3.Cookie;

/**
 * @author weiwei
 *
 * A CookieCache handles the volatile cookie session storage.
 */
public interface CookieCache extends Iterable<Cookie> {

  /**
   * Add all the new cookies to the session, existing cookies will be overwritten.
   *
   * @param cookies cookie 列表
   */
  void addAll(Collection<Cookie> cookies);

  /**
   * Clear all the cookies from the session.
   */
  void clear();
}
