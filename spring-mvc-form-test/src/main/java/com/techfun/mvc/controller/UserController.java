package com.techfun.mvc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techfun.mvc.model.User;
import com.techfun.mvc.service.UserService;

@Controller
public class UserController {
	
	
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView("redirect:/users");
	}

	// list page
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView showAllUsers() throws Exception{

		ModelAndView mav = new ModelAndView("users/list");
		List<User> users = userService.findAll();
		mav.addObject("users", users);
		return mav;

	}

	// save or update user
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateUser(@ModelAttribute("userForm") User user) throws Exception{
		userService.saveOrUpdate(user);
		ModelAndView mav = new ModelAndView("redirect:/users/" + user.getId());
		return mav;
	}

	// show add user form
	@RequestMapping(value = "/users/add", method = RequestMethod.GET)
	public ModelAndView showAddUserForm() {

		ModelAndView mav = new ModelAndView("users/userform");

		User user = new User();

		user.setNewsletter(true);
		user.setSex("M");
		user.setFramework(new ArrayList<String>(Arrays.asList("Spring MVC", "GWT")));
		user.setSkill(new ArrayList<String>(Arrays.asList("Spring", "Grails", "Groovy")));
		user.setCountry("SG");
		user.setNumber(2);

		mav.addObject("userForm", user);

		populateDefaultModel(mav);

		return mav;

	}

	// show update form
	@RequestMapping(value = "/users/{id}/update", method = RequestMethod.GET)
	public ModelAndView showUpdateUserForm(@PathVariable("id") int id) throws Exception{

		User user = userService.findById(id);

		ModelAndView mav = new ModelAndView("users/userform");
		mav.addObject("userForm", user);

		populateDefaultModel(mav);
		return mav;
	}

	// delete user
	@RequestMapping(value = "/users/{id}/delete", method = RequestMethod.POST)
	public ModelAndView deleteUser(@PathVariable("id") int id, final RedirectAttributes redirectAttributes) throws Exception{
		userService.delete(id);

		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "User is deleted!");

		return new ModelAndView("redirect:/users");

	}

	// show user
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ModelAndView showUser(@PathVariable("id") int id) throws Exception{
		ModelAndView mav = new ModelAndView("users/show");
		User user = userService.findById(id);
		if (user == null) {
			mav.addObject("css", "danger");
			mav.addObject("msg", "User not found");
		}
		mav.addObject("user", user);
		return mav;

	}

	private void populateDefaultModel(ModelAndView mav) {

		List<String> frameworksList = new ArrayList<String>();
		frameworksList.add("Spring MVC");
		frameworksList.add("Struts 2");
		frameworksList.add("JSF 2");
		frameworksList.add("GWT");
		frameworksList.add("Play");
		frameworksList.add("Apache Wicket");
		mav.addObject("frameworkList", frameworksList);

		Map<String, String> skill = new LinkedHashMap<String, String>();
		skill.put("Hibernate", "Hibernate");
		skill.put("Spring", "Spring");
		skill.put("Struts", "Struts");
		skill.put("Groovy", "Groovy");
		skill.put("Grails", "Grails");
		mav.addObject("javaSkillList", skill);

		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		numbers.add(4);
		numbers.add(5);
		mav.addObject("numberList", numbers);

		Map<String, String> country = new LinkedHashMap<String, String>();
		country.put("US", "United Stated");
		country.put("CN", "China");
		country.put("SG", "Singapore");
		country.put("MY", "Malaysia");
		mav.addObject("countryList", country);

	}

}