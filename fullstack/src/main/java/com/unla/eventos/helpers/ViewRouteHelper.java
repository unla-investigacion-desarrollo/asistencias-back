package com.unla.eventos.helpers;

public class ViewRouteHelper {
	/**** Views ****/
	//HOME
	public final static String INDEX = "home/index";
	public final static String HELLO = "home/hello";
	
	//USER
	public final static String USER_LOGIN = "user/login";
	public final static String USER_LOGOUT = "user/logout";
	
	//EVENT
	public final static String EVENT_INDEX = "event/index";
	public final static String EVENT_SAVE = "event/save";
	
	//REGISTRO
	public final static String REGISTRO_EXITO = "registro/exito";
	public final static String REGISTRO_INDEX = "registro/index";
	public final static String REGISTRO_NOTFOUND = "registro/notfound";
	
	/**** Redirects ****/
	public final static String ROUTE = "/index";
	public final static String EVENTS_CRUD = "redirect:/events";
	
	//MAILS
	public final static String TEMPLATE_REGISTER_USER = "mails/email_register_user";
	public final static String EMAIL_SENDER = "test.unla.labs@gmail.com";
	public final static String CODE = "cvlbdrttfgbhovpy";
}
