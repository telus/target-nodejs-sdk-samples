package com.adobe.targetdemo.controller;

import com.adobe.targetdemo.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@Controller
public class MainController {

  @Autowired
  private ConfigProperties configProperties;

  @GetMapping("/")
  public String index(@RequestParam(name="name", required = false, defaultValue = "World") String name, Model model) {
    HashMap visitorState = new HashMap();
    HashMap serverState = new HashMap();

    model.addAttribute("name", name);
    model.addAttribute("organizationId", configProperties.getOrganizationId());
    model.addAttribute("visitorState", visitorState);
    model.addAttribute("clientCode", configProperties.getClient());
    model.addAttribute("serverDomain", configProperties.getServerDomain());
    model.addAttribute("launchScript", configProperties.getLaunchScript());

    model.addAttribute("serverState", serverState);

    return "index";
  }
}
