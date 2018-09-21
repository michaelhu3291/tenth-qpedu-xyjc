package data.platform.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import data.framework.support.AbstractBaseController;
/**
 * 平台-角色控制器类。
 * @author wanggq
 */
@Controller
@RequestMapping( "authority/personTree" )
public class PersonTreeController extends AbstractBaseController
{
	
    @Override
    protected void init( ModelMap model, HttpServletRequest request )
    {
    	System.out.println("5555555555555");    
    
    }
    
    
    
    
    
}