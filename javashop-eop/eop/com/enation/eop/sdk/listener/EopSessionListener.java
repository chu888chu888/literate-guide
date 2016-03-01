package com.enation.eop.sdk.listener;

import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.enation.eop.resource.IAppManager;
import com.enation.eop.resource.model.EopApp;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.IApp;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.context.spring.SpringContextHolder;
/**
 * EOP Session监听器
 * @author Kanon
 *
 */
public class EopSessionListener implements HttpSessionListener {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	public void sessionCreated(HttpSessionEvent se) {
		
	}
	
	/**
	 * session 失效
	 * @param 
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		
		//session 失效记录日志
		if(logger.isDebugEnabled()){
			logger.debug("session destroyed..");
		}
		
		//如果是已经安装状态
		if("YES".equals( EopSetting.INSTALL_LOCK.toUpperCase())){
			
			if(logger.isDebugEnabled()){
				logger.debug("installed...");
			}
			
			EopSite site = (EopSite) se.getSession().getAttribute("site_key");
			String sessionid = se.getSession().getId();
			IAppManager appManager = SpringContextHolder.getBean("appManager");
			List<EopApp> appList  = appManager.list();
			
			//循环应用列表
			for(EopApp eopApp:appList){
				String appid  = eopApp.getAppid();
				
				if(logger.isDebugEnabled()){
					logger.debug("call app["+appid+"] destory...");
				}
				
				//执行各应用下的session失效方法
				IApp app = SpringContextHolder.getBean(appid);
				app.sessionDestroyed(sessionid,site);
			}
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("not installed...");
			}
		}
	}

}
