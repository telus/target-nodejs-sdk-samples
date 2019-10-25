package com.adobe.targetdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value = "classpath:config.properties")
public class ConfigProperties {

  @Value("${client}")
  private String client;

  @Value("${organizationId}")
  private String organizationId;

  @Value("${timeout}")
  private int timeout;

  @Value("${serverDomain}")
  private String serverDomain;

  @Value("${cookieDomain}")
  private String cookieDomain;

  @Value("${launchScript}")
  private String launchScript;

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public String getServerDomain() {
    return serverDomain;
  }

  public void setServerDomain(String serverDomain) {
    this.serverDomain = serverDomain;
  }

  public String getCookieDomain() {
    return cookieDomain;
  }

  public void setCookieDomain(String cookieDomain) {
    this.cookieDomain = cookieDomain;
  }

  public String getLaunchScript() {
    return launchScript;
  }

  public void setLaunchScript(String launchScript) {
    this.launchScript = launchScript;
  }
}
