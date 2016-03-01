package com.enation.eop.processor.core;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;


/**
 *  版权：Copyright (C) 2015  易族智汇（北京）科技有限公司.
 *  本系统是商用软件,未经授权擅自复制或传播本程序的部分或全部将是非法的.
 *  描述：api调用拦截器
 *  修改人：xulipeng
 *  修改时间：2015-12-11
 *  修改内容：制定初版
 */
public class ApiRightInterceptor extends MethodFilterInterceptor {

	
	private static final long serialVersionUID = 2188748169858714415L;
	private String json;

	@Override
	protected String doIntercept(ActionInvocation inv) throws Exception {
		if(EopSetting.PRODUCT.equals("b2c")){
			
			Member member =  UserConext.getCurrentMember();
			if(member==null){
				HttpServletRequest request = ThreadContextHolder.getHttpRequest();
				request.setAttribute("json", "{\"result\":0,\"message\":\"服务器连接超时，请刷新页面！\"}");
				return "json_message";
			}
			
		}else if(EopSetting.PRODUCT.equals("b2b2c")){
			
			Object obj = ThreadContextHolder.getSessionContext().getAttribute("curr_store_member");
			if(obj==null){
				HttpServletRequest request = ThreadContextHolder.getHttpRequest();
				request.setAttribute("json", "{\"result\":0,\"message\":\"服务器连接超时，请刷新页面！\"}"); 
				return "json_message";
			}
		}
		
		String result = inv.invoke();
		return result;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
