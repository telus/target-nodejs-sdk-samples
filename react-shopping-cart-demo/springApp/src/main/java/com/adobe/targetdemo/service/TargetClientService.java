package com.adobe.targetdemo.service;

import com.adobe.target.delivery.v1.model.*;
import com.adobe.target.edge.client.ClientConfig;
import com.adobe.target.edge.client.TargetClient;
import com.adobe.target.edge.client.model.TargetCookie;
import com.adobe.target.edge.client.model.TargetDeliveryRequest;
import com.adobe.target.edge.client.model.TargetDeliveryResponse;
import com.adobe.target.edge.client.utils.CookieUtils;
import com.adobe.targetdemo.ConfigProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TargetClientService {

  private static final Logger log = Logger.getLogger(String.valueOf(TargetClientService.class));

  private ConfigProperties configProperties;
  private TargetClient targetClient;

  public TargetClientService(ConfigProperties configProperties) {
    ClientConfig config = ClientConfig.builder()
      .client(configProperties.getClient())
      .organizationId(configProperties.getOrganizationId())
      .build();
    this.configProperties = configProperties;
    this.targetClient = TargetClient.create(config);
  }

  public TargetDeliveryResponse getPrefetchOffers(HttpServletRequest request, HttpServletResponse response, String... mboxes) {
    Cookie[] cookies = request.getCookies();
    Address address = getAddress(request);
    String auth = request.getParameter("authorization");
    Trace trace = null;
    if (auth != null) {
      trace = new Trace().authorizationToken(auth);
    }
    ViewRequest viewRequest = (ViewRequest)new ViewRequest().address(address);
    List<ViewRequest> views = Collections.singletonList(viewRequest);
    TargetDeliveryRequest targetRequest = TargetDeliveryRequest.builder()
      .context(new Context().channel(ChannelType.WEB))
      .cookies(getTargetCookies(cookies))
      .prefetch(new PrefetchRequest().views(views).mboxes(getMboxRequests(mboxes)))
      .trace(trace)
      .build();
    TargetDeliveryResponse targetResponse = this.targetClient.getOffers(targetRequest);
    log.info("targetResponse=" + targetResponse);
    setTargetCookies(targetResponse.getCookies(), response);
    return targetResponse;
  }

  private List<TargetCookie> getTargetCookies(Cookie[] cookies) {
    if (cookies == null) {
      return Collections.emptyList();
    }
    return Arrays.stream(cookies)
      .filter(Objects::nonNull)
      .filter(cookie -> CookieUtils.getTargetCookieNames().contains(cookie.getName()))
      .map(cookie -> new TargetCookie(cookie.getName(), cookie.getValue(), cookie.getMaxAge()))
      .collect(Collectors.toList());
  }

  private HttpServletResponse setTargetCookies(List<TargetCookie> targetCookies,
                                                HttpServletResponse response) {
    targetCookies
      .stream()
      .map(targetCookie -> new Cookie(targetCookie.getName(), targetCookie.getValue()))
      .forEach(cookie -> {
        cookie.setPath("/");
        response.addCookie(cookie);
      });
    return response;
  }

  private Address getAddress(HttpServletRequest request) {
    return new Address()
      .referringUrl(request.getHeader("referer"))
      .url(request.getRequestURL().toString());
  }

  private List<MboxRequest> getMboxRequests(String... name) {
    List<MboxRequest> mboxRequests = new ArrayList<>();
    for (int i = 0; i < name.length; i++) {
      mboxRequests.add(new MboxRequest().name(name[i]).index(i));
    }
    return mboxRequests;
  }
}
