package com.enation.eop.processor.back;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.eop.IEopProcessor;
import com.enation.eop.resource.IAdminThemeManager;
import com.enation.eop.resource.model.AdminTheme;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.RequestUtil;
/**
 * 后台模板处理器<br>
 * 负责权限的判断及后台模板的解析<br>
 * @author kingapex
 *2015-3-13
 */
public class BackendProcessor implements IEopProcessor {
	
	
	public boolean process() throws IOException{
		//获取当前管理员
		IAdminUserManager adminUserManager = SpringContextHolder.getBean("adminUserManager");
		AdminUser adminUser  = UserConext.getCurrentAdminUser();
		
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		HttpServletResponse httpResponse=  ThreadContextHolder.getHttpResponse();
		
		String ctx = httpRequest.getContextPath();
		
		
		if("/".equals(ctx)){
			ctx="";
		}
		
		String uri = httpRequest.getServletPath();
		
		if( uri.startsWith("/core/admin/ueditor!getConfigJson.do") ){	//不拦截ueditor加载
			return false;
		}
		
		if( uri.startsWith("/admin/backendUi!login.do")){ //不拦截登陆界面
			return false;
		}
		
		if( uri.startsWith("/admin/backendUi!login.do")){ //登录界面放过
			return false;
		}
		
		//登录验证放过
		if(uri.startsWith("/core/admin/adminUser!login.do")){
			return false;
		}
		
		String redirectUrl ="";
		
		//判断是否访问后台首页
		if(uri.startsWith("/admin") ){
			
			//判断管理员没有登陆、管理员登陆超时，如果没有登陆跳转至登陆页面
			if(adminUser==null){
				redirectUrl=(ctx+"/admin/backendUi!login.do");
			}else{ 
				//如果不是访问的登录界，跳到登录界面
				if(!uri.startsWith("/admin/backendUi")){
					httpResponse.sendRedirect(ctx+"/admin/backendUi!main.do");
					return true;
				}
				return false;
			}
			httpResponse.sendRedirect(redirectUrl);
			return true;
		}else{ 
			//判断管理员没有登陆、或者登陆管理员登陆超时，则登陆跳转至登陆页面
			if(adminUser==null){
				String referer = RequestUtil.getRequestUrl(httpRequest);
				httpResponse.sendRedirect(ctx+"/admin/backendUi!login.do?timeout=yes&referer="+referer);
				return true;
			}else{
				//获取站点信息，存放至request中
				EopSite site=EopSite.getInstance();
				String product_type = EopSetting.PRODUCT;
				httpRequest.setAttribute("site",site);
				httpRequest.setAttribute("ctx",ctx);
				httpRequest.setAttribute("product_type",product_type);
				httpRequest.setAttribute("theme",getAdminTheme(site.getAdminthemeid() ));
			}
		}
		return false;
	}
	
	/**
	 * 获取后台模板
	 * @param themeid 后台模板Id
	 * @return 后台模板
	 */
	private String getAdminTheme(int themeid){
		
		IAdminThemeManager adminThemeManager =SpringContextHolder.getBean("adminThemeManager");
		// 读取后台使用的模板
		AdminTheme theTheme = adminThemeManager.get(themeid);
		
		String theme = "default";
		if (theTheme != null) {
			theme = theTheme.getPath();
		}
		return theme;
	}
	
}
