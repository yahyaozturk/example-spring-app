package com.quasys.demo.examplespringapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on October, 2018
 *
 * @author xaph
 */
@RestController
public class HelloController {
	@RequestMapping("/")
	public String index() {
		return "Greetings from Yahya Ozturk!";
	}
}
