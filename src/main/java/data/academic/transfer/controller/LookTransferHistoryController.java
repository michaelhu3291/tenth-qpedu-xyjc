package data.academic.transfer.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.framework.support.AbstractBaseController;

@RestController
@RequestMapping("transfer/lookTransferHistory")
public class LookTransferHistoryController extends AbstractBaseController{

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {		
		
	}

}
