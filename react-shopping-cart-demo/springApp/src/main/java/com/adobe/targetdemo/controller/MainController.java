package com.adobe.targetdemo.controller;

import com.adobe.target.edge.client.model.TargetDeliveryResponse;
import com.adobe.targetdemo.ConfigProperties;
import com.adobe.targetdemo.service.TargetClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class MainController {

  @Autowired
  private ConfigProperties configProperties;

  @GetMapping("/")
  public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

    TargetClientService targetClient = new TargetClientService(configProperties);
    TargetDeliveryResponse targetResponse = targetClient.getPrefetchOffers(request, response);

    model.addAttribute("organizationId", configProperties.getOrganizationId());
    model.addAttribute("visitorState", targetResponse.getVisitorState());
    model.addAttribute("clientCode", configProperties.getClient());
    model.addAttribute("serverDomain", configProperties.getServerDomain());
    model.addAttribute("launchScript", configProperties.getLaunchScript());
    model.addAttribute("serverState", targetResponse);

    return "index";
  }
}
