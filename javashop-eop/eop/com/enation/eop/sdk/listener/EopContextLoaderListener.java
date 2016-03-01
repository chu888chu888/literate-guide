package com.enation.eop.sdk.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.enation.eop.SystemSetting;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.component.IComponentManager;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * EopLinstener 负责初始化站点缓存
 * @author kingapex
 * 2010-7-18下午04:01:16
 */
public class EopContextLoaderListener implements ServletContextListener {

	
	public void contextDestroyed(ServletContextEvent event) {

	}
	/**
	 * 初始化
	 */
	public void contextInitialized(ServletContextEvent event) {
	 
		if( EopSetting.INSTALL_LOCK.toUpperCase().equals("YES") ){
			
			//重新加载站点设置
			EopSite.reload();
			
			//系统配置
			SystemSetting.load();
			
			//启动组件
			IComponentManager componentManager = SpringContextHolder.getBean("componentManager");
			componentManager.startComponents(); 
		}
		
	}
	
	  

}
